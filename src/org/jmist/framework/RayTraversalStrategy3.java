/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Ray3;

/**
 * A strategy for determining with what objects to perform ray intersection
 * tests.
 * @author bkimmel
 */
public interface RayTraversalStrategy3 {

	/**
	 * Intersects the specified <code>Ray3</code> with the objects in this
	 * collection.
	 * @param ray The <code>Ray3</code> to intersect with the objects in this
	 * 		hierarchy.
	 * @param I The <code>Interval</code> along the <code>ray</code> to
	 * 		consider.
	 * @param visitor The <code>Visitor</code> to notify when the bounding box
	 * 		of an item is hit.
	 * @return A value indicating whether the operation was completed without
	 * 		being canceled.
	 */
	boolean intersect(Ray3 ray, Interval I, Visitor visitor);

}
