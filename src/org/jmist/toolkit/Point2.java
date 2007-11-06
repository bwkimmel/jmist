/**
 *
 */
package org.jmist.toolkit;

import java.io.Serializable;

import org.jmist.util.MathUtil;

/**
 * A location in two dimensional space.
 * This class is immutable.
 * @author brad
 */
public final class Point2 implements Serializable {

	/**
	 * Initializes the components for the point.
	 * @param x The distance from the origin along the x axis.
	 * @param y The distance from the origin along the y axis.
	 */
	public Point2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the distance from the origin along the x-axis.
	 * Equivalent to {@code this.minus(Point2.ORIGIN).dot(Vector2.I);}
	 * @return The distance from the origin along the x-axis.
	 * @see {@link #getX()}, {@link Vector2#I},
	 * 		{@link Vector2#dot(Vector2)}.
	 */
	public double x() {
		return x;
	}

	/**
	 * Gets the distance from the origin along the y-axis.
	 * Equivalent to {@code this.minus(Point2.ORIGIN).dot(Vector2.J);}
	 * @return The distance from the origin along the y-axis.
	 * @see {@link #getY()}, {@link Vector2#J},
	 * 		{@link Vector2#dot(Vector2)}.
	 */
	public double y() {
		return y;
	}

	/**
	 * Computes the square of the distance from this point to the
	 * specified point.
	 * @param p The point to compute the square of the distance to.
	 * @return The square of the distance between this point and
	 * the specified point.
	 */
	public double squaredDistanceTo(Point2 p) {
		return ((x - p.x) * (x - p.x)) + ((y - p.y) * (y - p.y));
	}

	/**
	 * Computes the distance between this point and the specified point.
	 * @param p The point to compute the distance to.
	 * @return The distance between this point and p.
	 */
	public double distanceTo(Point2 p) {
		return Math.sqrt(squaredDistanceTo(p));
	}

	/**
	 * Computes the vector from this point to the specified point.
	 * @param p The point at the end of the vector.
	 * @return The vector from this point to p.
	 */
	public Vector2 vectorTo(Point2 p) {
		return new Vector2(p.x - x, p.y - y);
	}

	/**
	 * Computes the vector from the specified point to this point.
	 * @param p The point at the start of the vector.
	 * @return The vector from p to this point.
	 */
	public Vector2 vectorFrom(Point2 p) {
		return new Vector2(x - p.x, y - p.y);
	}

	/**
	 * Returns this point translated according to the specified vector.
	 * @param v The vector to translate this point by.
	 * @return The value of this point translated by v.
	 */
	public Point2 plus(Vector2 v) {
		return new Point2(x + v.x(), y + v.y());
	}

	/**
	 * Returns this point translated in the opposite direction of the
	 * specified vector.
	 * @param v The opposite of the vector to translate by.
	 * @return The value of this point translated by -v.
	 */
	public Point2 minus(Vector2 v) {
		return new Point2(x - v.x(), y - v.y());
	}

	/**
	 * Interpolates between two <code>Point2</code>s.
	 * @param p The <code>Point2</code> at <code>t = 0</code>.
	 * @param q The <code>Point2</code> at <code>t = 1</code>.
	 * @param t The value at which to interpolate.
	 * @return The <code>Point2</code> that is the fraction <code>t</code> of
	 * 		the way from <code>p</code> to <code>q</code>:
	 * 		<code>p + t(q - p)</code>.
	 */
	public static Point2 interpolate(Point2 p, Point2 q, double t) {
		return new Point2(
				MathUtil.interpolate(p.x, q.x, t),
				MathUtil.interpolate(p.y, q.y, t)
		);
	}

	/**
	 * Performs a bilinear interpolation between four <code>Point2</code>s.
	 * @param _00 The <code>Point2</code> at <code>(t, u) = (0, 0)</code>.
	 * @param _10 The <code>Point2</code> at <code>(t, u) = (1, 0)</code>.
	 * @param _01 The <code>Point2</code> at <code>(t, u) = (0, 1)</code>.
	 * @param _11 The <code>Point2</code> at <code>(t, u) = (1, 1)</code>.
	 * @param t The first value at which to interpolate.
	 * @param u The second value at which to interpolate.
	 * @return The interpolated <code>Point2</code> at <code>(t, u)</code>.
	 */
	public static Point2 bilinearInterpolate(Point2 _00, Point2 _10,
			Point2 _01, Point2 _11, double t, double u) {

		return new Point2(
				MathUtil.bilinearInterpolate(_00.x, _10.x, _01.x, _11.x, t, u),
				MathUtil.bilinearInterpolate(_00.y, _10.y, _01.y, _11.y, t, u)
		);

	}

	/**
	 * Performs a trilinear interpolation between eight <code>Point2</code>s.
	 * @param _000 The <code>Point2</code> at <code>(t, u, v) = (0, 0, 0)</code>.
	 * @param _100 The <code>Point2</code> at <code>(t, u, v) = (1, 0, 0)</code>.
	 * @param _010 The <code>Point2</code> at <code>(t, u, v) = (0, 1, 0)</code>.
	 * @param _110 The <code>Point2</code> at <code>(t, u, v) = (1, 1, 0)</code>.
	 * @param _001 The <code>Point2</code> at <code>(t, u, v) = (0, 0, 1)</code>.
	 * @param _101 The <code>Point2</code> at <code>(t, u, v) = (1, 0, 1)</code>.
	 * @param _011 The <code>Point2</code> at <code>(t, u, v) = (0, 1, 1)</code>.
	 * @param _111 The <code>Point2</code> at <code>(t, u, v) = (1, 1, 1)</code>.
	 * @param t The first value at which to interpolate.
	 * @param u The second value at which to interpolate.
	 * @param v The third value at which to interpolate.
	 * @return The interpolated <code>Point2</code> at <code>(t, u, v)</code>.
	 */
	public static Point2 trilinearInterpolate(Point2 _000, Point2 _100,
			Point2 _010, Point2 _110, Point2 _001, Point2 _101, Point2 _011,
			Point2 _111, double t, double u, double v) {

		return new Point2(
				MathUtil.trilinearInterpolate(_000.x, _100.x, _010.x, _110.x, _001.x, _101.x, _011.x, _111.x, t, u, v),
				MathUtil.trilinearInterpolate(_000.y, _100.y, _010.y, _110.y, _001.y, _101.y, _011.y, _111.y, t, u, v)
		);

	}

	/**
	 * Finds the midpoint of the line segment joining two <code>Point2</code>s.
	 * Equivalent to <code>Point2.interpolate(p, q, 0.5)</code>.
	 * @param p The first <code>Point2</code>.
	 * @param q The second <code>Point2</code>.
	 * @return The <code>Point2</code> half-way between <code>p</code> and
	 * 		<code>q</code>.
	 * @see #interpolate(Point2, Point2, double)
	 */
	public static Point2 midpoint(Point2 p, Point2 q) {
		return Point2.interpolate(p, q, 0.5);
	}

	/**
	 * Computes the centroid of a triangle defined by three <code>Point2</code>s.
	 * @param a The <code>Point2</code> at the first corner of the triangle.
	 * @param b The <code>Point2</code> at the second corner of the triangle.
	 * @param c The <code>Point2</code> at the third corner of the triangle.
	 * @return The <code>Point2</code> at the centroid of the triangle.
	 */
	public static Point2 centroid(Point2 a, Point2 b, Point2 c) {
		return new Point2(
				(a.x + b.x + c.x) / 3.0,
				(a.y + b.y + c.y) / 3.0
		);
	}

	/**
	 * The origin of three dimensional space.
	 */
	public static final Point2 ORIGIN = new Point2(0.0, 0.0);

	/**
	 * The distances from the origin along each axis.
	 */
	private final double x, y;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 5124476444817020178L;

}
