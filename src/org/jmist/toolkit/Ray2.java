/**
 *
 */
package org.jmist.toolkit;

import java.io.Serializable;

/**
 * A ray (half-line) in two dimensional space.
 * This class is immutable.
 * @author brad
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
