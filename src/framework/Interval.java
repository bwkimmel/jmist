/**
 *
 */
package framework;

/**
 * An closed interval [a, b] on the real number line.
 * @author brad
 */
public final class Interval {

	/**
	 * Default constructor.
	 */
	public Interval() {
	}

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
	public void extendTo(double t) {
		if (isEmpty()) {
			minimum = maximum = t;
		} else if (t < minimum) {
			minimum = t;
		} else if (t > maximum) {
			maximum = t;
		}
	}

	/**
	 * Expands this interval by the specified amount.
	 * @param amount The amount to expand this interval by.
	 */
	public void expand(double amount) {
		minimum -= amount;
		maximum += amount;

		// If the interval was contracted (amount < 0) by more than
		// half the length of the interval, then the interval will
		// be empty.
		if (minimum > maximum) {
			makeEmpty();
		}
	}

	/**
	 * Computes the intersection of this interval with another.
	 * @param I The interval to intersect with this one.
	 * @return The intersection of this interval with I.
	 */
	public Interval intersection(Interval I) {
		return new Interval(Math.max(minimum, I.minimum), Math.min(maximum, I.maximum));
	}

	/**
	 * Intersects the specified ray with this one.
	 * Equivalent to {@code this = this.intersection(I);}
	 * @param I The interval to intersect with this one.
	 * @see intersection
	 */
	public void intersect(Interval I) {
		if (I.isEmpty()) {
			makeEmpty();
			return;
		}

		if (I.minimum > minimum) {
			minimum = I.minimum;
		}

		if (I.maximum < maximum) {
			maximum = I.maximum;
		}

		if (minimum > maximum) {
			makeEmpty();
		}
	}

	/**
	 * Makes this interval empty.
	 */
	private void makeEmpty() {
		minimum = maximum = Double.NaN;
	}

	/**
	 * Creates an empty interval.
	 * @return An empty interval.
	 */
	private static Interval getEmptyInterval() {
		Interval I = new Interval();
		I.makeEmpty();
		return I;
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
	public static final Interval EMPTY = getEmptyInterval();

	/** The lower bound of this interval. */
	private double minimum;

	/** The upper bound of this interval. */
	private double maximum;

}
