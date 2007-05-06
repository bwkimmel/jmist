/**
 *
 */
package framework.core;

/**
 * An axis-aligned three dimensional box.
 * @author brad
 */
public final class Box3 {

	/**
	 * Default constructor.
	 */
	public Box3() {
	}

	/**
	 * Initializes the extents of the box along the x, y, and z axes.
	 * @param spanX The extent of the box along the x-axis.
	 * @param spanY The extent of the box along the y-axis.
	 * @param spanZ The extent of the box along the z-axis.
	 */
	public Box3(Interval spanX, Interval spanY, Interval spanZ) {
		if (spanX.isEmpty() || spanY.isEmpty() || spanZ.isEmpty()) {
			makeEmpty();
		} else {
			minimumX = spanX.getMinimum();
			maximumX = spanX.getMaximum();
			minimumY = spanY.getMinimum();
			maximumY = spanY.getMaximum();
			minimumZ = spanZ.getMinimum();
			maximumZ = spanZ.getMaximum();
		}
	}

	/**
	 * Initializes the box between two points.
	 * @param p One corner of the box.
	 * @param q The corner of the box opposite p.
	 */
	public Box3(Point3 p, Point3 q) {
		minimumX = Math.min(p.x, q.x);
		maximumX = Math.max(p.x, q.x);
		minimumY = Math.min(p.y, q.y);
		maximumY = Math.max(p.y, q.y);
		minimumZ = Math.min(p.z, q.z);
		maximumZ = Math.max(p.z, q.z);
	}

	/**
	 * Initializes the box from its extents.
	 * @param minimumX The lower bound of the box along the x-axis.
	 * @param minimumY The lower bound of the box along the y-axis.
	 * @param minimumZ The lower bound of the box along the z-axis.
	 * @param maximumX The upper bound of the box along the x-axis.
	 * @param maximumY The upper bound of the box along the y-axis.
	 * @param maximumZ The upper bound of the box along the z-axis.
	 */
	public Box3(double minimumX, double minimumY, double minimumZ, double maximumX, double maximumY, double maximumZ) {
		assert(minimumX <= maximumX && minimumY <= maximumY && minimumZ <= maximumZ);

		this.minimumX = minimumX;
		this.minimumY = minimumY;
		this.minimumZ = minimumZ;
		this.maximumX = maximumX;
		this.maximumY = maximumY;
		this.maximumZ = maximumZ;
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
	 * Gets the lower bound of this box along the z-axis.
	 * @return The lower bound of this box along the z-axis.
	 */
	public double getMinimumZ() {
		return minimumZ;
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
	 * Gets the upper bound of this box along the z-axis.
	 * @return The upper bound of this box along the z-axis.
	 */
	public double getMaximumZ() {
		return maximumZ;
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
	 * Gets the extent of this box along the z-axis.
	 * @return An interval representing the extent of this box along the z-axis.
	 */
	public Interval getSpanZ() {
		return isEmpty() ? Interval.EMPTY : new Interval(minimumZ, maximumZ);
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
	public boolean contains(Point3 p) {
		return (minimumX <= p.x && p.x <= maximumX) && (minimumY <= p.y && p.y <= maximumY) && (minimumZ <= p.z && p.z <= maximumZ);
	}

	/**
	 * Computes the intersection of this box with another.
	 * @param other The box to intersect with this box.
	 * @return The intersection of this box with the other box.
	 */
	public Box3 intersection(Box3 other) {
		return new Box3(
				Math.max(minimumX, other.minimumX),
				Math.max(minimumY, other.minimumY),
				Math.max(minimumZ, other.minimumZ),
				Math.min(maximumX, other.maximumX),
				Math.min(maximumY, other.maximumY),
				Math.min(maximumZ, other.maximumZ)
				);
	}

	/**
	 * Intersects this box with another.
	 * @param other The box to intersect this box with.
	 */
	public void intersect(Box3 other) {
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

		if (other.minimumZ > minimumZ) {
			minimumZ = other.minimumZ;
		}

		if (other.maximumX < maximumX) {
			maximumX = other.maximumX;
		}

		if (other.maximumY < maximumY) {
			maximumY = other.maximumY;
		}

		if (other.maximumZ < maximumZ) {
			maximumZ = other.maximumZ;
		}

		if (minimumX > maximumX || minimumY > maximumY || minimumZ > maximumZ) {
			makeEmpty();
		}
	}

	/**
	 * Extends the box to contain the specified point.
	 * Guarantees that {@code this.contains(p)} after this method is called.
	 * @param p The point to extend the box to.
	 * @see contains
	 */
	public void extendTo(Point3 p) {
		if (isEmpty()) {
			minimumX = maximumX = p.x;
			minimumY = maximumY = p.y;
			minimumZ = maximumZ = p.z;
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

			// expand along the z-axis if necessary.
			if (p.z < minimumZ) {
				minimumZ = p.z;
			} else if (p.z > maximumZ) {
				maximumZ = p.z;
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
		minimumZ -= amount;
		maximumX += amount;
		maximumY += amount;
		minimumZ += amount;

		// If the box is contracted (amount < 0) by more than half the span
		// in the x or y direction, then the box will be empty.
		if (minimumX > maximumX || minimumY > maximumY || minimumZ > maximumZ) {
			makeEmpty();
		}
	}

	/**
	 * Makes this box empty.
	 */
	private void makeEmpty() {
		minimumX = maximumX = minimumY = maximumY = minimumZ = maximumZ = Double.NaN;
	}

	/**
	 * The box containing all of two dimensional space.
	 * {@code Box3.UNIVERSE.contains(p)} will be true for all p.
	 */
	public static final Box3 UNIVERSE = new Box3(Interval.UNIVERSE, Interval.UNIVERSE, Interval.UNIVERSE);

	/**
	 * The empty box.
	 * {@code Box3.EMPTY.contains(p)} will be false for all p.
	 */
	public static final Box3 EMPTY = new Box3(Interval.EMPTY, Interval.EMPTY, Interval.EMPTY);

	/** The lower bound along the x-axis. */
	private double minimumX;

	/** The upper bound along the x-axis. */
	private double maximumX;

	/** The lower bound along the y-axis. */
	private double minimumY;

	/** The upper bound along the y-axis. */
	private double maximumY;

	/** The lower bound along the z-axis. */
	private double minimumZ;

	/** The upper bound along the z-axis. */
	private double maximumZ;

}
