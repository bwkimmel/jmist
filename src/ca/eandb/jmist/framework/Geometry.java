/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.*;

/**
 * @author Brad Kimmel
 *
 */
public interface Geometry extends Bounded3, VisibilityFunction3, PartialBoundable3 {

	void intersect(Ray3 ray, IntersectionRecorder recorder);

//	WeightedSurfacePoint generateRandomSurfacePoint();
//
	boolean isClosed();

}
