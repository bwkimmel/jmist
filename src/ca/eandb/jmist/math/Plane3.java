/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A plane in three dimensional space.
 * @author Brad Kimmel
 */
public final class Plane3 implements Serializable {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -5388824832351428994L;

	/**
	 * Creates a <code>Plane3</code>.
	 * @param p A <code>Point3</code> on the plane.
	 * @param normal A <code>Vector3</code> that is normal to the plane.
	 */
	public Plane3(Point3 p, Vector3 normal) {
		this.normal = normal;
		this.d = -normal.dot(p.vectorFromOrigin());
	}

	/**
	 * Gets the <code>Plane3</code> that passes through three
	 * <code>Point3</code>s.
	 * @param p0 The first <code>Point3</code>.
	 * @param p1 The second <code>Point3</code>.
	 * @param p2 The third <code>Point3</code>.
	 * @return The <code>Plane3</code> that passes through three
	 * <code>Point3</code>s.
	 */
	public static Plane3 throughPoints(Point3 p0, Point3 p1, Point3 p2) {
		Vector3 u = p2.vectorTo(p0);
		Vector3 v = p0.vectorTo(p1);
		return new Plane3(p0, u.cross(v).unit());
	}

	/**
	 * Gets the <code>Vector3</code> that is normal to the plane.
	 * @return The <code>Vector3</code> that is normal to the plane.
	 */
	public Vector3 normal() {
		return this.normal;
	}

	/**
	 * Computes the intersection of a <code>Ray3</code> with this plane.
	 * @param ray The <code>Ray3</code> to intersect with this plane.
	 * @return The ray parameter at which the ray intersects the plane (i.e.,
	 * 		<code>ray.pointAt(this.intersect(ray))</code> is a point on this
	 * 		plane.
	 * @see Ray3#pointAt(double)
	 */
	public double intersect(Ray3 ray) {
		return this.intersect(ray.origin(), ray.direction());
	}

	/**
	 * Finds the <code>Point3</code> on this <code>Plane3</code> that is
	 * nearest to the given <code>Point3</code>.
	 * @param p The <code>Point3</code> to project onto this
	 * 		<code>Plane3</code>.
	 * @return The <code>Point3</code> on this <code>Plane3</code> that is
	 * 		nearest to <code>p</code>.
	 */
	public Point3 project(Point3 p) {
		return p.minus(this.normal.times(this.altitude(p)));
	}

	/**
	 * Computes the distance from the given point to this <code>Plane3</code>.
	 * Equivalent to <code>Math.abs(this.altitude(p))</code>.
	 * @param p The <code>Point3</code> to get the distance to.
	 * @return The distance from this <code>Plane3</code> to <code>p</code>.
	 * @see #altitude(Point3)
	 */
	public double distanceTo(Point3 p) {
		return Math.abs(this.altitude(p));
	}

	/**
	 * Computes the multiple of the normal that needs to be subtracted from the
	 * given <code>Point3</code> to obtain a <code>Point3</code> on this
	 * <code>Plane3</code>.
	 * @param p The <code>Point3</code> to compute the altitude of.
	 * @return The multiple of the normal that needs to be subtracted from
	 * 		<code>p</code> to obtain a <code>Point3</code> on this
	 * 		<code>Plane3</code>.  This is the distance to <code>p</code> if
	 * 		<code>p</code> if on the side of this <code>Plane3</code> toward
	 * 		which {@link #normal()} points, or the negative of the distance to
	 * 		<code>p</code> if <code>p</code> is on the opposite side of this
	 * 		<code>Plane3</code> toward which {@link #normal()} points.
	 * @see #distanceTo(Point3)
	 * @see #normal()
	 */
	public double altitude(Point3 p) {
		return -this.intersect(p, this.normal);
	}

	/**
	 * Computes the value, <code>t</code>, where (p + tv)
	 * (<code>p.plus(v.times(t))</code>) is on this <code>Plane3</code>.
	 * @param p The <code>Point3</code>.
	 * @param v The <code>Vector3</code>.
	 * @return The value, <code>t</code>, where (p + tv)
	 * 		(<code>p.plus(v.times(t))</code>) is on this <code>Plane3</code>.
	 * @see Point3#plus(Vector3)
	 * @see Vector3#times(double)
	 */
	private double intersect(Point3 p, Vector3 v) {
		return -(normal.dot(p.vectorFromOrigin()) + d)
				/ normal.dot(v);
	}

	/** A <code>Vector3</code> that is normal to the plane. */
	private final Vector3 normal;

	/**
	 * The altitude of {@link Point3#ORIGIN}.
	 * @see Point3#ORIGIN
	 */
	private final double d;

}
