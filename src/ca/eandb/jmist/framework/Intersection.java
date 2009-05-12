/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.*;

/**
 * @author Brad Kimmel
 *
 */
public interface Intersection extends SurfacePoint {

	/**
	 * Gets the incident direction.
	 * @return The incident direction.
	 */
	Vector3 incident();

	/**
	 * Gets the distance from the ray origin to the intersection
	 * point.
	 * @return The distance from the ray origin to the intersection.
	 */
	double distance();

	/**
	 * Indicates whether the incident ray approaches the interface from the
	 * front (the side toward which the normal points).
	 * @return A value indicating whether the ray approaches the interface
	 * 		from the front.
	 */
	boolean front();

}