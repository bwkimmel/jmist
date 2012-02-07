/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.attribute;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof V3i) {
			V3i other = (V3i) obj;
			return (x == other.x && y == other.y && z == other.z);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Integer.valueOf(x).hashCode() ^ Integer.valueOf(y).hashCode()
				^ Integer.valueOf(z).hashCode();
	}

}
