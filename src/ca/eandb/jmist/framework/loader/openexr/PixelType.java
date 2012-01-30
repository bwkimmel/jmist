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
public enum PixelType implements Attribute {

	UINT(0, 4),
	HALF(1, 2),
	FLOAT(2, 4);
	
	private final int key;
	
	private final int sampleSize;
	
	private PixelType(int key, int sampleSize) {
		this.key = key;
		this.sampleSize = sampleSize;
	}
	
	/**
	 * Gets the size of a sample of this type, in bytes.
	 * @return The size of a sample of this type, in bytes.
	 */
	public int getSampleSize() {
		return sampleSize;
	}
	
	public static PixelType read(DataInput in) throws IOException {
		int key = in.readInt();
		for (PixelType type : PixelType.values()) {
			if (type.key == key) {
				return type;
			}
		}
		return null;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(key);
	}
	
}
