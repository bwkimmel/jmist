/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author brad
 *
 */
public final class Channel implements Attribute {

	private final String name;
	
	private final PixelType pixelType;
	
	private final byte pLinear;
	
	private final int xSampling;
	
	private final int ySampling;
	
	public Channel(String name, PixelType pixelType, byte pLinear, int xSampling, int ySampling) {
		this.name = name;
		this.pixelType = pixelType;
		this.pLinear = pLinear;
		this.xSampling = xSampling;
		this.ySampling = ySampling;
		if (pLinear != 1 && pLinear != 0) {
			throw new IllegalArgumentException("pLinear must be 0 or 1");
		}
	}
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the pixelType
	 */
	public final PixelType getPixelType() {
		return pixelType;
	}

	/**
	 * @return the pLinear
	 */
	public final byte getpLinear() {
		return pLinear;
	}

	/**
	 * @return the xSampling
	 */
	public final int getxSampling() {
		return xSampling;
	}

	/**
	 * @return the ySampling
	 */
	public final int getySampling() {
		return ySampling;
	}

	public static Channel read(DataInput in) throws IOException {
		String name = readString(in);
		if (name.isEmpty()) {
			return null;
		}
		PixelType pixelType = PixelType.read(in);
		byte pLinear = in.readByte();
		for (int i = 0; i < 3; i++) { in.readByte(); }
		int xSampling = in.readInt();
		int ySampling = in.readInt();
		return new Channel(name, pixelType, pLinear, xSampling, ySampling);
	}
	
	private static String readString(DataInput in) throws IOException {
		StringBuilder s = new StringBuilder();
		while (true) {
			int b = in.readByte();
			if (b <= 0) {
				break;
			}
			s.append((char) b);
		}
		return s.toString();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeBytes(name);
		out.writeByte(0);
		pixelType.write(out);
		out.writeByte(pLinear);
		for (int i = 0; i < 3; i++) { out.writeByte(0); }
		out.writeInt(xSampling);
		out.writeInt(ySampling);
	}
	
}
