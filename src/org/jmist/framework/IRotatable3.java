/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author brad
 *
 */
public interface IRotatable3 {

	/**
	 * Rotates the object about the x-axis.
	 * Equivalent to {@code this.rotate(Vector3.I, angle);}
	 * @param angle The angle (in radians) to rotate the object by.
	 * @see rotate
	 */
	void rotateX(double angle);

	/**
	 * Rotates the object about the y-axis.
	 * Equivalent to {@code this.rotate(Vector3.J, angle);}
	 * @param angle The angle (in radians) to rotate the object by.
	 * @see rotate
	 */
	void rotateY(double angle);

	/**
	 * Rotates the object about the z-axis.
	 * Equivalent to {@code this.rotate(Vector3.K, angle);}
	 * @param angle The angle (in radians) to rotate the object by.
	 * @see rotate
	 */
	void rotateZ(double angle);

	/**
	 * Rotates the object about an arbitrary axis.
	 * @param axis The axis to rotate the object around (must not be Vector3.ZERO).
	 * @param angle The angle (in radians) to rotate the object by.
	 */
	void rotate(Vector3 axis, double angle);

}
