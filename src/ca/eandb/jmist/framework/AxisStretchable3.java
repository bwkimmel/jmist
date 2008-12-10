/**
 *
 */
package ca.eandb.jmist.framework;


/**
 * A three dimensional object that can be stretched along the coordinate axes.
 * @author Brad Kimmel
 */
public interface AxisStretchable3 extends Scalable {

	/**
	 * Stretches the object along the x-axis.
	 * Equivalent to {@code this.stretch(cx, 1.0, 1.0);}
	 * @param cx The factor by which to stretch the object along the x-axis.
	 * @see stretch
	 */
	void stretchX(double cx);

	/**
	 * Stretches the object along the y-axis.
	 * Equivalent to {@code this.stretch(1.0, cy, 1.0);}
	 * @param cy The factor by which to stretch the object along the y-axis.
	 * @see stretch
	 */
	void stretchY(double cy);

	/**
	 * Stretches the object along the z-axis.
	 * Equivalent to {@code this.stretch(1.0, 1.0, cz);}
	 * @param cz The factor by which to stretch the object along the z-axis.
	 * @see stretch
	 */
	void stretchZ(double cz);

	/**
	 * Stretches an object along each axis independently.
	 * @param cx The factor by which to scale the object along the x-axis.
	 * @param cy The factor by which to scale the object along the y-axis.
	 * @param cz The factor by which to scale the object along the z-axis.
	 */
	void stretch(double cx, double cy, double cz);

}
