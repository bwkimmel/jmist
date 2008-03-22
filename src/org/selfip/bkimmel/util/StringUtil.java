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

	/** Declared private to prevent this class from being instantiated. */
	private StringUtil() {}

}
