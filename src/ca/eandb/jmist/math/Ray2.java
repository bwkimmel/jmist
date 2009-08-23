/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A ray (half-line) in two dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Ray2 implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = 8111023479108425628L;

	/**
	 * Creates a <code>Ray2</code>.
	 * @param origin The origin of the ray.
	 * @param direction The direction of the ray.
	 * @param limit The maximum units along the ray.
	 */
	public Ray2(Point2 origin, Vector2 direction, double limit) {
		this.origin = origin;
		this.direction = direction;
		this.limit = limit;
	}

	/**
	 * Creates a <code>Ray2</code> with no limit.
	 * @param origin The origin of the ray.
	 * @param direction The direction of the ray.
	 */
	public Ray2(Point2 origin, Vector2 direction) {
		this(origin, direction, Double.POSITIVE_INFINITY);
	}

	/**
	 * Creates a <code>Ray2</code> spanning from one point to another.
	 * @param p The <code>Point2</code> at the origin of the ray.
	 * @param q The <code>Point2</code> at the end of the ray.
	 */
	public Ray2(Point2 p, Point2 q) {
		this.limit = p.distanceTo(q);
		this.origin = p;
		this.direction = p.vectorTo(q).divide(limit);
	}

	/**
	 * Gets the origin of this ray.
	 * @return The origin of this ray.
	 */
	public Point2 origin() {
		return origin;
	}

	/**
	 * Gets the direction of this ray.
	 * @return The direction of this ray.
	 */
	public Vector2 direction() {
		return direction;
	}

	/**
	 * Gets the maximum number of units along the ray.
	 * @return The maximum number of units along this ray.
	 */
	public double limit() {
		return limit;
	}

	/**
	 * Gets a value indicating if this ray is infinite.  Equivalent to
	 * <code>Double.isInfinite(this.limit())</code>.
	 * @return A value indicating if this ray is infinite.
	 * @see #limit()
	 */
	public boolean isInfinite() {
		return Double.isInfinite(limit);
	}

	/**
	 * Gets a parallel <code>Ray2</code> with the origin <code>t</code> units
	 * along this <code>Ray2</code>.
	 * @param t The number of units along the ray to advance.
	 * @return The new <code>Ray2</code>.
	 */
	public Ray2 advance(double t) {
		return new Ray2(pointAt(t), direction);
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
	 * Gets the end point of this ray.
	 * Equivalent to <code>this.pointAt(this.limit())</code>.
	 * @return The end point of this ray.
	 * @see #pointAt(double)
	 * @see #limit()
	 */
	public Point2 pointAtLimit() {
		return pointAt(limit);
	}

	/** The origin of the ray. */
	private final Point2 origin;

	/** The direction of the ray. */
	private final Vector2 direction;

	/** The maximum units along the ray. */
	private final double limit;

}
