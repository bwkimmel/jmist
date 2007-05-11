/**
 *
 */
package framework.core;

/**
 * An axis-aligned two dimensional box.
 * This class is immutable.
 * @author brad
 */
public final class Box2 {

	/**
	 * Initializes the extents of the box along the x and y axes.
	 * @param spanX The extent of the box along the x-axis.
	 * @param spanY The extent of the box along the y-axis.
	 */
	public Box2(Interval spanX, Interval spanY) {
		if (spanX.isEmpty() || spanY.isEmpty()) {
			minimumX = minimumY = maximumX = maximumY = Double.NaN;
		} else {
			minimumX = spanX.getMinimum();
			maximumX = spanX.getMaximum();
			minimumY = spanY.getMinimum();
			maximumY = spanY.getMaximum();
		}
	}

	/**
	 * Initializes the box between two points.
	 * @param p One corner of the box.
	 * @param q The corner of the box opposite p.
	 */
	public Box2(Point2 p, Point2 q) {
		minimumX = Math.min(p.x(), q.x());
		maximumX = Math.max(p.x(), q.x());
		minimumY = Math.min(p.y(), q.y());
		maximumY = Math.max(p.y(), q.y());
	}

	/**
	 * Initializes the box from its extents.
	 * @param minimumX The lower bound of the box along the x-axis.
	 * @param minimumY The lower bound of the box along the y-axis.
	 * @param maximumX The upper bound of the box along the x-axis.
	 * @param maximumY The upper bound of the box along the y-axis.
	 */
	public Box2(double minimumX, double minimumY, double maximumX, double maximumY) {
		assert(minimumX <= maximumX && minimumY <= maximumY);

		this.minimumX = minimumX;
		this.minimumY = minimumY;
		this.maximumX = maximumX;
		this.maximumY = maximumY;
	}

	/**
	 * Gets the lower bound of this box along the x-axis.
	 * @return The lower bound of this box along the x-axis.
	 */
	public double getMinimumX() {
		return minimumX;
	}

	/**
	 * Gets the lower bound of this box along the y-axis.
	 * @return The lower bound of this box along the y-axis.
	 */
	public double getMinimumY() {
		return minimumY;
	}

	/**
	 * Gets the upper bound of this box along the x-axis.
	 * @return The upper bound of this box along the x-axis.
	 */
	public double getMaximumX() {
		return maximumX;
	}

	/**
	 * Gets the upper bound of this box along the y-axis.
	 * @return The upper bound of this box along the y-axis.
	 */
	public double getMaximumY() {
		return maximumY;
	}

	/**
	 * Gets the extent of this box along the x-axis.
	 * @return An interval representing the extent of this box along the x-axis.
	 */
	public Interval getSpanX() {
		return isEmpty() ? Interval.EMPTY : new Interval(minimumX, maximumX);
	}

	/**
	 * Gets the extent of this box along the y-axis.
	 * @return An interval representing the extent of this box along the y-axis.
	 */
	public Interval getSpanY() {
		return isEmpty() ? Interval.EMPTY : new Interval(minimumY, maximumY);
	}

	/**
	 * Determines if this box is empty.
	 * @return A value indicating wither this box is empty.
	 */
	public boolean isEmpty() {
		return Double.isNaN(minimumX);
	}

	/**
	 * Determines if this box contains the specified point.
	 * @param p The point to check for containment of.
	 * @return A value indicating whether p is inside this box.
	 */
	public boolean contains(Point2 p) {
		return (minimumX <= p.x() && p.x() <= maximumX) && (minimumY <= p.y() && p.y() <= maximumY);
	}

	/**
	 * Gets the area of the box.
	 * @return The area of the box.
	 */
	public double getArea() {
		return (maximumX - minimumX) * (maximumY - minimumY);
	}

	/**
	 * Gets the length of the perimeter of the box.
	 * @return The perimeter of the box.
	 */
	public double getPerimeter() {
		return 2.0 * ((maximumX - minimumX) + (maximumY - minimumY));
	}

	/**
	 * Computes the intersection of this box with another.
	 * @param other The box to intersect with this box.
	 * @return The intersection of this box with the other box.
	 */
	public Box2 intersect(Box2 other) {
		return getInstance(
				Math.max(minimumX, other.minimumX),
				Math.max(minimumY, other.minimumY),
				Math.min(maximumX, other.maximumX),
				Math.min(maximumY, other.maximumY)
		);
	}

	/**
	 * Extends the box to contain the specified point.
	 * Guarantees that {@code this.contains(p)} after this method is called.
	 * @param p The point to extend the box to.
	 * @return The extended box.
	 * @see contains
	 */
	public Box2 extendTo(Point2 p) {
		if (isEmpty()) {
			return new Box2(p.x(), p.y(), p.x(), p.y());
		} else {
			return new Box2(
					Math.min(minimumX, p.x()),
					Math.min(minimumY, p.y()),
					Math.max(maximumX, p.x()),
					Math.max(maximumY, p.y())
			);
		}
	}

	/**
	 * Expands the box by the specified amount in all directions.
	 * @param amount The amount to expand the box by.
	 * @return The expanded box.
	 */
	public Box2 expand(double amount) {
		return getInstance(minimumX - amount, minimumY - amount, maximumX + amount, maximumY + amount);
	}

	/**
	 * Default constructor.
	 */
	private Box2() {
		minimumX = maximumX = minimumY = maximumY = Double.NaN;
	}

	/**
	 * Gets an instance of a two dimensional box.
	 * @param minimumX The minimum extent of the box along the x-axis.
	 * @param minimumY The minimum extent of the box along the y-axis.
	 * @param maximumX The maximum extent of the box along the x-axis.
	 * @param maximumY The maximum extent of the box along the y-axis.
	 * @return A new box if minimumX <= maximumX and minimumY <= maximumY,
	 *         Box2.EMPTY otherwise.
	 */
	private static final Box2 getInstance(double minimumX, double minimumY, double maximumX, double maximumY) {
		if (minimumX > maximumY || minimumY > maximumY) {
			return Box2.EMPTY;
		} else {
			return new Box2(minimumX, minimumY, maximumX, maximumY);
		}
	}

	/**
	 * The box containing all of two dimensional space.
	 * {@code Box2.UNIVERSE.contains(p)} will be true for all p.
	 */
	public static final Box2 UNIVERSE = new Box2(Interval.UNIVERSE, Interval.UNIVERSE);

	/**
	 * The unit box: [0, 1]^2.
	 */
	public static final Box2 UNIT = new Box2(0.0, 0.0, 1.0, 1.0);

	/**
	 * The empty box.
	 * {@code Box2.EMPTY.contains(p)} will be false for all p.
	 */
	public static final Box2 EMPTY = new Box2();

	/** The lower bound along the x-axis. */
	private double minimumX;

	/** The upper bound along the x-axis. */
	private double maximumX;

	/** The lower bound along the y-axis. */
	private double minimumY;

	/** The upper bound along the y-axis. */
	private double maximumY;

}
