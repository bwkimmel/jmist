/**
 *
 */
package org.jmist.util;

/**
 * @author bkimmel
 *
 */
public final class MathUtil {

	public static double threshold(double x, double min, double max) {
		if (x < min) {
			return min;
		} else if (x > max) {
			return max;
		} else {
			return x;
		}
	}

	public static int threshold(int x, int min, int max) {
		if (x < min) {
			return min;
		} else if (x > max) {
			return max;
		} else {
			return x;
		}
	}

	public static boolean equal(double x, double y, double epsilon) {
		return Math.abs(x - y) < epsilon;
	}

	public static boolean equal(double x, double y) {
		return Math.abs(x - y) < MathUtil.EPSILON;
	}

	public static final double TINY_EPSILON		= 1e-12;
	public static final double SMALL_EPSILON	= 1e-9;
	public static final double EPSILON			= 1e-6;
	public static final double BIG_EPSILON		= 1e-4;

	/**
	 * This class contains only static utility methods and static constants,
	 * and therefore should not be creatable.
	 */
	private MathUtil() {}

}
