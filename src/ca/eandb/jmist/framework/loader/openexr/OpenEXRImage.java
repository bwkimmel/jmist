/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.loader.openexr.attribute.Attribute;
import ca.eandb.jmist.framework.loader.openexr.attribute.AttributeFactory;
import ca.eandb.jmist.framework.loader.openexr.attribute.Box2i;
import ca.eandb.jmist.framework.loader.openexr.attribute.Channel;
import ca.eandb.jmist.framework.loader.openexr.attribute.ChannelList;
import ca.eandb.jmist.framework.loader.openexr.attribute.CompressionMethod;
import ca.eandb.jmist.framework.loader.openexr.attribute.FloatAttribute;
import ca.eandb.jmist.framework.loader.openexr.attribute.LineOrder;
import ca.eandb.jmist.framework.loader.openexr.attribute.OpenEXRAttributeType;
import ca.eandb.jmist.framework.loader.openexr.attribute.PixelType;
import ca.eandb.jmist.framework.loader.openexr.attribute.TileDescription;
import ca.eandb.jmist.framework.loader.openexr.attribute.TileDescription.RoundingMode;
import ca.eandb.jmist.framework.loader.openexr.attribute.V2f;
import ca.eandb.jmist.framework.loader.openexr.attribute.V2i;
import ca.eandb.jmist.math.MathUtil;
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
	
	private static final int VERSION = 2;
	
	private static final Map<String, Class<?>> ATTRIBUTE_TYPES;
	static
	{
		Map<String, Class<?>> types = new HashMap<String, Class<?>>();
		types.put("channels", ChannelList.class);
		types.put("compression", CompressionMethod.class);
		types.put("dataWindow", Box2i.class);
		types.put("displayWindow", Box2i.class);
		types.put("lineOrder", LineOrder.class);
		types.put("pixelAspectRatio", FloatAttribute.class);
		types.put("screenWindowCenter", V2f.class);
		types.put("screenWindowWidth", FloatAttribute.class);
		ATTRIBUTE_TYPES = Collections.unmodifiableMap(types);
	}

	private final Map<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	private final Map<String, Buffer> data = new HashMap<String, Buffer>();
	
	public OpenEXRImage(int w, int h) {
		this(new Box2i(0, 0, w - 1, h - 1));
	}
	
	public OpenEXRImage(Box2i dataWindow) {
		this(dataWindow, dataWindow);
	}
	
	public OpenEXRImage(Box2i dataWindow, Box2i displayWindow) {
		attributes.put("channels", new ChannelList());
		attributes.put("compression", CompressionMethod.NONE);
		attributes.put("dataWindow", dataWindow);
		attributes.put("displayWindow", displayWindow);
		attributes.put("lineOrder", LineOrder.INCREASING_Y);
		attributes.put("pixelAspectRatio", new FloatAttribute(1.0f));
		attributes.put("screenWindowCenter", new V2f(0.0f, 0.0f));
		attributes.put("screenWindowWidth", new FloatAttribute(1.0f));
	}
	
	private OpenEXRImage(ImageInputStream source) throws IOException {
		
		FileOutputStream f = new FileOutputStream("/home/brad/test.dat");
		source.setByteOrder(ByteOrder.LITTLE_ENDIAN);

		if (source.readInt() != MAGIC) {
			throw new IOException("Unrecognized format");
		}
		int version = source.readInt();
		boolean tiled = (version & TILE_FLAG) != 0;
		
		version &= VERSION_MASK;
		if (version != VERSION) {
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
			
			source.seek(source.getFlushedPosition() + 8 * numBlocks);
			
			for (int i = 0; i < numBlocks; i++) {
				source.flush();
				
				int x0 = dw.getXMin();
				int x1 = dw.getXMax();
				int y0 = source.readInt();
				int y1 = Math.min(y0 + blockHeight - 1, ymax);
				Box2i block = new Box2i(x0, y0, x1, y1);
				int size = source.readInt();
				int blockSize = computeTileSize(block);
				
				IIOByteBuffer buf = new IIOByteBuffer(null, 0, 0);
				source.readBytes(buf, size);				
				if (size < blockSize) {
					cm.decompress(buf, block);
					if (buf.getLength() < blockSize) {
						throw new RuntimeException("Undersized block");
					}
				}
				f.write(buf.getData(), buf.getOffset(), buf.getLength());
				
				ByteBuffer inBuf = ByteBuffer.wrap(buf.getData(), buf.getOffset(), buf.getLength())
						.order(ByteOrder.LITTLE_ENDIAN);
				
				for (int y = y0; y <= y1; y++) {
					for (Channel channel : chlist.channels()) {
						String name = channel.getName();
						int sx = channel.getxSampling();
						int sy = channel.getySampling();
						
						if ((y % sy) == 0) {
							int numElem = 1 + (w - 1) / sx;
							PixelType pt = channel.getPixelType();
							switch (pt) {
							case UINT:
								{
									IntBuffer chBuf = (IntBuffer) getChannelBuffer(name);
									chBuf.position(((y - ymin) / sy) * numElem);
									chBuf.put((IntBuffer) inBuf.asIntBuffer().limit(numElem));
									break;
								}
								
							case HALF:
								{
									ShortBuffer chBuf = (ShortBuffer) getChannelBuffer(name);
									chBuf.position(((y - ymin) / sy) * numElem);
									chBuf.put((ShortBuffer) inBuf.asShortBuffer().limit(numElem));
									break;
								}
								
							case FLOAT:
								{
									FloatBuffer chBuf = (FloatBuffer) getChannelBuffer(name);
									chBuf.position(((y - ymin) / sy) * numElem);
									chBuf.put((FloatBuffer) inBuf.asFloatBuffer().limit(numElem));
									break;
								}
								
							} // switch (channel.getPixelType())

							inBuf.position(inBuf.position() + numElem * pt.getSampleSize());
						} // if ((y % sy) == 0)
					} // for (c)
				} // for (y)
			} // for (i)
		} // if (tiled)
		
		source.flush();
		f.close();
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
	
	private Buffer getChannelBuffer(String name) {
		Buffer buf = data.get(name);
		return buf != null ? buf : getChannelBufferSync(name);
	}
	
	private synchronized Buffer getChannelBufferSync(String name) {
		Buffer buf = data.get(name);
		if (buf == null) {
			ChannelList chlist = getChannelList();
			Channel channel = chlist.getChannel(name);
			Box2i dataWindow = getDataWindow();
			int w = dataWindow.getXSize();
			int h = dataWindow.getYSize();
			int sx = 1 + (w - 1) / channel.getxSampling();
			int sy = 1 + (h - 1) / channel.getySampling();
			PixelType pt = channel.getPixelType();
			
			switch (pt) {
			case UINT:
				buf = IntBuffer.allocate(sx * sy);
				break;
				
			case FLOAT:
				buf = FloatBuffer.allocate(sx * sy);
				break;
				
			case HALF:
				buf = ShortBuffer.allocate(sx * sy);
				break;
				
			default:
				throw new UnexpectedException("Invalid pixel type");
			}
			
			data.put(name, buf);
		}
		return buf;
	}
	
	private int computeTileSize(Box2i tile) {
		ChannelList chlist = getChannelList();		
		int x0 = tile.getXMin();
		int y0 = tile.getYMin();
		int x1 = tile.getXMax();
		int y1 = tile.getYMax();
		
		int size = 0;		
		for (Channel channel : chlist.channels()) {
			PixelType type = channel.getPixelType();
			int sx = channel.getxSampling();
			int sy = channel.getySampling();
			int nx = 1 + (x1 - x0 - (x1 % sx)) / sx;
			int ny = 1 + (y1 - y0 - (y1 % sy)) / sy;
			size += nx * ny * type.getSampleSize();
		}
		return size;
	}
	
	private int computeMaximumTileSize(V2i tileSize) {
		ChannelList chlist = getChannelList();
		int tw = tileSize.getX();
		int th = tileSize.getY();
		
		int size = 0;		
		for (Channel channel : chlist.channels()) {
			PixelType type = channel.getPixelType();
			int sx = channel.getxSampling();
			int sy = channel.getySampling();
			int nx = 1 + (tw - 1) / sx;
			int ny = 1 + (th - 1) / sy;
			size += nx * ny * type.getSampleSize();
		}
		return size;
	}
	
	public void write(ImageOutputStream out) throws IOException {
		ChannelList chlist = getChannelList();
		long start = out.getStreamPosition();
		
		out.setByteOrder(ByteOrder.LITTLE_ENDIAN);
		out.writeInt(MAGIC);
		out.writeInt(VERSION);
		
		for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
			Attribute attr = entry.getValue();
			String name = entry.getKey();
			String type = attr.getClass().getAnnotation(OpenEXRAttributeType.class).value();
			out.writeBytes(name);
			out.writeByte(0);
			out.writeBytes(type);
			out.writeByte(0);
			out.flush();
			long attrStart = out.getStreamPosition() + 4;
			out.seek(attrStart);
			attr.write(out);
			long attrEnd = out.getStreamPosition();
			out.seek(out.getFlushedPosition());
			out.writeInt((int) (attrEnd - attrStart));
			out.seek(attrEnd);
			out.flush();
		}
		out.writeByte(0);

		CompressionMethod cm = getCompressionMethod();
		Box2i dw = getDataWindow();
		int xmin = dw.getXMin();
		int ymin = dw.getYMin();
		int xmax = dw.getXMax();
		int ymax = dw.getYMax();
		
		int numBlocks = dw.getYSize() / cm.getScanLinesPerBlock();  
		long blockPtrPos = out.getStreamPosition();
		long blockPos = blockPtrPos + 8 * numBlocks;
		
		int maximumBlockSize = computeMaximumTileSize(new V2i(dw.getXSize(),
				Math.min(dw.getYSize(), cm.getScanLinesPerBlock())));
		byte[] blockData = new byte[maximumBlockSize];
		IIOByteBuffer buf = new IIOByteBuffer(null, 0, 0);
		ByteBuffer bytes = ByteBuffer.wrap(blockData).order(ByteOrder.LITTLE_ENDIAN);
		
		int firstBlock;
		int lastBlock;
		int blockIncr;
		switch (getLineOrder()) {
		case INCREASING_Y:
		case RANDOM_Y:
			firstBlock = 0;
			lastBlock = numBlocks;
			blockIncr = 1;
			break;
			
		case DECREASING_Y:
			firstBlock = numBlocks - 1;
			lastBlock = -1;
			blockIncr = -1;
			break;
			
		default:
			throw new UnexpectedException("Invalid line order");
		}
		
		for (int i = firstBlock; i != lastBlock; i += blockIncr) {
			out.seek(blockPtrPos + 8 * i);
			out.writeLong(blockPos - start);
			out.seek(blockPos);
			bytes.rewind();

			// TODO write the next block here
			int x0 = dw.getXMin();
			int x1 = dw.getXMax();
			int y0 = dw.getYMin() + i * cm.getScanLinesPerBlock();
			int y1 = Math.min(y0 + cm.getScanLinesPerBlock() - 1, dw.getYMax());
			Box2i block = new Box2i(x0, y0, x1, y1);
			int blockSize = computeTileSize(block);
			
			for (int y = y0; y <= y1; y++) {
				for (Channel channel : chlist.channels()) {
					String name = channel.getName();
					int sx = channel.getxSampling();
					int sy = channel.getySampling();
					if (y % sy == 0) {
						int nx = 1 + (x1 - x0 - (x1 % sx)) / sx;
						int offset = ((y - ymin) / sy) * nx;
						Buffer chBuf = getChannelBuffer(name);
						PixelType pt = channel.getPixelType();
						
						switch (pt) {
						case UINT:
							bytes.asIntBuffer().put((IntBuffer)
									((IntBuffer) chBuf).duplicate().position(offset).limit(offset + nx));
							break;
							
						case HALF:
							bytes.asShortBuffer().put((ShortBuffer)
									((ShortBuffer) chBuf).duplicate().position(offset).limit(offset + nx));
							break;
							
						case FLOAT:
							bytes.asFloatBuffer().put((FloatBuffer)
									((FloatBuffer) chBuf).duplicate().position(offset).limit(offset + nx));
							break;
							
						default:
							throw new UnexpectedException("Invalid pixel type");
						}
						
						bytes.position(bytes.position() + nx * pt.getSampleSize());
					}
				}
			}
			
			buf.setData(blockData);
			buf.setOffset(0);
			buf.setLength(blockSize);
			cm.compress(buf, block);
			
			out.writeInt(y0);
			if (buf.getLength() < blockSize) {
				out.writeInt(buf.getLength());
				out.write(buf.getData(), buf.getOffset(), buf.getLength());
			} else {
				out.writeInt(blockSize);
				out.write(blockData);
			}
			
			blockPos = out.getStreamPosition();
		}
		
		out.flush();
		out.close();
	}
	
	public Attribute getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void setAttribute(String name, Attribute value) {
		if (name.equals("dataWindow")) {
			throw new IllegalArgumentException("Cannot set dataWindow after instantiation");
		}
		Class<?> type = ATTRIBUTE_TYPES.get(name);
		if (type != null && value.getClass() != type) {
			String msg = String.format(
					"value is of incorrect type, expected `%s' but got `%s'",
					type.getName(), value.getClass().getName());
			throw new IllegalArgumentException(msg);
		}
		attributes.put(name, value);
	}
	
	public ChannelList getChannelList() {
		return (ChannelList) attributes.get("channels");
	}
	
	public CompressionMethod getCompressionMethod() {
		return (CompressionMethod) attributes.get("compression");
	}
	
	public void setCompressionMethod(CompressionMethod value) {
		attributes.put("compression", value);
	}
	
	public Box2i getDataWindow() {
		return (Box2i) attributes.get("dataWindow");
	}
	
	public Box2i getDisplayWindow() {
		return (Box2i) attributes.get("displayWindow");
	}
	
	public void setDisplayWindow(Box2i value) {
		attributes.put("displayWindow", value);
	}
	
	public LineOrder getLineOrder() {
		return (LineOrder) attributes.get("lineOrder");
	}
	
	public void setLineOrder(LineOrder value) {
		attributes.put("lineOrder", value);
	}
	
	public float getPixelAspectRatio() {
		return ((FloatAttribute) attributes.get("pixelAspectRatio")).getValue();
	}
	
	public void setPixelAspectRatio(float value) {
		attributes.put("pixelAspectRatio", new FloatAttribute(value));
	}
	
	public V2f getScreenWindowCenter() {
		return (V2f) attributes.get("screenWindowCenter");
	}
	
	public void setScreenWindowCenter(V2f value) {
		attributes.put("screenWindowCenter", value);
	}
	
	public float getScreenWindowWidth() {
		return ((FloatAttribute) attributes.get("screenWindowWidth")).getValue();
	}
	
	public void setScreenWindowWidth(float value) {
		attributes.put("screenWindowWidth", new FloatAttribute(value));
	}
	
	public TileDescription getTiles() {
		return (TileDescription) attributes.get("tiles");
	}
	
	public void setTiles(TileDescription value) {
		attributes.put("tiles", value);
	}
	
	public RGB getRGB(int x, int y) {
		return new RGB(
				getFloat(x, y, "R"),
				getFloat(x, y, "G"),
				getFloat(x, y, "B"));
	}
	
	public void setRGB(int x, int y, RGB rgb) {
		setFloat(x, y, "R", (float) rgb.r());
		setFloat(x, y, "G", (float) rgb.g());
		setFloat(x, y, "B", (float) rgb.b());
	}
	
	public long getUnsignedInt(int x, int y, String c) {
		Channel ch = getChannelList().getChannel(c);
		Box2i dw = getDataWindow();
		int xmin = dw.getXMin();
		int ymin = dw.getYMin();
		int sx = ch.getxSampling();
		int sy = ch.getySampling();
		int x0 = (x - xmin) / sx;
		int y0 = (y - ymin) / sy;
		int w0 = dw.getXSize() / sx;
		int index = y0 * w0 + x0;
		
		switch (ch.getPixelType()) {
		case UINT:
			IntBuffer ibuf = (IntBuffer) getChannelBuffer(c);
			return ((long) ibuf.get(index)) & 0xffffffffL;
		
		case HALF:
			ShortBuffer sbuf = (ShortBuffer) getChannelBuffer(c);
			return MathUtil.clamp(Half.fromShortBits(sbuf.get(index)).longValue(), 0, (0x1L << 32) - 1L);
			
		case FLOAT:
			FloatBuffer fbuf = (FloatBuffer) getChannelBuffer(c);
			return MathUtil.clamp((long) fbuf.get(index), 0, (0x1L << 32) - 1L);
			
		default:
			throw new UnexpectedException("Invalid pixel type");
				
		}
	}
	
	public Half getHalf(int x, int y, String c) {
		Channel ch = getChannelList().getChannel(c);
		Box2i dw = getDataWindow();
		int sx = ch.getxSampling();
		int sy = ch.getySampling();
		int xmin = dw.getXMin();
		int ymin = dw.getYMin();
		int x0 = (x - xmin) / sx;
		int y0 = (y - ymin) / sy;
		int w0 = dw.getXSize() / sx;
		int index = y0 * w0 + x0;

		switch (ch.getPixelType()) {
		case UINT:
			IntBuffer ibuf = (IntBuffer) getChannelBuffer(c);
			return Half.valueOf((double) (((long) ibuf.get(index)) & 0xffffffffL));
		
		case HALF:
			ShortBuffer sbuf = (ShortBuffer) getChannelBuffer(c);
			return Half.fromShortBits(sbuf.get(index));
			
		case FLOAT:
			FloatBuffer fbuf = (FloatBuffer) getChannelBuffer(c);
			return Half.valueOf(fbuf.get(index));
			
		default:
			throw new UnexpectedException("Invalid pixel type");
				
		}
	}
	
	public float getFloat(int x, int y, String c) {
		Channel ch = getChannelList().getChannel(c);
		Box2i dw = getDataWindow();
		int sx = ch.getxSampling();
		int sy = ch.getySampling();
		int xmin = dw.getXMin();
		int ymin = dw.getYMin();
		int x0 = (x - xmin) / sx;
		int y0 = (y - ymin) / sy;
		int w0 = dw.getXSize() / sx;
		int index = y0 * w0 + x0;

		switch (ch.getPixelType()) {
		case UINT:
			IntBuffer ibuf = (IntBuffer) getChannelBuffer(c);
			return (float) (((long) ibuf.get(index)) & 0xffffffffL);
		
		case HALF:
			ShortBuffer sbuf = (ShortBuffer) getChannelBuffer(c);
			return Half.fromShortBits(sbuf.get(index)).floatValue();
			
		case FLOAT:
			FloatBuffer fbuf = (FloatBuffer) getChannelBuffer(c);
			return fbuf.get(index);
			
		default:
			throw new UnexpectedException("Invalid pixel type");
				
		}
	}
	
	public void setUnsignedInt(int x, int y, String c, long value) {
		Channel ch = getChannelList().getChannel(c);
		Box2i dw = getDataWindow();
		int sx = ch.getxSampling();
		int sy = ch.getySampling();
		int xmin = dw.getXMax();
		int ymin = dw.getYMin();
		int x0 = (x - xmin) / sx;
		int y0 = (y - ymin) / sy;
		int w0 = dw.getXSize() / sx;
		int index = y0 * w0 + x0;
		
		switch (ch.getPixelType()) {
		case UINT:
			IntBuffer ibuf = (IntBuffer) getChannelBuffer(c);
			ibuf.put(index, (int) value);
			break;
			
		case HALF:
			ShortBuffer sbuf = (ShortBuffer) getChannelBuffer(c);
			sbuf.put(index, Half.valueOf((float) ((int) value)).toShortBits());
			break;
			
		case FLOAT:
			FloatBuffer fbuf = (FloatBuffer) getChannelBuffer(c);
			fbuf.put(index, (float) ((int) value));
			break;
			
		default:
			throw new UnexpectedException("invalid pixel type");
		}
	}
	
	public void setHalf(int x, int y, String c, Half value) {
		Channel ch = getChannelList().getChannel(c);
		Box2i dw = getDataWindow();
		int sx = ch.getxSampling();
		int sy = ch.getySampling();
		int x0 = x / sx;
		int y0 = y / sy;
		int w0 = dw.getXSize() / sx;
		int index = y0 * w0 + x0;
		
		switch (ch.getPixelType()) {
		case UINT:
			IntBuffer ibuf = (IntBuffer) getChannelBuffer(c);
			ibuf.put(index, (int) MathUtil.clamp(value.longValue(), 0, (0x1L << 32) - 1));
			break;
			
		case HALF:
			ShortBuffer sbuf = (ShortBuffer) getChannelBuffer(c);
			sbuf.put(index, value.toShortBits());
			break;
			
		case FLOAT:
			FloatBuffer fbuf = (FloatBuffer) getChannelBuffer(c);
			fbuf.put(index, value.floatValue());
			break;
			
		default:
			throw new UnexpectedException("Invalid pixel type");
		}
	}
	
	public void setFloat(int x, int y, String c, float value) {
		Channel ch = getChannelList().getChannel(c);
		Box2i dw = getDataWindow();
		int sx = ch.getxSampling();
		int sy = ch.getySampling();
		int x0 = x / sx;
		int y0 = y / sy;
		int w0 = dw.getXSize() / sx;
		int index = y0 * w0 + x0;
		
		switch (ch.getPixelType()) {
		case UINT:
			IntBuffer ibuf = (IntBuffer) getChannelBuffer(c);
			ibuf.put(index, (int) MathUtil.clamp((long) value, 0, (0x1L << 32) - 1));
			break;
			
		case HALF:
			ShortBuffer sbuf = (ShortBuffer) getChannelBuffer(c);
			sbuf.put(index, Half.valueOf(value).toShortBits());
			break;
			
		case FLOAT:
			FloatBuffer fbuf = (FloatBuffer) getChannelBuffer(c);
			fbuf.put(index, value);
			break;
			
		default:
			throw new UnexpectedException("Invalid pixel type");
		}
	}
	
	public static void main(String[] args) {
		try {
			OpenEXRImage image = new OpenEXRImage(new FileImageInputStream(new File("/home/brad/Downloads/openexr-images-1.5.0/ScanLines/Blobbies.exr")));
//			image.attributes.put("compression", CompressionMethod.NONE);
			
			PrintStream out = new PrintStream(new FileOutputStream("/home/brad/Downloads/openexr-images-1.5.0/ScanLines/Blobbies2.test"));
			for (int y = -20; y < 1020; y++) {
				for (int x = -20; x < 1020; x++) {
					out.printf("% 8.6e,% 8.6e,% 8.6e,% 8.6e,% 8.6e",
							image.getFloat(x, y, "A"),
							image.getFloat(x, y, "B"),
							image.getFloat(x, y, "G"),
							image.getFloat(x, y, "R"),
							image.getFloat(x, y, "Z"));
					out.println();
				}
			}
			out.flush();
			out.close();
			
			image.write(new FileImageOutputStream(new File("/home/brad/Downloads/openexr-images-1.5.0/ScanLines/Blobbies2.exr")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
