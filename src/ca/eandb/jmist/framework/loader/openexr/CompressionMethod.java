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
@OpenEXRAttributeType("compression")
public enum CompressionMethod implements Attribute {
	
	NONE(0, 1),
	RLE(1, 1),
	ZIPS(2, 1),
	ZIP(3, 16),
	PIZ(4, 32),
	PXR24(5, 16),
	B44(6, 32),
	B44A(7, 32);
	
	private final int key;
	
	private final int scanLinesPerBlock;
	
	private CompressionMethod(int key, int scanLinesPerBlock) {
		this.key = key;
		this.scanLinesPerBlock = scanLinesPerBlock;
	}
	
	/**
	 * @return the scanLinesPerBlock
	 */
	public final int getScanLinesPerBlock() {
		return scanLinesPerBlock;
	}

	public static CompressionMethod read(DataInput in, int size) throws IOException {
		int key = in.readByte();
		for (CompressionMethod method : CompressionMethod.values()) {
			if (method.key == key) {
				return method;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(key);
	}

}
