/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IIntersection extends ISurfacePoint {

	/**
	 * Gets the ray that was used to obtain the intersection.
	 * @return The ray that was used to obtain the intersection.
	 */
	Ray3 ray();

	/**
	 * Gets the distance from the ray origin to the intersection
	 * point, divided by the length of the ray direction.
	 * @return The distance from the ray origin to the intersection
	 * 		divided by the length of the ray direction.
	 */
	double rayParameter();

}
