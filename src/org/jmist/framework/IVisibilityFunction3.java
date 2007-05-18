/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IVisibilityFunction3 {

	/**
	 * Determines whether the given ray intersects
	 * an object within the given interval.
	 * @param ray The ray with which to check for an intersection.
	 * @param I The interval along the ray to consider.
	 * @return True if no intersection was found, false otherwise.
	 */
	boolean visibility(Ray3 ray, Interval I);


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
