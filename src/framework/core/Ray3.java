/**
 *
 */
package framework.core;

/**
 * A ray (half-line) in three dimensional space.
 * This class is immutable.
 * @author brad
 */
public final class Ray3 {

	/**
	 * Initializes the origin and direction of the ray.
	 * @param origin The origin of the ray.
	 * @param direction The direction of the ray.
	 */
	public Ray3(Point3 origin, Vector3 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	/**
	 * Gets the origin of this ray.
	 * @return The origin of this ray.
	 */
	public Point3 getOrigin() {
		return origin;
	}

	/**
	 * Gets the direction of this ray.
	 * @return The direction of this ray.
	 */
	public Vector3 getDirection() {
		return direction;
	}

	/**
	 * Gets the point that is t units along the ray.
	 * @param t The number of units along the ray to find the point of.
	 * @return The point that is t units along the ray.
	 */
	public Point3 pointAt(double t) {
		return origin.plus(direction.times(t));
	}

	/** The origin of the ray. */
	private final Point3 origin;

	/** The direction of the ray. */
	private final Vector3 direction;

}
