/**
 *
 */
package framework.math;

/**
 * A ray (half-line) in two dimensional space.
 * @author brad
 */
public final class Ray2 {

	/**
	 * Default constructor.
	 */
	public Ray2() {
	}

	/**
	 * Initializes the origin and direction of the ray.
	 * @param origin The origin of the ray.
	 * @param direction The direction of the ray.
	 */
	public Ray2(Point2 origin, Vector2 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	/**
	 * Sets the origin of this ray.
	 * @param origin The origin of the ray.
	 */
	public void setOrigin(Point2 origin) {
		this.origin = origin;
	}

	/**
	 * Gets the origin of this ray.
	 * @return The origin of this ray.
	 */
	public Point2 getOrigin() {
		return origin;
	}

	/**
	 * Sets the direction of this ray.
	 * @param direction The direction of this ray.
	 */
	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}

	/**
	 * Gets the direction of this ray.
	 * @return The direction of this ray.
	 */
	public Vector2 getDirection() {
		return direction;
	}

	/**
	 * Gets the point that is t units along the ray.
	 * @param t The number of units along the ray to find the point of.
	 * @return The point that is t units along the ray.
	 */
	public Point2 pointAt(double t) {
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
	 * @see Vector2.normalize
	 */
	public void normalize() {
		direction.normalize();
	}

	private Point2 origin;
	private Vector2 direction;

}
