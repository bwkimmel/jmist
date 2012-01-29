/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageInputStream;

import ca.eandb.jmist.framework.loader.openexr.TileDescription.RoundingMode;
import ca.eandb.util.UnexpectedException;
import ca.eandb.util.UnimplementedException;
import ca.eandb.util.io.StreamUtil;

/**
 * @author brad
 *
 */
public final class OpenEXRImage {
	
	private static final int MAGIC = 20000630;
	
	private static final int TILE_FLAG = 0x0200;
	
	private static final int VERSION_MASK = 0xff;

	private final Map<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	private final List<Buffer> data = new ArrayList<Buffer>();
	
	private OpenEXRImage(ImageInputStream source) throws IOException {
		source.setByteOrder(ByteOrder.LITTLE_ENDIAN);

		if (source.readInt() != MAGIC) {
			throw new IOException("Unrecognized format");
		}
		int version = source.readInt();
		boolean tiled = (version & TILE_FLAG) != 0;
		
		version &= VERSION_MASK;
		if (version != 2) {
			throw new IOException("Unsupported version");
		}
		
		source.flush();
		
		AttributeFactory factory = AttributeFactory.getInstance();
	
		while (true) {			
			String name = StreamUtil.readNullTerminatedString(source);
			if (name.isEmpty()) {
				break;
			}

			String type = StreamUtil.readNullTerminatedString(source);
			int size = source.readInt();

			source.flush();
			Attribute attribute = factory.create(type, size, source);
			
			if (attribute != null) {
				attributes.put(name, attribute);
			}
			
			source.seek(source.getFlushedPosition() + size);
			source.flush();
		}
		
		Box2i dw = getDataWindow();
		CompressionMethod cm = getCompressionMethod();
		
		source.flush();
		
		if (tiled) {
			TileDescription td = getTiles();
			int numTiles;
			switch (td.getLevelMode()) {
			case ONE_LEVEL:
				numTiles = (1 + (dw.getYSize() - 1) / td.getYSize())
						* (1 + (dw.getXSize() - 1) / td.getXSize());
				break;
				
			case MIPMAP_LEVELS:
			{
				int w = dw.getXSize();
				int h = dw.getYSize();
				int tw = td.getXSize();
				int th = td.getYSize();
				RoundingMode rm = td.getRoundingMode();
				int n = 1 + round(Math.log(Math.max(w, h)) / Math.log(2.0), rm);
				numTiles = 0;
				for (int i = 0; i < n; i++) {
					int lx = round(w / Math.pow(2.0, i), rm);
					int ly = round(h / Math.pow(2.0, i), rm);
					int tx = 1 + (lx - 1) / tw;
					int ty = 1 + (ly - 1) / th;
					numTiles += tx * ty;
				}
				break;
			}
				
			case RIPMAP_LEVELS:
			{
				int w = dw.getXSize();
				int h = dw.getYSize();
				int tw = td.getXSize();
				int th = td.getYSize();
				RoundingMode rm = td.getRoundingMode();
				int nx = 1 + round(Math.log(w) / Math.log(2.0), rm);
				int ny = 1 + round(Math.log(h) / Math.log(2.0), rm);
				numTiles = 0;
				for (int j = 0; j < ny; j++) {
					int ly = round(h / Math.pow(2.0, j), rm);
					int ty = 1 + (ly - 1) / th;
					for (int i = 0; i < nx; i++) {
						int lx = round(w / Math.pow(2.0, i), rm);
						int tx = 1 + (lx - 1) / tw;
						numTiles += tx * ty;
					}
				}
				break;
			}
			
			default:
				throw new UnexpectedException("Invalid level mode.");
			}
			source.seek(source.getFlushedPosition() + 8 * numTiles);
			source.flush();
			
			throw new UnimplementedException();
			
		} else { // scan lines
			int w = dw.getXSize();
			int h = dw.getYSize();
			int ymin = dw.getYMin();
			int ymax = dw.getYMax();
			int blockHeight = cm.getScanLinesPerBlock();
			int numBlocks = 1 + (h - 1) / blockHeight;
			ChannelList chlist = getChannelList();
			int numChannels = chlist.channels().size();
			int lineSize = 0;
			
			for (Channel channel : chlist.channels()) {
				int xs = 1 + (w - 1) / channel.getxSampling();
				int ys = 1 + (h - 1) / channel.getySampling();
				
				switch (channel.getPixelType()) {
				case UINT:
					lineSize += xs * 4;
					data.add(IntBuffer.allocate(xs * ys));
					break;
					
				case FLOAT:
					lineSize += xs * 4;
					data.add(FloatBuffer.allocate(xs * ys));
					break;
					
				case HALF:
					lineSize += xs * 2;
					data.add(ShortBuffer.allocate(xs * ys));
					break;
				}
			}
			
			source.seek(source.getFlushedPosition() + 8 * numBlocks);
			source.flush();
			
			for (int i = 0; i < numBlocks; i++) {
				int y0 = source.readInt();
				int y1 = Math.min(y0 + blockHeight - 1, ymax);
				int size = source.readInt();
				int blockSize = lineSize * (y1 - y0 + 1);
				
				IIOByteBuffer buf = new IIOByteBuffer(null, 0, 0);
				source.readBytes(buf, size);				
				if (size < blockSize) {
					cm.decompress(buf);
					if (buf.getLength() < blockSize) {
						throw new RuntimeException("Undersized block");
					}
				}
				
				ByteBuffer inBuf = ByteBuffer.wrap(buf.getData(), buf.getOffset(), buf.getLength())
						.order(ByteOrder.LITTLE_ENDIAN);
				
				for (int y = y0; y <= y1; y++) {
					for (int c = 0; c < numChannels; c++) {
						Channel channel = chlist.channels().get(c);
						
						int numElem = 1 + (w - 1) / channel.getxSampling();
						switch (channel.getPixelType()) {
						case UINT:
							{
								IntBuffer chBuf = (IntBuffer) data.get(c);
								chBuf.position((y - ymin) * numElem);
								chBuf.put((IntBuffer) inBuf.asIntBuffer().limit(numElem));
								inBuf.position(inBuf.position() + numElem * 4);
								break;
							}
							
						case HALF:
							{
								ShortBuffer chBuf = (ShortBuffer) data.get(c);
								chBuf.position((y - ymin) * numElem);
								chBuf.put((ShortBuffer) inBuf.asShortBuffer().limit(numElem));
								inBuf.position(inBuf.position() + numElem * 2);
								break;
							}
							
						case FLOAT:
							{
								FloatBuffer chBuf = (FloatBuffer) data.get(c);
								chBuf.position((y - ymin) * numElem);
								chBuf.put((FloatBuffer) inBuf.asFloatBuffer().limit(numElem));
								inBuf.position(inBuf.position() + numElem * 4);
								break;
							}
							
						}
					}
				}
			}
		}
		
		
	}
	
