/**
 *
 */
package framework;

/**
 * An axis-aligned two dimensional box.
 * @author brad
 */
public final class Box2 {

	/**
	 * Default constructor.
	 */
	public Box2() {
	}

	/**
	 * Initializes the extents of the box along the x and y axes.
	 * @param spanX The extent of the box along the x-axis.
	 * @param spanY The extent of the box along the y-axis.
	 */
	public Box2(Interval spanX, Interval spanY) {
		if (spanX.isEmpty() || spanY.isEmpty()) {
			makeEmpty();
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
		minimumX = Math.min(p.x, q.x);
		maximumX = Math.max(p.x, q.x);
		minimumY = Math.min(p.y, q.y);
		maximumY = Math.max(p.y, q.y);
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
		return (minimumX <= p.x && p.x <= maximumX) && (minimumY <= p.y && p.y <= maximumY);
	}

	/**
	 * Computes the intersection of this box with another.
	 * @param other The box to intersect with this box.
	 * @return The intersection of this box with the other box.
	 */
	public Box2 intersection(Box2 other) {
		return new Box2(
				Math.max(minimumX, other.minimumX),
				Math.max(minimumY, other.minimumY),
				Math.min(maximumX, other.maximumX),
				Math.min(maximumY, other.maximumY)
				);
	}

	/**
	 * Intersects this box with another.
	 * @param other The box to intersect this box with.
	 */
	public void intersect(Box2 other) {
		if (other.isEmpty()) {
			makeEmpty();
			return;
		}

		if (other.minimumX > minimumX) {
			minimumX = other.minimumX;
		}

		if (other.minimumY > minimumY) {
			minimumY = other.minimumY;
		}

		if (other.maximumX < maximumX) {
			maximumX = other.maximumX;
		}

		if (other.maximumY < maximumY) {
			maximumY = other.maximumY;
		}

		if (minimumX > maximumX || minimumY > maximumY) {
			makeEmpty();
		}
	}

	/**
	 * Extends the box to contain the specified point.
	 * Guarantees that {@code this.contains(p)} after this method is called.
	 * @param p The point to extend the box to.
	 * @see contains
	 */
	public void extendTo(Point2 p) {
		if (isEmpty()) {
			minimumX = maximumX = p.x;
			minimumY = maximumY = p.y;
		} else {

			// expand along the x-axis if necessary.
			if (p.x < minimumX) {
				minimumX = p.x;
			} else if (p.x > maximumX) {
				maximumX = p.x;
			}

			// expand along the y-axis if necessary.
			if (p.y < minimumY) {
				minimumY = p.y;
			} else if (p.y > maximumY) {
				maximumY = p.y;
			}

		}
	}

	/**
	 * Expands the box by the specified amount in all directions.
	 * @param amount The amount to expand the box by.
	 */
	public void expand(double amount) {
		minimumX -= amount;
		minimumY -= amount;
		maximumX += amount;
		maximumY += amount;

		// If the box is contracted (amount < 0) by more than half the span
		// in the x or y direction, then the box will be empty.
		if (minimumX > maximumX || minimumY > maximumY) {
			makeEmpty();
		}
	}

	/**
	 * Makes this box empty.
	 */
	private void makeEmpty() {
		minimumX = maximumX = minimumY = maximumY = Double.NaN;
	}

	/**
	 * The box containing all of two dimensional space.
	 * {@code Box2.UNIVERSE.contains(p)} will be true for all p.
	 */
	public static final Box2 UNIVERSE = new Box2(Interval.UNIVERSE, Interval.UNIVERSE);

	/**
	 * The empty box.
	 * {@code Box2.EMPTY.contains(p)} will be false for all p.
	 */
	public static final Box2 EMPTY = new Box2(Interval.EMPTY, Interval.EMPTY);

	/** The lower bound along the x-axis. */
	private double minimumX;

	/** The upper bound along the x-axis. */
	private double maximumX;

	/** The lower bound along the y-axis. */
	private double minimumY;

	/** The upper bound along the y-axis. */
	private double maximumY;

}
