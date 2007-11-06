/**
 *
 */
package org.jmist.toolkit;

import java.io.Serializable;

import org.jmist.util.MathUtil;

/**
 * A location in three dimensional space.
 * This class is immutable.
 * @author brad
 */
public final class Point3 implements Serializable {

	/**
	 * Initializes the components for the point.
	 * @param x The distance from the origin along the x axis.
	 * @param y The distance from the origin along the y axis.
	 * @param z The distance from the origin along the z axis.
	 */
	public Point3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets the distance from the origin along the x-axis.
	 * Equivalent to {@code this.minus(Point3.ORIGIN).dot(Vector3.I);}
	 * @return The distance from the origin along the x-axis.
	 * @see {@link #getX()}, {@link Vector3#I},
	 * 		{@link Vector3#dot(Vector3)}.
	 */
	public double x() {
		return x;
	}

	/**
	 * Gets the distance from the origin along the y-axis.
	 * Equivalent to {@code this.minus(Point3.ORIGIN).dot(Vector3.J);}
	 * @return The distance from the origin along the y-axis.
	 * @see {@link #getY()}, {@link Vector3#J},
	 * 		{@link Vector3#dot(Vector3)}.
	 */
	public double y() {
		return y;
	}

	/**
	 * Gets the distance from the origin along the z-axis.
	 * Equivalent to {@code this.minus(Point3.ORIGIN).dot(Vector3.K);}
	 * @return The distance from the origin along the z-axis.
	 * @see {@link #getZ()}, {@link Vector3#K},
	 * 		{@link Vector3#dot(Vector3)}.
	 */
	public double z() {
		return z;
	}

	/**
	 * Computes the square of the distance from this point to the
	 * specified point.
	 * @param p The point to compute the square of the distance to.
	 * @return The square of the distance between this point and
	 * the specified point.
	 */
	public double squaredDistanceTo(Point3 p) {
		return ((x - p.x) * (x - p.x)) + ((y - p.y) * (y - p.y)) + ((z - p.z) * (z - p.z));
	}

	/**
	 * Computes the distance between this point and the specified point.
	 * @param p The point to compute the distance to.
	 * @return The distance between this point and p.
	 */
	public double distanceTo(Point3 p) {
		return Math.sqrt(squaredDistanceTo(p));
	}

	/**
	 * Computes the vector from this point to the specified point.
	 * @param p The point at the end of the vector.
	 * @return The vector from this point to p.
	 */
	public Vector3 vectorTo(Point3 p) {
		return new Vector3(p.x - x, p.y - y, p.z - z);
	}

	/**
	 * Computes the vector from the specified point to this point.
	 * @param p The point at the start of the vector.
	 * @return The vector from p to this point.
	 */
	public Vector3 vectorFrom(Point3 p) {
		return new Vector3(x - p.x, y - p.y, z - p.z);
	}

	/**
	 * Returns this point translated according to the specified vector.
	 * @param v The vector to translate this point by.
	 * @return The value of this point translated by v.
	 */
	public Point3 plus(Vector3 v) {
		return new Point3(x + v.x(), y + v.y(), z + v.z());
	}

	/**
	 * Returns this point translated in the opposite direction of the
	 * specified vector.
	 * @param v The opposite of the vector to translate by.
	 * @return The value of this point translated by -v.
	 */
	public Point3 minus(Vector3 v) {
		return new Point3(x - v.x(), y - v.y(), z - v.z());
	}

	/**
	 * Interpolates between two <code>Point3</code>s.
	 * @param p The <code>Point3</code> at <code>t = 0</code>.
	 * @param q The <code>Point3</code> at <code>t = 1</code>.
	 * @param t The value at which to interpolate.
	 * @return The <code>Point3</code> that is the fraction <code>t</code> of
	 * 		the way from <code>p</code> to <code>q</code>:
	 * 		<code>p + t(q - p)</code>.
	 */
	public static Point3 interpolate(Point3 p, Point3 q, double t) {
		return new Point3(
				MathUtil.interpolate(p.x, q.x, t),
				MathUtil.interpolate(p.y, q.y, t),
				MathUtil.interpolate(p.z, q.z, t)
		);
	}

	/**
	 * Finds the midpoint of the line segment joining two <code>Point3</code>s.
	 * Equivalent to <code>Point3.interpolate(p, q, 0.5)</code>.
	 * @param p The first <code>Point3</code>.
	 * @param q The second <code>Point3</code>.
	 * @return The <code>Point3</code> half-way between <code>p</code> and
	 * 		<code>q</code>.
	 * @see #interpolate(Point3, Point3, double)
	 */
	public static Point3 midpoint(Point3 p, Point3 q) {
		return Point3.interpolate(p, q, 0.5);
	}

	/**
	 * The origin of three dimensional space.
	 */
	public static final Point3 ORIGIN = new Point3(0.0, 0.0, 0.0);

	/**
	 * The distances from the origin along each axis.
	 */
	private final double x, y, z;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 15329079129798899L;

}