	private static int round(double x, TileDescription.RoundingMode mode) {
		switch (mode) {
		case DOWN: return (int) Math.floor(x);
		case UP: return (int) Math.ceil(x);
		default: throw new UnexpectedException("Invalid rounding mode");
		}
	}
	
	public static OpenEXRImage read(ImageInputStream source) throws IOException {
		return new OpenEXRImage(source);
	}
	
	public ChannelList getChannelList() {
		return (ChannelList) attributes.get("channels");
	}
	
	public CompressionMethod getCompressionMethod() {
		return (CompressionMethod) attributes.get("compression");
	}
	
	public Box2i getDataWindow() {
		return (Box2i) attributes.get("dataWindow");
	}
	
	public Box2i getDisplayWindow() {
		return (Box2i) attributes.get("displayWindow");
	}
	
	public LineOrder getLineOrder() {
		return (LineOrder) attributes.get("lineOrder");
	}
	
	public float getPixelAspectRatio() {
		return ((FloatAttribute) attributes.get("pixelAspectRatio")).getValue();
	}
	
	public V2f getScreenWindowCenter() {
		return (V2f) attributes.get("screenWindowCenter");
	}
	
	public float getScreenWindowWidth() {
		return ((FloatAttribute) attributes.get("screenWindowWidth")).getValue();
	}
	
