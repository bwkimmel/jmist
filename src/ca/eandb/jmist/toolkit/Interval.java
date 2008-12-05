/**
 *
 */
package ca.eandb.jmist.toolkit;

import java.io.Serializable;

/**
 * An closed interval [a, b] on the real number line.
 * This class is immutable.
 * @author brad
 */
public final class Interval implements Serializable {

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
	 * Gets the interval between the specified values.
	 * @param a The first value.
	 * @param b The second value.
	 * @return The interval [a, b], if a <= b. The interval [b, a], if
	 * 		b < a.
	 */
	public static Interval between(double a, double b) {
		return new Interval(Math.min(a, b), Math.max(a, b));
	}

	/**
	 * Gets the lower bound of this interval.
	 * @return The lower bound of this interval.
	 */
	public double minimum() {
		return minimum;
	}

	/**
	 * Gets the upper bound of this interval.
	 * @return The upper bound of this interval.
	 */
	public double maximum() {
		return maximum;
	}

	/**
	 * Gets the length of this interval (i.e., the distance
	 * between the endpoints).
	 * @return The length of the interval.
	 */
	public double length() {
		return maximum - minimum;
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
	 * @see {@link #contains(double)}.
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
	 * @return The expanded interval.
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
	 * Determines whether this interval intersects with another.
	 * Equivalent to {@code !this.intersect(I).isEmpty()}.
	 * @param I The interval with which to check for an intersection
	 * @return A value indicating whether the two intervals overlap.
	 * @see {@link #intersect(Interval)}, {@link #isEmpty()}.
	 */
	public boolean intersects(Interval I) {
		return Math.max(minimum, I.minimum) <= Math.min(maximum, I.maximum);
	}

	/**
	 * Interpolates between the endpoints of this interval.  If
	 * {@code t} is in [0, 1], then the result will fall within
	 * the interval, otherwise, the result will fall outside the
	 * interval.
	 * @param t The value at which to interpolate.
	 * @return The interpolated point.
	 */
	public double interpolate(double t) {
		return this.minimum + t * (this.maximum - this.minimum);
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
	 * The unit interval: [0, 1].
	 */
	public static final Interval UNIT = new Interval(0.0, 1.0);

	/**
	 * The empty set.
	 * {@code Interval.EMPTY.contains(t)} will return false for all t.
	 */
	public static final Interval EMPTY = new Interval();

	/**
	 * The interval containing the non-negative real numbers: [0, infinity).
	 */
	public static final Interval POSITIVE = new Interval(0.0, Double.POSITIVE_INFINITY);

	/**
	 * The interval containing the non-positive real numbers: (-infinity, 0].
	 */
	public static final Interval NEGATIVE = new Interval(Double.NEGATIVE_INFINITY, 0.0);

	/** The lower bound of this interval. */
	private final double minimum;

	/** The upper bound of this interval. */
	private final double maximum;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4034510279908046892L;

}
