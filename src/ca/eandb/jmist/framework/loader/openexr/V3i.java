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
@OpenEXRAttributeType("v3i")
public final class V3i implements Attribute {

	private final int x;
	private final int y;
	private final int z;
	
	public V3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static V3i read(DataInput in, int size) throws IOException {
		return new V3i(in.readInt(), in.readInt(), in.readInt());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(z);
	}

}
