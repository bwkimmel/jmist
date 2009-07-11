/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author Brad Kimmel
 *
 */
public interface SceneElement extends Bounded3, VisibilityFunction3, Serializable {

	/**
	 * Gets the number of primitives contained in this <code>SceneElement</code>.
	 * @return The number of primitives contained in this
	 * 		<code>SceneElement</code>.
	 */
	int getNumPrimitives();

	/**
	 * Computes the intersections between a given ray and the specified
	 * primitive.
	 * @param index The index of the primitive with which to compute
	 * 		intersections.
	 * @param ray A <code>Ray3</code> to intersect with the specified
	 * 		primitive.
	 * @param recorder An <code>IntersectionRecorder</code> to receive the
	 * 		computed intersections.
	 */
	void intersect(int index, Ray3 ray, IntersectionRecorder recorder);

	/**
	 * Computes the intersections between a given ray and this geometry.
	 * @param ray A <code>Ray3</code> to intersect with this geometry.
	 * @param recorder An <code>IntersectionRecorder</code> to receive the
	 * 		computed intersections.
	 */
	void intersect(Ray3 ray, IntersectionRecorder recorder);

	/**
	 * Determines if a given ray intersects with the specified primitive within
	 * the maximum distance provided.
	 * @param index The index of the primitive with which to compute
	 * 		intersections.
	 * @param ray A <code>Ray3</code> to intersect with the specified
	 * 		primitive.
	 * @param maximumDistance The maximum distance along the ray to consider.
	 * @return A value indicating if <code>ray</code> intersects with the
	 * 		specified primitive within the given distance.
	 */
	boolean visibility(int index, Ray3 ray, double maximumDistance);

	/**
	 * Determines if a given ray intersects with this geometry within the
	 * maximum distance provided.
	 * @param ray A <code>Ray3</code> to intersect with this geometry.
	 * @param maximumDistance The maximum distance along the ray to consider.
	 * @return A value indicating if <code>ray</code> intersects with this
	 * 		geometry within the given distance.
	 */
	boolean visibility(Ray3 ray, double maximumDistance);

	/**
	 * Determines if a given ray intersects with the specified primitive.
	 * @param index The index of the primitive with which to compute
	 * 		intersections.
	 * @param ray A <code>Ray3</code> to intersect with the specified
	 * 		primitive.
	 * @return A value indicating if <code>ray</code> intersects with the
	 * 		specified primitive.
	 */
	boolean visibility(int index, Ray3 ray);

	/**
	 * Determines if a given ray intersects with this geometry.
	 * @param ray A <code>Ray3</code> to intersect with this geometry.
	 * @return A value indicating if <code>ray</code> intersects with this
	 * 		geometry.
	 */
	boolean visibility(Ray3 ray);

	/**
	 * Determines if the surface of the specified primitive intersects with the
	 * given box.
	 * @param index The index of the primitive for which to determine
	 * 		intersection.
	 * @param box A <code>Box3</code> to intersect with.
	 * @return True if <code>box</code> intersects with the specified primitive
	 * 		or if it cannot be determined whether <code>box</code> intersects
	 * 		with the specified primitive.  False otherwise.
	 */
	boolean intersects(int index, Box3 box);

	/**
	 * Computes a bounding box for the specified primitive.
	 * @param index The index of the primitive for which to compute the
	 * 		bounding box.
	 * @return A <code>Box3</code> containing the primitive.
	 */
	Box3 getBoundingBox(int index);

	/**
	 * Computes a bounding sphere for the specified primitive.
	 * @param index The index of the primitive for which to compute the
	 * 		bounding sphere.
	 * @return A <code>Sphere</code> containing the primitive.
	 */
	Sphere getBoundingSphere(int index);

	/**
	 * Computes the surface area of the specified primitive (optional
	 * operation).
	 * @return The surface area of the primitive.
	 * @throws UnsupportedOperationException If the operation is not
	 * 		supported by this <code>SceneElement</code>.
	 */
	double getSurfaceArea(int index);

	/**
	 * Computes the surface area of this geometry (optional operation).
	 * @return The surface area of this geometry.
	 * @throws UnsupportedOperationException If the operation is not
	 * 		supported by this <code>SceneElement</code>.
	 */
	double getSurfaceArea();

	/**
	 * Generates a <code>SurfacePoint</code> randomly that is uniformly
	 * distributed over the surface of the specified primitive (optional
	 * operation).
	 * @return The randomly generated <code>SurfacePoint</code>.
	 * @throws UnsupportedOperationException If the operation is not supported
	 * 		by this <code>SceneElement</code>.
	 */
	void generateRandomSurfacePoint(int index, ShadingContext context);

	/**
	 * Generates a <code>SurfacePoint</code> randomly that is uniformly
	 * distributed over the surface of this geometry (optional operation).
	 * @return The randomly generated <code>SurfacePoint</code>.
	 * @throws UnsupportedOperationException If the operation is not supported
	 * 		by this <code>SceneElement</code>.
	 */
	void generateRandomSurfacePoint(ShadingContext context);

	/**
	 * Generates a <code>SurfacePoint</code> randomly that is uniformly
	 * distributed over the surface of the specified primitive (optional
	 * operation).
	 * @return The randomly generated <code>SurfacePoint</code>.
	 * @throws UnsupportedOperationException If the operation is not supported
	 * 		by this <code>SceneElement</code>.
	 */
	double generateImportanceSampledSurfacePoint(int index, SurfacePoint x, ShadingContext context);

	/**
	 * Generates a <code>SurfacePoint</code> randomly that is uniformly
	 * distributed over the surface of this geometry (optional operation).
	 * @return The randomly generated <code>SurfacePoint</code>.
	 * @throws UnsupportedOperationException If the operation is not supported
	 * 		by this <code>SceneElement</code>.
	 */
	double generateImportanceSampledSurfacePoint(SurfacePoint x, ShadingContext context);

	/**
	 * Creates a <code>Light</code> from the emissive surfaces in this scene
	 * element.
	 * @return A <code>Light</code> representing the emissive surfaces in this
	 * 		scene element.
	 */
	Light createLight();

}
