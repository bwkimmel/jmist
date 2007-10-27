/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * A three dimensional object that can be stretched along an arbitrary axis.
 * @author brad
 */
public interface Stretchable3 extends AxisStretchable3 {

	/**
	 * Stretches the object along an arbitrary axis.
	 * @param axis The axis along which to stretch the object (must not be Vector3.ZERO).
	 * @param c The factor by which to stretch the object.
	 */
	void stretch(Vector3 axis, double c);

}
