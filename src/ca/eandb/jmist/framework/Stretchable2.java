/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Vector2;

/**
 * A two dimensional object that can be stretched along an arbitrary axis.
 * @author Brad Kimmel
 */
public interface Stretchable2 extends AxisStretchable2 {

	/**
	 * Stretches the object along an arbitrary axis.
	 * @param axis The axis along which to stretch the object (must not be Vector2.ZERO).
	 * @param c The factor by which to stretch the object.
	 */
	void stretch(Vector2 axis, double c);

}
