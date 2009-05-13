/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;


/**
 * A location in three dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
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
	 * Computes the vector from {@link #ORIGIN}.
	 * @return The vector from {@link #ORIGIN} to this point.
	 * @see #ORIGIN
	 */
	public Vector3 vectorFromOrigin() {
		return new Vector3(x, y, z);
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
	 * Performs a bilinear interpolation between four <code>Point3</code>s.
	 * @param _00 The <code>Point3</code> at <code>(t, u) = (0, 0)</code>.
	 * @param _10 The <code>Point3</code> at <code>(t, u) = (1, 0)</code>.
	 * @param _01 The <code>Point3</code> at <code>(t, u) = (0, 1)</code>.
	 * @param _11 The <code>Point3</code> at <code>(t, u) = (1, 1)</code>.
	 * @param t The first value at which to interpolate.
	 * @param u The second value at which to interpolate.
	 * @return The interpolated <code>Point3</code> at <code>(t, u)</code>.
	 */
	public static Point3 bilinearInterpolate(Point3 _00, Point3 _10,
			Point3 _01, Point3 _11, double t, double u) {

		return new Point3(
				MathUtil.bilinearInterpolate(_00.x, _10.x, _01.x, _11.x, t, u),
				MathUtil.bilinearInterpolate(_00.y, _10.y, _01.y, _11.y, t, u),
				MathUtil.bilinearInterpolate(_00.z, _10.z, _01.z, _11.z, t, u)
		);

	}

	/**
	 * Performs a trilinear interpolation between eight <code>Point3</code>s.
	 * @param _000 The <code>Point3</code> at <code>(t, u, v) = (0, 0, 0)</code>.
	 * @param _100 The <code>Point3</code> at <code>(t, u, v) = (1, 0, 0)</code>.
	 * @param _010 The <code>Point3</code> at <code>(t, u, v) = (0, 1, 0)</code>.
	 * @param _110 The <code>Point3</code> at <code>(t, u, v) = (1, 1, 0)</code>.
	 * @param _001 The <code>Point3</code> at <code>(t, u, v) = (0, 0, 1)</code>.
	 * @param _101 The <code>Point3</code> at <code>(t, u, v) = (1, 0, 1)</code>.
	 * @param _011 The <code>Point3</code> at <code>(t, u, v) = (0, 1, 1)</code>.
	 * @param _111 The <code>Point3</code> at <code>(t, u, v) = (1, 1, 1)</code>.
	 * @param t The first value at which to interpolate.
	 * @param u The second value at which to interpolate.
	 * @param v The third value at which to interpolate.
	 * @return The interpolated <code>Point3</code> at <code>(t, u, v)</code>.
	 */
	public static Point3 trilinearInterpolate(Point3 _000, Point3 _100,
			Point3 _010, Point3 _110, Point3 _001, Point3 _101, Point3 _011,
			Point3 _111, double t, double u, double v) {

		return new Point3(
				MathUtil.trilinearInterpolate(_000.x, _100.x, _010.x, _110.x, _001.x, _101.x, _011.x, _111.x, t, u, v),
				MathUtil.trilinearInterpolate(_000.y, _100.y, _010.y, _110.y, _001.y, _101.y, _011.y, _111.y, t, u, v),
				MathUtil.trilinearInterpolate(_000.z, _100.z, _010.z, _110.z, _001.z, _101.z, _011.z, _111.z, t, u, v)
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
	 * Computes the centroid of a triangle defined by three <code>Point3</code>s.
	 * @param a The <code>Point3</code> at the first corner of the triangle.
	 * @param b The <code>Point3</code> at the second corner of the triangle.
	 * @param c The <code>Point3</code> at the third corner of the triangle.
	 * @return The <code>Point3</code> at the centroid of the triangle.
	 */
	public static Point3 centroid(Point3 a, Point3 b, Point3 c) {
		return new Point3(
				(a.x + b.x + c.x) / 3.0,
				(a.y + b.y + c.y) / 3.0,
				(a.z + b.z + c.z) / 3.0
		);
	}

	/**
	 * Computes the centroid of several points.
	 * @param points The collection of <code>Point3</code>s for which to
	 * 		computes the centroid.
	 * @return The centroid of the given collection of points.
	 */
	public static Point3 centroid(Iterable<Point3> points) {

		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		int n = 0;

		for (Point3 p : points) {
			x += p.x;
			y += p.y;
			z += p.z;
			n++;
		}

		return new Point3(x / (double) n, y / (double) n, z / (double) n);

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
