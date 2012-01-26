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
@OpenEXRAttributeType("v3f")
public final class V3f implements Attribute {

	private final float x;
	private final float y;
	private final float z;
	
	public V3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static V3f read(DataInput in, int size) throws IOException {
		return new V3f(in.readFloat(), in.readFloat(), in.readFloat());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(z);
	}

}
