/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A ray (half-line) in three dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Ray3 implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = 599796092151860220L;

	/**
	 * Creates a <code>Ray3</code>.
	 * @param origin The origin of the ray.
	 * @param direction The direction of the ray.
	 * @param limit The maximum units along the ray.
	 */
	public Ray3(Point3 origin, Vector3 direction, double limit) {
		this.origin = origin;
		this.direction = direction;
		this.limit = limit;
	}

	/**
	 * Creates a <code>Ray3</code> with no limit.
	 * @param origin The origin of the ray.
	 * @param direction The direction of the ray.
	 */
	public Ray3(Point3 origin, Vector3 direction) {
		this(origin, direction, Double.POSITIVE_INFINITY);
	}

	/**
	 * Creates a <code>Ray3</code> spanning from one point to another.
	 * @param p The <code>Point3</code> at the origin of the ray.
	 * @param q The <code>Point3</code> at the end of the ray.
	 */
	public Ray3(Point3 p, Point3 q) {
		this.limit = p.distanceTo(q);
		this.origin = p;
		this.direction = p.vectorTo(q).divide(limit);
	}

	/**
	 * Creates a <code>Ray3</code> from one point along a direction or to
	 * another point.
	 * @param p The <code>Point3</code> at the origin of the ray.
	 * @param q The <code>HPoint3</code> indicating the direction and limit of
	 * 		the ray.  If <code>q</code> is a <code>Point3</code>, this creates
	 * 		a finite ray from <code>p</code> to <code>q</code>.  If
	 * 		<code>q</code> is a <code>Vector3</code>, this creates an infinite
	 * 		ray from <code>p</code> in the direction indicated by
	 * 		<code>q</code>.
	 */
	public Ray3(Point3 p, HPoint3 q) {
		this.origin = p;
		if (q.isPoint()) {
			this.limit = p.distanceTo((Point3) q);
			this.direction = p.vectorTo((Point3) q).divide(limit);
		} else {
			this.direction = (Vector3) q;
			this.limit = Double.POSITIVE_INFINITY;
		}
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
	 * Gets the maximum units along the ray.
	 * @return The maximum units along the ray.
	 */
	public double limit() {
		return limit;
	}

	/**
	 * Gets a parallel <code>Ray3</code> with the origin <code>t</code> units
	 * along this <code>Ray3</code>.
	 * @param t The number of units along the ray to advance.
	 * @return The new <code>Ray3</code>.
	 */
	public Ray3 advance(double t) {
		return new Ray3(pointAt(t), direction, limit - t);
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
	 * Gets the end point of this ray.
	 * Equivalent to <code>this.pointAt(this.limit())</code>.
	 * @return The end point of this ray.
	 * @see #pointAt(double)
	 * @see #limit()
	 */
	public Point3 pointAtLimit() {
		return new Point3(
				origin.x() + limit * direction.x(),
				origin.y() + limit * direction.y(),
				origin.z() + limit * direction.z()
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
				T.times(direction),
				limit
		);
	}

	/** The origin of the ray. */
	private final Point3 origin;

	/** The direction of the ray. */
	private final Vector3 direction;

	/** The maximum units along the ray. */
	private final double limit;

}
