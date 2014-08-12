/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.util;

import java.util.Arrays;

/**
 * Static utility methods for working with arrays.
 * @author Brad Kimmel
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
	 * Resets all elements of the given array to zero.
	 * @param array The array of <code>int</code>s to zero the values of.
	 * 		This may be <code>null</code>, in which case nothing is done.
	 * @return A reference to <code>array</code>.
	 */
	public static int[] reset(int[] array) {
		if (array != null) {
			Arrays.fill(array, 0);
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
	 * Returns an array of zeros of the specified length.
	 * @param array The array of <code>int</code>s to zero and return.  If
	 * 		<code>null</code>, a new array will be created.  Otherwise, all
	 * 		elements will be set to zero and this array will be returned.
	 * @param length The length of the array to return.
	 * @return An array of the specified length that is all zeros.
	 * @throws IllegalArgumentException if <code>array</code> is not
	 * 		<code>null</code> and <code>array.length != length</code>.
	 */
	public static int[] initialize(int[] array, int length) {

		if (array == null) {
			return new int[length];
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
	 * Returns an array of <code>int</code>s, all equal to the specified
	 * value, of the specified length.
	 * @param array The array of <code>int</code>s to zero and return.  If
	 * 		<code>null</code>, a new array will be created.  Otherwise, all
	 * 		elements will be set to zero and this array will be returned.
	 * @param length The length of the array to return.
	 * @param value The value to initialize each element of the array to.
	 * @return An array of the specified length that is all zeros.
	 * @throws IllegalArgumentException if <code>array</code> is not
	 * 		<code>null</code> and <code>array.length != length</code>.
	 */
	public static int[] initialize(int[] array, int length, int value) {

		if (array == null) {
			array = new int[length];
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
	 * Copies a range of values from one array into another array.
	 * @param target The array of <code>double</code>s to receive the values.
	 * @param targetStart The index into <code>target</code> at which to start
	 * 		copying values into (this array will be modified).
	 * @param source The array of <code>double</code>s from which to copy.
	 * @param sourceStart The index into <code>source</code> at which to start
	 * 		copying values.
	 * @param length The number of values to copy from <code>source</code> to
	 * 		<code>target</code>.
	 * @return A reference to <code>target</code>.
	 */
	public static double[] setRange(double[] target, int targetStart,
			double[] source, int sourceStart, int length) {

		assert(target.length <= targetStart + length);
		assert(source.length <= sourceStart + length);

		for (int i = sourceStart, j = targetStart, n = 0; n < length; i++, j++, n++) {
			target[j] = source[i];
		}

		return target;

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
	 * Sets all of the values in the specified array of <code>int</code>s to
	 * the same value.
	 * @param array The array of <code>int</code>s the elements of which are to
	 * 		be set (the elements of this array will be modified).
	 * @param value The value to set each element of <code>array</code> to.
	 * @return A reference to <code>array</code>.
	 */
	public static int[] setAll(int[] array, int value) {
		Arrays.fill(array, value);
		return array;
	}

	/**
	 * Fills an array with values uniformly within a specified range.
	 * @param array The array to populate.
	 * @param first The value to assign to the first element of
	 * 		<code>array</code>.
	 * @param last The value to assign to the last element of the
	 * 		<code>array</code>.
	 * @return A reference to <code>array</code>, populated with the values
	 * 		<code>array[i] == first + (i / (array.length - 1)) * last</code>,
	 * 		for <code>0 &lt;= i &lt; array.length</code>.
	 */
	public static double[] fillRange(double[] array, double first, double last) {

		if (array == null) {
			throw new IllegalArgumentException("array is null");
		}

		for (int i = 0; i < array.length; i++) {
			double t = i / (double) (array.length - 1);
			array[i] = first + t * (last - first);
		}

		return array;

	}

	/**
	 * Fills an array with each value within a specified range.
	 * @param array The array to populate.
	 * @param first The value to assign to the first element of
	 * 		<code>array</code> (must be <code>null</code> or of length equal to
	 * 		<code>1 + |last - first|</code>.
	 * @param last The value to assign to the last element of the
	 * 		<code>array</code>.
	 * @return A reference to <code>array</code>, populated with the values
	 * 		<code>first</code> through <code>last</code>.
	 * @throws IllegalArgumentException if <code>array != null</code> and
	 * 		<code>array.length != 1 + Math.abs(last - first)</code>.
	 */
	public static int[] fillRange(int[] array, int first, int last) {

		array = ArrayUtil.initialize(array, 1 + Math.abs(last - first), first);

		if (last >= first) {
			for (int i = 0; i < array.length; i++) {
				array[i] += i;
			}
		} else { /* last < first */
			for (int i = 0; i < array.length; i++) {
				array[i] -= i;
			}
		}

		return array;

	}

	/**
	 * Creates a new array populated with values spaced uniformly in the
	 * specified range.
	 * @param first The value to assign to the first element of the array.
	 * @param last The value to assign to the last element of the array.
	 * @param length The length of the array.
	 * @return An array of the given length, populated with the values
	 * 		<code>array[i] == first + (i / (length - 1)) * last</code>, for
	 * 		<code>0 &lt;= i &lt; length</code>.
	 */
	public static double[] range(double first, double last, int length) {
		return ArrayUtil.fillRange(new double[length], first, last);
	}

	/**
	 * Creates a new array populated with values in the specified range.
	 * @param first The value to assign to the first element of the array.
	 * @param last The value to assign to the last element of the array.
	 * @return An array of the given length, populated with the values
	 * 		from <code>first</code> to <code>last</code>.
	 */
	public static int[] range(int first, int last) {
		return ArrayUtil.fillRange((int[]) null, first, last);
	}

	/**
	 * Swaps two elements of an array.
	 * @param arr The array whose elements to swap.
	 * @param a The index of the first element.
	 * @param b The index of the second element.
	 */
	public static void swap(int[] arr, int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}

	/**
	 * Swaps two elements of an array.
	 * @param arr The array whose elements to swap.
	 * @param a The index of the first element.
	 * @param b The index of the second element.
	 */
	public static void swap(float[] arr, int a, int b) {
		float temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}

	/**
	 * Swaps two elements of an array.
	 * @param arr The array whose elements to swap.
	 * @param a The index of the first element.
	 * @param b The index of the second element.
	 */
	public static void swap(double[] arr, int a, int b) {
		double temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}

	/**
	 * Default constructor.  This constructor is private because this class
	 * cannot be instantiated.
	 */
	private ArrayUtil() {
		/* do nothing */
	}

}
