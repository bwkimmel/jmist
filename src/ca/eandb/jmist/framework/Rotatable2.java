/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * Something that may be rotated in two-dimensional space.
 * @author Brad Kimmel
 */
public interface Rotatable2 {

	/**
	 * Rotates the object.
	 * @param angle The angle (in radians) to rotate the object by.
	 */
	void rotate(double angle);

}
