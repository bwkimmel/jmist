/**
 *
 */
package framework;

/**
 * A location in two dimensional space.
 * @author brad
 */
public final class Point2 {

	/**
	 * The distances from the origin along each axis.
	 */
	public double x, y;

	/**
	 * Default constructor.
	 *
	 */
	public Point2() {
	}

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
		return new Point2(x + v.x, y + v.y);
	}

	/**
	 * Translates this vector along the specified vector.
	 * Equivalent to {@code this = this.plus(v);}
	 * @param v The vector to translate this point along.
	 * @see plus
	 */
	public void add(Vector2 v) {
		x += v.x;
		y += v.y;
	}

	/**
	 * Returns this point translated in the opposite direction of the
	 * specified vector.
	 * @param v The opposite of the vector to translate by.
	 * @return The value of this point translated by -v.
	 */
	public Point2 minus(Vector2 v) {
		return new Point2(x - v.x, y - v.y);
	}

	/**
	 * Translates this point along the opposite of the specified vector.
	 * Equivalent to {@code this = this.minus(v);}
	 * @param v The opposite of the vector to translate this point along.
	 * @see minus
	 */
	public void subtract(Vector2 v) {
		x -= v.x;
		y -= v.y;
	}

	/**
	 * The origin of three dimensional space.
	 */
	public static final Point2 ORIGIN = new Point2(0.0, 0.0);

}
