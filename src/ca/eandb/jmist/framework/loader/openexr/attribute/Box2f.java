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
@OpenEXRAttributeType("box2f")
public final class Box2f implements Attribute {
	
	private final float xMin;
	private final float yMin;
	private final float xMax;
	private final float yMax;
	
	public Box2f(float xMin, float yMin, float xMax, float yMax) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	/**
	 * @return the xMin
	 */
	public float getXMin() {
		return xMin;
	}

	/**
	 * @return the yMin
	 */
	public float getYMin() {
		return yMin;
	}

	/**
	 * @return the xMax
	 */
	public float getXMax() {
		return xMax;
	}

	/**
	 * @return the yMax
	 */
	public float getYMax() {
		return yMax;
	}
	
	public float getXSize() {
		return xMax - xMin;
	}
	
	public float getYSize() {
		return yMax - yMin;
	}

	public static Box2f read(DataInput in, int size) throws IOException {
		return new Box2f(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeFloat(xMin);
		out.writeFloat(yMin);
		out.writeFloat(xMax);
		out.writeFloat(yMax);
	}

}
