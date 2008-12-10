/**
 *
 */
package ca.eandb.jmist.toolkit;

import java.io.Serializable;

/**
 * A ray (half-line) in three dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Ray3 implements Serializable {

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
	public Point3 origin() {
		return origin;
	}

	/**
	 * Gets the direction of this ray.
	 * @return The direction of this ray.
	 */
	public Vector3 direction() {
		return direction;
	}

	/**
	 * Gets the point that is t units along the ray.
	 * @param t The number of units along the ray to find the point of.
	 * @return The point that is t units along the ray.
	 */
	public Point3 pointAt(double t) {
		return new Point3(
				origin.x() + t * direction.x(),
				origin.y() + t * direction.y(),
				origin.z() + t * direction.z()
		);
	}

	/**
	 * Transforms this <code>Ray3</code> according to the specified
	 * transformation matrix.
	 * @param T The <code>AffineMatrix3</code> representing the transformation
	 * 		to apply.
	 * @return The transformed <code>Ray3</code>.
	 */
	public Ray3 transform(AffineMatrix3 T) {
		return new Ray3(
				T.times(origin),
				T.times(direction)
		);
	}

	/** The origin of the ray. */
	private final Point3 origin;

	/** The direction of the ray. */
	private final Vector3 direction;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -8306947693218836236L;

}
