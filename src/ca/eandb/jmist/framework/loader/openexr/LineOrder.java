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
@OpenEXRAttributeType("lineOrder")
public enum LineOrder implements Attribute {

	INCREASING_Y(0),
	DECREASING_Y(1),
	RANDOM_Y(2);
	
	private final int key;
	
	private LineOrder(int key) {
		this.key = key;
	}
	
	public static LineOrder read(DataInput in, int size) throws IOException {
		int key = in.readByte();
		for (LineOrder order : LineOrder.values()) {
			if (order.key == key) {
				return order;
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
