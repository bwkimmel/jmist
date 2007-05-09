/**
 *
 */
package framework.core;

/**
 * A circle in two dimensional space (the set of points at most a
 * constant distance from a fixed point).
 * This class is immutable.
 * @author brad
 */
public final class Circle {

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
	 * Gets the radius of the circle.
	 * @return The radius of the circle.
	 */
	public double getRadius() {
		return radius;
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
	 * Computes the area of the circle.
	 * @return The area of the circle.
	 */
	public double getArea() {
		return Math.PI * radius * radius;
	}

	/**
	 * Computes the diameter of the circle.
	 * @return The diameter of the circle.
	 */
	public double getDiameter() {
		return 2.0 * radius;
	}

	/**
	 * Computes the circumference of the circle.
	 * @return The circumference of the circle.
	 */
	public double getCircumference() {
		return 2.0 * Math.PI * radius;
	}

	/**
	 * Expands this circle outwards by the specified amount.
	 * @param amount The amount to expand the circle by.
	 * @return The expanded circle.
	 */
	public Circle expand(double amount) {
		return getInstance(center, radius + amount);
	}

	/**
	 * Expands this circle outwards to encompass the specified point.
	 * Guarantees that {@code this.contains(p)} after this method is called.
	 * @param p The point to include in this circle.
	 * @return The expanded circle.
	 */
	public Circle expandTo(Point2 p) {
		if (isEmpty()) {
			return new Circle(p, 0.0);
		} else {
			double newRadius = center.distanceTo(p);

			if (newRadius < radius) {
				return this;
			} else {
				return new Circle(center, newRadius);
			}
		}
	}

	/**
	 * Default constructor.
	 */
	private Circle() {
		center = Point2.ORIGIN;
		radius = Double.NaN;
	}

	/**
	 * Gets an instance of a circle.
	 * @param center The center of the circle.
	 * @param radius The radius of the circle.
	 * @return A new circle if 0 <= radius < infinity,
	 *         Circle.UNIVERSE if radius == infinity,
	 *         Circle.EMPTY if radius < 0.
	 */
	private static final Circle getInstance(Point2 center, double radius) {
		if (radius < 0.0) {
			return Circle.EMPTY;
		} else if (Double.isInfinite(radius)) {
			return Circle.UNIVERSE;
		} else {
			return new Circle(center, radius);
		}
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
	public static final Circle EMPTY = new Circle();

	/** The center of the circle. */
	private final Point2 center;

	/** The radius of the circle. */
	private final double radius;

}
