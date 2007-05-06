/**
 *
 */
package framework.math;

/**
 * A circle in two dimensional space (the set of points at most a
 * constant distance from a fixed point).
 * @author brad
 */
public final class Circle {

	/**
	 * Default constructor.
	 */
	public Circle() {
	}

	/**
	 * Initializes the center and radius of the circle.
	 * @param center The center of the circle.
	 * @param radius The radius of the circle (must be non-negative).
	 */
	public Circle(Point2 center, double radius) {
		assert(radius >= 0.0);

		this.center = center;
		this.radius = radius;
	}

	/**
	 * Gets the center of the circle.
	 * @return The center of the circle.
	 */
	public Point2 getCenter() {
		return center;
	}

	/**
	 * Sets the center of the circle.
	 * @param center The new center of the circle.
	 */
	public void setCenter(Point2 center) {
		this.center = center;
	}

	/**
	 * Gets the radius of the circle.
	 * @return The radius of the circle.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of the circle.
	 * @param radius The new radius of the circle (must be non-negative).
	 */
	public void setRadius(double radius) {
		assert(radius >= 0.0);

		this.radius = radius;
	}

	/**
	 * Determines if this circle is empty.
	 * @return A value indicating if this circle is empty.
	 */
	public boolean isEmpty() {
		return Double.isNaN(radius);
	}

	/**
	 * Determines if this circle contains the specified point.
	 * @param p The point to check for containment of.
	 * @return A value indicating if p is within this circle.
	 */
	public boolean contains(Point2 p) {
		return center.distanceTo(p) <= radius;
	}

	/**
	 * Expands this circle outwards by the specified amount.
	 * @param amount The amount to expand the circle by.
	 */
	public void expand(double amount) {
		radius += amount;

		// If the circle was contracted by more than its radius,
		// then it will be empty.
		if (radius < 0.0) {
			makeEmpty();
		}
	}

	/**
	 * Expands this circle outwards to encompass the specified point.
	 * Guarantees that {@code this.contains(p)} after this method is called.
	 * @param p The point to include in this circle.
	 */
	public void expandTo(Point2 p) {
		if (isEmpty()) {
			center = p;
			radius = 0.0;
		} else {
			radius = Math.max(radius, center.distanceTo(p));
		}
	}

	/**
	 * Makes this circle empty.
	 */
	private void makeEmpty() {
		center = Point2.ORIGIN;
		radius = Double.NaN;
	}

	/**
	 * Creates an empty circle.
	 * @return An empty circle.
	 */
	private static Circle getEmptyCircle() {
		Circle circle = new Circle();
		circle.makeEmpty();
		return circle;
	}

	/**
	 * A circle containing all points.
	 */
	public static final Circle UNIVERSE = new Circle(Point2.ORIGIN, Double.POSITIVE_INFINITY);

	/**
	 * The unit circle (the circle centered at the origin with a radius of 1.0).
	 */
	public static final Circle UNIT = new Circle(Point2.ORIGIN, 1.0);

	/**
	 * An empty circle.
	 */
	public static final Circle EMPTY = getEmptyCircle();

	/** The center of the circle. */
	private Point2 center;

	/** The radius of the circle. */
	private double radius;

}
