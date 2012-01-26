/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.DataInput;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ca.eandb.util.io.LimitedInputStream;
import ca.eandb.util.io.LittleEndianDataInputStream;

/**
 * @author brad
 *
 */
public final class OpenEXRImage {
	
	private static final int MAGIC = 20000630;
	
	private static final int TILE_FLAG = 0x0200;
	
	private static final int VERSION_MASK = 0xff;

	private final Map<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	public static OpenEXRImage read(InputStream in) throws IOException {
		OpenEXRImage image = new OpenEXRImage();
		LittleEndianDataInputStream dis = new LittleEndianDataInputStream(in);

		if (dis.readInt() != MAGIC) {
			throw new IOException("Unrecognized format");
		}
		int version = dis.readInt();
		boolean tiled = (version & TILE_FLAG) != 0;
		
		version &= VERSION_MASK;
		if (version != 2) {
			throw new IOException("Unsupported version");
		}
		
		AttributeFactory factory = AttributeFactory.getInstance();
	
		while (true) {
			String name = readString(dis);
			if (name.isEmpty()) {
				break;
			}
			String type = readString(dis);
			int size = dis.readInt();
			LimitedInputStream lis = new LimitedInputStream(size, in);
			DataInput data = new LittleEndianDataInputStream(lis);
			Attribute attribute = factory.create(type, size, data);
			lis.moveToEnd();
			
			if (attribute != null) {
				image.attributes.put(name, attribute);
			}			
		}
		
		return image;
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
	
	public static void main(String[] args) {
		try {
			read(new FileInputStream("/home/brad/Downloads/openexr-images-1.5.0/LuminanceChroma/Flowers.exr"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String readString(InputStream in) throws IOException {
		StringBuilder s = new StringBuilder();
		int b;
		
		while (true) {
			b = in.read();
			if (b <= 0) {
				break;
			}
			s.append((char) b);
		}
		
		return s.toString();
	}
	
}
