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

	/** The origin of the ray. */
	private final Point2 origin;

	/** The direction of the ray. */
	private final Vector2 direction;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 3350510125101588443L;

}
