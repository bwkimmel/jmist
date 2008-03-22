/**
 *
 */
package org.selfip.bkimmel.util;

/**
 * Utility methods for working with strings.
 * @author brad
 */
public final class StringUtil {

	/** The hexadecimal digits. */
	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * Converts the specified byte value to a two digit hex string.
	 * @param b The <code>byte</code> value to convert to a string.
	 * @return The two digit hexadecimal representation of <code>b</code>.
	 */
	public static String toHex(byte b) {
		final char[] string = { hexDigits[(b >> 4) & 0x0f], hexDigits[b & 0x0f] };
		return new String(string);
	}

	/**
	 * Converts the specified array of bytes to a hex string.
	 * @param bytes The array of bytes to convert to a string.
	 * @return The hexadecimal representation of <code>bytes</code>.
	 */
	public static String toHex(byte[] bytes) {
		final char[] string = new char[2 * bytes.length];
		int i = 0;
		for (byte b : bytes) {
			string[i++] = hexDigits[(b >> 4) & 0x0f];
			string[i++] = hexDigits[b & 0x0f];
		}
		return new String(string);
	}

	/**
	 * Converts a number expressed in hexadecimal to a byte array.
	 * @param hex The <code>String</code> representation of the number, in
	 * 		hexadecimal.
	 * @return The byte array represented by <code>hex</code>.
	 */
	public static byte[] hexToByteArray(String hex) {
		int length = hex.length();
		byte[] result = new byte[(length / 2) + (length % 2)];
		for (int i = length, j = result.length - 1; i > 0; i -= 2, j--) {
			result[j] = hexToByte(hex.charAt(i - 1));
			if (i > 1) {
				result[j] |= (hexToByte(hex.charAt(i - 2)) << 4);
			}
		}
		return result;
	}

	/**
	 * Converts a hexadecimal digit to a byte.
	 * @param hex The hexadecimal digit.
	 * @return The byte value corresponding to <code>hex</code>.
	 */
	public static byte hexToByte(char hex) {
		if ('0' <= hex && hex <= '9') {
			return (byte) (hex - '0');
		} else if ('A' <= hex && hex <= 'F') {
			return (byte) (10 + hex - 'A');
		} else if ('a' <= hex && hex <= 'f') {
			return (byte) (10 + hex - 'a');
		} else {
			throw new IllegalArgumentException(String.format("'%c' is not a hexadecimal digit.", hex));
		}
	}

	/** Declared private to prevent this class from being instantiated. */
	private StringUtil() {}

}
