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
@OpenEXRAttributeType("box2i")
public final class Box2i implements Attribute {
	
	private final int xMin;
	private final int yMin;
	private final int xMax;
	private final int yMax;
	
	public Box2i(int xMin, int yMin, int xMax, int yMax) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	/**
	 * @return the xMin
	 */
	public int getXMin() {
		return xMin;
	}

	/**
	 * @return the yMin
	 */
	public int getYMin() {
		return yMin;
	}

	/**
	 * @return the xMax
	 */
	public int getXMax() {
		return xMax;
	}

	/**
	 * @return the yMax
	 */
	public int getYMax() {
		return yMax;
	}
	
	public int getXSize() {
		return xMax - xMin;
	}
	
	public int getYSize() {
		return yMax - yMin;
	}

	public static Box2i read(DataInput in, int size) throws IOException {
		return new Box2i(in.readInt(), in.readInt(), in.readInt(), in.readInt());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(xMin);
		out.writeInt(yMin);
		out.writeInt(xMax);
		out.writeInt(yMax);
	}
	
}
