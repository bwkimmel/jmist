/**
 *
 */
package framework.math;

/**
 * A ray (half-line) in three dimensional space.
 * @author brad
 */
public final class Ray3 {

	/**
	 * Default constructor.
	 */
	public Ray3() {
	}

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
	 * Sets the origin of this ray.
	 * @param origin The origin of the ray.
	 */
	public void setOrigin(Point3 origin) {
		this.origin = origin;
	}

	/**
	 * Gets the origin of this ray.
	 * @return The origin of this ray.
	 */
	public Point3 getOrigin() {
		return origin;
	}

	/**
	 * Sets the direction of this ray.
	 * @param direction The direction of this ray.
	 */
	public void setDirection(Vector3 direction) {
		this.direction = direction;
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

	/**
	 * Translates the origin of this ray by t units along its direction.
	 * Equivalent to {@code this.setOrigin(this.getOrigin().plus(this.getDirection().times(t)));}
	 * @param t The multiple of the direction of this ray to advance the origin by.
	 */
	public void advance(double t) {
		origin.add(direction.times(t));
	}

	/**
	 * Normalizes this ray (scales the direction so that is of unit length).
	 * Equivalent to {@code this.setDirection(this.getDirection().unit());}
	 * @see Vector3.normalize
	 */
	public void normalize() {
		direction.normalize();
	}

	private Point3 origin;
	private Vector3 direction;

}
