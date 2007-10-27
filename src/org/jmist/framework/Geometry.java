/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface Geometry extends Bounded3, VisibilityFunction3 {

	void intersect(Ray3 ray, Interval I, IntersectionRecorder recorder);

	WeightedSurfacePoint generateRandomSurfacePoint();

	boolean isClosed();

}
