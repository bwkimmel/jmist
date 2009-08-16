/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * A function that determines if a line segment or ray intersects
 * something.
 * @author Brad Kimmel
 */
public interface VisibilityFunction3 {

	/**
	 * Determines whether the given ray intersects
	 * an object within the interval [0, infinity).
	 * @param ray The ray with which to check for an intersection.
	 * @return True if no intersection was found, false otherwise.
	 */
	boolean visibility(Ray3 ray);

}
