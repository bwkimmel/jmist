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

	UINT(0),
	HALF(1),
	FLOAT(2);
	
	private final int key;
	
	private PixelType(int key) {
		this.key = key;
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
		out.writeByte(key);
	}
	
}
