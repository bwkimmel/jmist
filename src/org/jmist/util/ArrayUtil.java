/**
 *
 */
package org.jmist.util;

/**
 * @author bkimmel
 *
 */
public final class ArrayUtil {

	public static double[] reset(double[] array) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				array[i] = 0.0;
			}
		}
		return array;
	}

	public static double[] initialize(double[] array, int length) {

		if (array == null) {
			return new double[length];
		} else if (array.length != length) {
			throw new IllegalArgumentException(String.format(
					"Invalid array length: expected %d but got %d.", length,
					array.length));
		}

		return ArrayUtil.reset(array);

	}
	
	/**
	 * Default constructor.  This constructor is private because this class
	 * cannot be instantiated.
	 */
	private ArrayUtil() {
		/* do nothing */
	}

	/**
	 * Sets all of the values in the specified array of <code>double</code>s to
	 * the same value.
	 * @param array The array of <code>double</code>s the elements of which are
	 * 		to be set (the elements of this array will be modified).
	 * @param value The value to set each element of <code>array</code> to.
	 * @return A reference to <code>array</code>.
	 */
	public static double[] setAll(double[] array, double value) {
		for (int i = 0; i < array.length; i++) {
			array[i] = value;
		}
		return array;
	}

	/**
	 * Multiplies all of the values in the specified array of
	 * <code>double</code>s by the same value.
	 * @param array The array of <code>double</code>s the elements of which are
	 * 		to be multiplied by a constant factor (the elements of this array
	 * 		will be modified).
	 * @param factor The value by which to multiply each element of
	 * 		<code>array</code>.
	 * @return A reference to <code>array</code>.
	 */
	public static double[] scale(double[] array, double factor) {
		for (int i = 0; i < array.length; i++) {
			array[i] *= factor;
		}
		return array;
	}

}
