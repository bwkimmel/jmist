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
@OpenEXRAttributeType("v2f")
public final class V2f implements Attribute {

	private final float x;
	private final float y;
	
	public V2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public static V2f read(DataInput in, int size) throws IOException {
		return new V2f(in.readFloat(), in.readFloat());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(x);
		out.writeFloat(y);
	}

}
