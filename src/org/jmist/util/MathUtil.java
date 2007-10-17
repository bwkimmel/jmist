/**
 *
 */
package org.jmist.util;

/**
 * @author bkimmel
 *
 */
public final class MathUtil {

	/**
	 * Returns x if it is within the specified range, or the closest
	 * value within the specified range if x is outside the range.
	 * @param x The value to threshold.
	 * @param min The minimum of the range to threshold to.
	 * @param max The maximum of the range to threshold to.
	 * @return x, if min <= x <= max.  min, if x < min.  max, if x > max.
	 */
	public static double threshold(double x, double min, double max) {
		if (x < min) {
			return min;
		} else if (x > max) {
			return max;
		} else {
			return x;
		}
	}

	/**
	 * Returns x if it is within the specified range, or the closest
	 * value within the specified range if x is outside the range.
	 * @param x The value to threshold.
	 * @param min The minimum of the range to threshold to.
	 * @param max The maximum of the range to threshold to.
	 * @return x, if min <= x <= max.  min, if x < min.  max, if x > max.
	 */
	public static int threshold(int x, int min, int max) {
		if (x < min) {
			return min;
		} else if (x > max) {
			return max;
		} else {
			return x;
		}
	}

	/**
	 * Determines whether two floating point values are close enough
	 * to be considered "equal" (i.e., the difference may be attributed
	 * to rounding errors).
	 * @param x The first value to compare.
	 * @param y The second value to compare.
	 * @param epsilon The minimum difference required for the two values
	 * 		to be considered distinguishable.
	 * @return A value indicating whether the difference between x and y
	 * 		is less than the given threshold.
	 */
	public static boolean equal(double x, double y, double epsilon) {
		return Math.abs(x - y) < epsilon;
	}

	/**
	 * Determines whether two floating point values are close enough
	 * to be considered "equal" (i.e., the difference may be attributed
	 * to rounding errors).
	 * @param x The first value to compare.
	 * @param y The second value to compare.
	 * @return A value indicating whether the difference between x and y
	 * 		is less than MathUtil.EPSILON.
	 * @see #EPSILON
	 */
	public static boolean equal(double x, double y) {
		return Math.abs(x - y) < MathUtil.EPSILON;
	}

	/**
	 * Determines whether a floating point value is close enough to zero to be
	 * considered "equal" to zero (i.e., the difference may be attributed to
	 * rounding errors).
	 * @param x The value to compare to zero.
	 * @param epsilon The minimum absolute value of {@code x} for it to be
	 * 		considered non-zero.
	 * @return A value indicates whether the difference between {@code x} and
	 * 		0.0 is less than {@code epsilon}.
	 */
	public static boolean isZero(double x, double epsilon) {
		return Math.abs(x) < epsilon;
	}

	/**
	 * Determines whether a floating point value is close enough to zero to be
	 * considered "equal" to zero (i.e., the difference may be attributed to
	 * rounding errors).
	 * @param x The value to compare to zero.
	 * @param epsilon The minimum absolute value of {@code x} for it to be
	 * 		considered non-zero.
	 * @return A value indicates whether the difference between {@code x} and
	 * 		0.0 is less than {@link #EPSILON}.
	 * @see {@link #EPSILON}.
	 */
	public static boolean isZero(double x) {
		return Math.abs(x) < MathUtil.EPSILON;
	}

	/**
	 * Determines whether {@code x} falls within the open interval
	 * {@code (minimum, maximum)}.
	 * @param x The value to check.
	 * @param minimum The lower bound of the interval to check against.
	 * @param maximum The upper bound of the interval to check against.
	 * @return A value indicating whether {@code x} is contained in the open
	 * 		interval {@code (minimum, maximum)}.
	 */
	public static boolean inRangeOO(double x, double minimum, double maximum) {
		return minimum < x && x < maximum;
	}

	/**
	 * Determines whether {@code x} falls within the interval
	 * {@code [minimum, maximum)}.
	 * @param x The value to check.
	 * @param minimum The lower bound of the interval to check against.
	 * @param maximum The upper bound of the interval to check against.
	 * @return A value indicating whether {@code x} is contained in the
	 * 		interval {@code [minimum, maximum)}.
	 */
	public static boolean inRangeCO(double x, double minimum, double maximum) {
		return minimum <= x && x < maximum;
	}

	/**
	 * Determines whether {@code x} falls within the closed interval
	 * {@code [minimum, maximum]}.
	 * @param x The value to check.
	 * @param minimum The lower bound of the interval to check against.
	 * @param maximum The upper bound of the interval to check against.
	 * @return A value indicating whether {@code x} is contained in the closed
	 * 		interval {@code [minimum, maximum]}.
	 */
	public static boolean inRangeCC(double x, double minimum, double maximum) {
		return minimum <= x && x <= maximum;
	}

	/**
	 * Determines whether {@code x} falls within the interval
	 * {@code (minimum, maximum]}.
	 * @param x The value to check.
	 * @param minimum The lower bound of the interval to check against.
	 * @param maximum The upper bound of the interval to check against.
	 * @return A value indicating whether {@code x} is contained in the
	 * 		interval {@code (minimum, maximum]}.
	 */
	public static boolean inRangeOC(double x, double minimum, double maximum) {
		return minimum < x && x <= maximum;
	}

	/**
	 * A comparison threshold value to be used when a very high degree
	 * of precision is expected.
	 */
	public static final double TINY_EPSILON		= 1e-12;

	/**
	 * A comparison threshold value to be used when a high degree of
	 * precision is expected.
	 */
	public static final double SMALL_EPSILON	= 1e-9;

	/**
	 * A comparison threshold value to be used when a normal degree of
	 * precision is expected.
	 */
	public static final double EPSILON			= 1e-6;

	/**
	 * A comparison threshold value to be used when a low degree of
	 * precision is expected.
	 */
	public static final double BIG_EPSILON		= 1e-4;

	/**
	 * The difference between 1.0 and the next highest representable value.
	 */
	public static final double MACHINE_EPSILON	= Double.longBitsToDouble(Double.doubleToRawLongBits(1.0) + 1) - 1.0;

	/**
	 * This class contains only static utility methods and static constants,
	 * and therefore should not be creatable.
	 */
	private MathUtil() {}

}
