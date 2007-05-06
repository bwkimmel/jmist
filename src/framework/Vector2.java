/**
 *
 */
package framework;

/**
 * The difference between two points in two dimensional space.
 * @author brad
 *
 */
public final class Vector2 {

	/**
	 * The lengths of the vector along each axis.
	 */
	public double x, y;

	/**
	 * Default constructor.
	 *
	 */
	public Vector2() {
	}

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
	 * Negates this vector.
	 * Equivalent to {@code this = this.opposite();}
	 * @see opposite
	 */
	public void negate() {
		x = -x;
		y = -y;
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
	 * Adds another vector to this vector.
	 * Equivalent to {@code this = this.plus(v);}
	 * @param v
	 * @see plus
	 */
	public void add(Vector2 v) {
		x += v.x;
		y += v.y;
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
	 * Subtracts another vector from this vector.
	 * Equivalent to {@code this = this.minus(v);}
	 * @param v The vector to subtract from this vector.
	 * @see minus
	 */
	public void subtract(Vector2 v) {
		x -= v.x;
		y -= v.y;
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
	 * Scales this vector by a constant factor.
	 * Equivalent to {@code this = this.times(c);}
	 * @param c The factor to scale this vector by.
	 * @see times
	 */
	public void scale(double c) {
		x *= c;
		y *= c;
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
	 * Normalizes this vector (i.e., preserves its direction and sets its length to 1).
	 * Equivalent to {@code this = this.unit();}
	 * @see unit
	 */
	public void normalize() {
		scale(1.0 / length());
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

}
