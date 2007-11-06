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