	public TileDescription getTiles() {
		return (TileDescription) attributes.get("tiles");
	}
	
	public long getUnsignedInt(int x, int y, int c) {
		Channel ch = getChannelList().channels().get(c);
		Box2i dw = getDataWindow();
		int xmin = dw.getXMin();
		int ymin = dw.getYMin();
		int xs = ch.getxSampling();
		int ys = ch.getySampling();
		int x0 = (x - xmin) / xs;
		int y0 = (y - ymin) / ys;
		int w0 = dw.getXSize() / xs;
		int index = y0 * w0 + x0;
		IntBuffer buf = (IntBuffer) data.get(c);
		
		return ((long) buf.get(index)) & 0xffffffffL;
	}
	
	public Half getHalf(int x, int y, int c) {
		Channel ch = getChannelList().channels().get(c);
		Box2i dw = getDataWindow();
		int xs = ch.getxSampling();
		int ys = ch.getySampling();
		int x0 = x / xs;
		int y0 = y / ys;
		int w0 = dw.getXSize() / xs;
		int index = y0 * w0 + x0;
		ShortBuffer buf = (ShortBuffer) data.get(c);
		
		return Half.fromShortBits(buf.get(index));
	}
	
	public float getFloat(int x, int y, int c) {
		Channel ch = getChannelList().channels().get(c);
		Box2i dw = getDataWindow();
		int xs = ch.getxSampling();
		int ys = ch.getySampling();
		int x0 = x / xs;
		int y0 = y / ys;
		int w0 = dw.getXSize() / xs;
		int index = y0 * w0 + x0;
		FloatBuffer buf = (FloatBuffer) data.get(c);
		
		return buf.get(index);
	}
	
	public void setUnsignedInt(int x, int y, int c, long value) {
		Channel ch = getChannelList().channels().get(c);
		Box2i dw = getDataWindow();
		int xs = ch.getxSampling();
		int ys = ch.getySampling();
		int x0 = x / xs;
		int y0 = y / ys;
		int w0 = dw.getXSize() / xs;
		IntBuffer buf = (IntBuffer) data.get(c);
		
		buf.put(y0 * w0 + x0, (int) value);
	}
	
	public void setHalf(int x, int y, int c, Half value) {
		Channel ch = getChannelList().channels().get(c);
		Box2i dw = getDataWindow();
		int xs = ch.getxSampling();
		int ys = ch.getySampling();
		int x0 = x / xs;
		int y0 = y / ys;
		int w0 = dw.getXSize() / xs;
		ShortBuffer buf = (ShortBuffer) data.get(c);

		buf.put(y0 * w0 + x0, value.toShortBits());
	}
	
	public void setFloat(int x, int y, int c, float value) {
		Channel ch = getChannelList().channels().get(c);
		Box2i dw = getDataWindow();
		int xs = ch.getxSampling();
		int ys = ch.getySampling();
		int x0 = x / xs;
		int y0 = y / ys;
		int w0 = dw.getXSize() / xs;
		FloatBuffer buf = (FloatBuffer) data.get(c);
		
		buf.put(y0 * w0 + x0, value);
	}
	
	public static void main(String[] args) {
		try {
			OpenEXRImage image = new OpenEXRImage(new FileImageInputStream(new File("/home/brad/Downloads/openexr-images-1.5.0/ScanLines/Blobbies.exr")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
