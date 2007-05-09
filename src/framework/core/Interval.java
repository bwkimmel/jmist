/**
 *
 */
package framework.core;

/**
 * An closed interval [a, b] on the real number line.
 * This class is immutable.
 * @author brad
 */
public final class Interval {

	/**
	 * Initializes the endpoints of the interval.
	 * Requires {@code minimum <= maximum}
	 * @param minimum The lower bound of the interval.
	 * @param maximum The upper bound of the interval.
	 */
	public Interval(double minimum, double maximum) {
		assert(minimum <= maximum);

		this.minimum = minimum;
		this.maximum = maximum;
	}

	/**
	 * Gets the lower bound of this interval.
	 * @return The lower bound of this interval.
	 */
	public double getMinimum() {
		return minimum;
	}

	/**
	 * Gets the upper bound of this interval.
	 * @return The upper bound of this interval.
	 */
	public double getMaximum() {
		return maximum;
	}

	/**
	 * Determines if the interval contains a particular value.
	 * @param t The value to check for containment.
	 * @return True if {@code this.getMinimum() <= t <= this.getMaximum()}, false otherwise.
	 */
	public boolean contains(double t) {
		return minimum <= t && t <= maximum;
	}

	/**
	 * Determines if this interval is the empty interval.
	 * @return A value indicating if this interval is empty.
	 */
	public boolean isEmpty() {
		return Double.isNaN(minimum);
	}

	/**
	 * Extends this interval to include the specified value.
	 * Guarantees that {@code this.contains(t)} after this method is called.
	 * @param t The value to include in this interval.
	 * @see contains
	 */
	public Interval extendTo(double t) {
		if (isEmpty()) {
			return new Interval(t, t);
		} else if (t < minimum) {
			return new Interval(t, maximum);
		} else if (t > maximum) {
			return new Interval(minimum, maximum);
		}

		return this;
	}

	/**
	 * Expands this interval by the specified amount.
	 * @param amount The amount to expand this interval by.
	 * @returns The expanded interval.
	 */
	public Interval expand(double amount) {
		double newMinimum = minimum - amount;
		double newMaximum = maximum + amount;

		if (newMinimum > newMaximum) {
			return Interval.EMPTY;
		} else {
			return new Interval(newMinimum, newMaximum);
		}
	}

	/**
	 * Computes the intersection of this interval with another.
	 * @param I The interval to intersect with this one.
	 * @return The intersection of this interval with I.
	 */
	public Interval intersect(Interval I) {
		return new Interval(Math.max(minimum, I.minimum), Math.min(maximum, I.maximum));
	}

	/**
	 * Default constructor.
	 */
	private Interval() {
		minimum = maximum = Double.NaN;
	}

	/**
	 * The entire real line (-infinity, infinity).
	 * {@code Interval.UNIVERSE.contains(t)} will return true for all t.
	 */
	public static final Interval UNIVERSE = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

	/**
	 * The empty set.
	 * {@code Interval.EMPTY.contains(t)} will return false for all t.
	 */
	public static final Interval EMPTY = new Interval();

	/** The lower bound of this interval. */
	private final double minimum;

	/** The upper bound of this interval. */
	private final double maximum;

}
