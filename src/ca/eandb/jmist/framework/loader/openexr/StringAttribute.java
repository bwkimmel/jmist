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
@OpenEXRAttributeType("string")
public final class StringAttribute implements Attribute {
	
	private final String value;
	
	public StringAttribute(String value) {
		this.value = value;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public static StringAttribute read(DataInput in, int size) throws IOException {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < size; i++) {
			int b = in.readByte();
			s.append((char) b);
		}
		return new StringAttribute(s.toString());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeBytes(value);
	}

}
