/**
 *
 */
package org.selfip.bkimmel.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods for working with streams.
 * @author brad
 */
public final class StreamUtil {

	/**
	 * Copies the contents of an <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * @param in The <code>InputStream</code> to read from.
	 * @param out The <code>OutputStream</code> to write to.
	 * @throws IOException If unable to read from <code>in</code> or write to
	 * 		<code>out</code>.
	 */
	public static void writeStream(InputStream in, OutputStream out) throws IOException {
		int c;
		while ((c = in.read()) >= 0) {
			out.write((byte) c);
		}
	}

	/** Declared private to prevent this class from being instantiated. */
	private StreamUtil() {}

}
