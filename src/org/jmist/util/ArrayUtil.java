/**
 *
 */
package org.jmist.util;

import java.util.Arrays;

/**
 * Static utility methods for working with arrays.
 * @author bkimmel
 */
public final class ArrayUtil {

	/**
	 * Resets all elements of the given array to zero.
	 * @param array The array of <code>double</code>s to zero the values of.
	 * 		This may be <code>null</code>, in which case nothing is done.
	 * @return A reference to <code>array</code>.
	 */
	public static double[] reset(double[] array) {
		if (array != null) {
			Arrays.fill(array, 0.0);
		}
		return array;
	}

	/**
	 * Returns an array of zeros of the specified length.
	 * @param array The array of <code>double</code>s to zero and return.  If
	 * 		<code>null</code>, a new array will be created.  Otherwise, all
	 * 		elements will be set to zero and this array will be returned.
	 * @param length The length of the array to return.
	 * @return An array of the specified length that is all zeros.
	 * @throws IllegalArgumentException if <code>array</code> is not
	 * 		<code>null</code> and <code>array.length != length</code>.
	 */
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
	 * Returns an array of <code>double</code>s, all equal to the specified
	 * value, of the specified length.
	 * @param array The array of <code>double</code>s to zero and return.  If
	 * 		<code>null</code>, a new array will be created.  Otherwise, all
	 * 		elements will be set to zero and this array will be returned.
	 * @param length The length of the array to return.
	 * @param value The value to initialize each element of the array to.
	 * @return An array of the specified length that is all zeros.
	 * @throws IllegalArgumentException if <code>array</code> is not
	 * 		<code>null</code> and <code>array.length != length</code>.
	 */
	public static double[] initialize(double[] array, int length, double value) {

		if (array == null) {
			array = new double[length];
		} else if (array.length != length) {
			throw new IllegalArgumentException(String.format(
					"Invalid array length: expected %d but got %d.", length,
					array.length));
		}

		return ArrayUtil.setAll(array, value);

	}

	/**
	 * Populates a contiguous range within an array of <code>double</code>s
	 * with the values from another array of <code>double</code>s.
	 * @param array The array of <code>double</code>s to populate.
	 * @param start The index into <code>array</code> of the first element to
	 * 		populate.
	 * @param values An array of <code>double</code>s to populate part of
	 * 		<code>array</code> with.
	 * @return A reference to <code>array</code>.
	 */
	public static double[] setRange(double[] array, int start, double[] values) {
		assert(array.length <= start + values.length);
		for (int i = 0, j = start; i < values.length; i++, j++) {
			array[j] = values[i];
		}
		return array;
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
		Arrays.fill(array, value);
		return array;
	}

	/**
	 * Default constructor.  This constructor is private because this class
	 * cannot be instantiated.
	 */
	private ArrayUtil() {
		/* do nothing */
	}

}
