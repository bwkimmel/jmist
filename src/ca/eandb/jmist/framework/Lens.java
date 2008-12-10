/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.*;

/**
 * Generates the ray to cast corresponding to given points on the
 * image plane.
 * @author Brad Kimmel
 */
public interface Lens {

	/**
	 * Gets a ray indicating from which point and
	 * direction the camera is sensitive to incoming
	 * light at the specified point on its image plane.
	 * This will correspond to the direction to cast
	 * a ray in order to shade the specified point on
	 * the image plane.
	 * @param p The point on the image plane in
	 * 		normalized device coordinates (must fall
	 * 		within {@code Box2.UNIT}).
	 * @return The ray to cast for ray shading.
	 * @see {@link Box2#UNIT}
	 */
	Ray3 rayAt(Point2 p);

}
