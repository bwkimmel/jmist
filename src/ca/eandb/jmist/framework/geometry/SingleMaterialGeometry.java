/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.math.Ray3;

/**
 * A <code>Geometry</code> that consists of a single <code>Material</code>.
 * @author Brad Kimmel
 */
public abstract class SingleMaterialGeometry extends AbstractGeometry {

	/**
	 * Initializes the <code>Material</code> to apply to this
	 * <code>Geometry</code>.
	 * @param material The <code>Material</code> to apply to this
	 * 		<code>Geometry</code>.
	 */
	protected SingleMaterialGeometry(Material material) {
		this.material = material;
	}

	/**
	 * Creates a new <code>Intersection</code>.
	 * @param ray The <code>Ray3</code> intersecting with this
	 * 		<code>Geometry</code>.
	 * @param distance The distance from the ray origin to the intersection.
	 * @param front A value indicating if the ray strikes the front surface of
	 * 		the geometry (i.e., the side of the surface toward which the normal
	 * 		points).
	 * @param surfaceId A value indicating what portion of the surface was
	 * 		struck.
	 * @return A new <code>GeometryIntersection</code>.
	 */
	protected final GeometryIntersection newIntersection(Ray3 ray,
			double distance, boolean front, int surfaceId) {

		return this.newIntersection(ray, distance, front, surfaceId,
				this.material);

	}

	/**
	 * Creates a new <code>Intersection</code>.
	 * @param ray The <code>Ray3</code> intersecting with this
	 * 		<code>Geometry</code>.
	 * @param distance The distance from the ray origin to the intersection.
	 * @param front A value indicating if the ray strikes the front surface of
	 * 		the geometry (i.e., the side of the surface toward which the normal
	 * 		points).
	 * @return A new <code>GeometryIntersection</code>.
	 */
	protected final GeometryIntersection newIntersection(Ray3 ray,
			double distance, boolean front) {

		return this.newIntersection(ray, distance, front, 0, this.material);

	}

	/** The <code>Material</code> to apply to this <code>Geometry</code> */
	private final Material material;

}
