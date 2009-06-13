/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public interface Geometry extends Bounded3, VisibilityFunction3, PartialBoundable3 {

	void intersect(Ray3 ray, IntersectionRecorder recorder);

//	WeightedSurfacePoint generateRandomSurfacePoint();
//
	boolean isClosed();

	/**
	 * Computes the surface area of this geometry (optional operation).
	 * @return The surface area of this geometry.
	 * @throws UnsupportedOperationException If the operation is not
	 * 		supported by this <code>Geometry</code>.
	 */
	double getSurfaceArea();

	/**
	 * Generates a <code>SurfacePointGeometry</code> randomly that is uniformly
	 * distributed over the surface of this <code>Geometry</code> (optional
	 * operation).
	 * @return The randomly generated <code>SurfacePointGeometry</code>.
	 * @throws UnsupportedOperationException If the operation is not supported
	 * 		by this <code>Geometry</code>.
	 */
	SurfacePointGeometry generateRandomSurfacePoint();

}
