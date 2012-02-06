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
@OpenEXRAttributeType("float")
public final class FloatAttribute implements Attribute {
	
	private final float value;
	
	public FloatAttribute(float value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}
	
	public static FloatAttribute read(DataInput in, int size) throws IOException {
		return new FloatAttribute(in.readFloat());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(value);
	}

}
