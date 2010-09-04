/**
 *
 */
package ca.eandb.jmist.framework;


/**
 * A two dimensional object that can be stretched along the coordinate axes.
 * @author Brad Kimmel
 */
public interface AxisStretchable2 extends Scalable {

	/**
	 * Stretches the object along the x-axis.
	 * Equivalent to {@code this.stretch(cx, 1.0);}
	 * @param cx The factor by which to stretch the object along the x-axis.
	 * @see stretch
	 */
	void stretchX(double cx);

	/**
	 * Stretches the object along the y-axis.
	 * Equivalent to {@code this.stretch(1.0, cy);}
	 * @param cy The factor by which to stretch the object along the y-axis.
	 * @see stretch
	 */
	void stretchY(double cy);

	/**
	 * Stretches an object along each axis independently.
	 * @param cx The factor by which to scale the object along the x-axis.
	 * @param cy The factor by which to scale the object along the y-axis.
	 */
	void stretch(double cx, double cy);

}
