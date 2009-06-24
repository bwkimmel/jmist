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
	 * an object within the given interval.
	 * @param ray The ray with which to check for an intersection.
	 * @param maximumDistance The maximumDistance along the ray to consider.
	 * @return True if no intersection was found, false otherwise.
	 */
	boolean visibility(Ray3 ray, double maximumDistance);

	/**
	 * Determines whether the given ray intersects
	 * an object within the interval [0, infinity).
	 * @param ray The ray with which to check for an intersection.
	 * @return True if no intersection was found, false otherwise.
	 */
	boolean visibility(Ray3 ray);

	/**
	 * Determines whether the line segment between two
	 * points intersects an object.
	 * Equivalent to {@code this.visibility(new Ray(p, p.vectorTo(q)), Interval.UNIT)}
	 * @param p The point at the start of the line segment.
	 * @param q The point at the end of the line segment.
	 * @return True if no intersection was found, false otherwise.
	 * @see #visibility(Ray3, Interval)
	 */
	boolean visibility(Point3 p, Point3 q);

}
