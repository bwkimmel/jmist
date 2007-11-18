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

}
