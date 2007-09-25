/**
 *
 */
package org.jmist.toolkit;

/**
 * The difference between two points in two dimensional space.
 * This class is immutable.
 * @author brad
 */
public final class Vector2 {

	/**
	 * Initializes the components for the vector.
	 * @param x The length of the vector along the x axis.
	 * @param y The length of the vector along the y axis.
	 */
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the distance from the origin along the x-axis.
	 * Equivalent to {@code this.dot(Vector2.I);}
	 * @return The distance from the origin along the x-axis.
	 * @see {@link #I}, {@link #dot(Vector2)}.
	 */
	public double x() {
		return x;
	}

	/**
	 * Gets the distance from the origin along the y-axis.
	 * Equivalent to {@code this.dot(Vector2.J);}
	 * @return The distance from the origin along the y-axis.
	 * @see {@link #J}, {@link #dot(Vector2)}.
	 */
	public double y() {
		return y;
	}

	/**
	 * Computes the magnitude of the vector.
	 * @return The magnitude of the vector.
	 */
	public double length() {
		return Math.sqrt(dot(this));
	}

	/**
	 * Returns the opposite of this vector.
	 * @return The opposite of this vector.
	 */
	public Vector2 opposite() {
		return new Vector2(-x, -y);
	}

	/**
	 * Computes the sum of two vectors.
	 * @param v The vector to add to this vector.
	 * @return The sum of this vector and v.
	 */
	public Vector2 plus(Vector2 v) {
		return new Vector2(x + v.x, y + v.y);
	}

	/**
	 * Computes the difference between two vectors.
	 * @param v The vector to subtract from this vector.
	 * @return The difference between this vector and v.
	 */
	public Vector2 minus(Vector2 v) {
		return new Vector2(x - v.x, y - v.y);
	}

	/**
	 * Computes this vector scaled by a constant factor.
	 * @param c The factor to scale this vector by.
	 * @return This vector scaled by c.
	 */
	public Vector2 times(double c) {
		return new Vector2(c * x, c * y);
	}

	/**
	 * Computes this vector scaled by the reciprocal of
	 * a constant factor.
	 * Equivalent to {@code this.times(1.0 / c).}
	 * @param c The factor to divide this vector by.
	 * @return The vector scaled by 1.0 / c.
	 * @see {@link #times(double)}.
	 */
	public Vector2 divide(double c) {
		return new Vector2(x / c, y / c);
	}

	/**
	 * Computes the dot product between two vectors.
	 * @param v The vector to compute the dot product of with this vector.
	 * @return The dot product of this vector and v.
	 */
	public double dot(Vector2 v) {
		return (x * v.x) + (y * v.y);
	}

	/**
	 * Computes the unit vector in the same direction as this vector.
	 * @return The unit vector in the same direction as this vector.
	 */
	public Vector2 unit() {
		return this.times(1.0 / length());
	}

	/**
	 * Returns a new unit vector in the same direction as
	 * the vector with the specified components.
	 * Equivalent to {@code new Vector2(x, y).unit()}.
	 * @param x The magnitude of the vector along the x-axis.
	 * @param y The magnitude of the vector along the y-axis.
	 * @return A unit vector in the same direction as the
	 * 		vector with the indicated components.
	 * @see {@link #unit()}.
	 */
	public static Vector2 unit(double x, double y) {
		double r = Math.sqrt(x * x + y * y);
		return new Vector2(x / r, y / r);
	}

	/**
	 * The zero vector (represents the vector between two identical points).
	 */
	public static final Vector2 ZERO = new Vector2(0.0, 0.0);

	/**
	 * The unit vector along the x-axis.
	 */
	public static final Vector2 I = new Vector2(1.0, 0.0);

	/**
	 * The unit vector along the y-axis.
	 */
	public static final Vector2 J = new Vector2(0.0, 1.0);

	/**
	 * The lengths of the vector along each axis.
	 */
	private final double x, y;

}
