/**
 *
 */
package org.selfip.bkimmel.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

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

	/**
	 * Writes the contents of a <code>ByteBuffer</code> to the provided
	 * <code>OutputStream</code>.
	 * @param bytes The <code>ByteBuffer</code> containing the bytes to
	 * 		write.
	 * @param out The <code>OutputStream</code> to write to.
	 * @throws IOException If unable to write to the
	 * 		<code>OutputStream</code>.
	 */
	public static void writeBytes(ByteBuffer bytes, OutputStream out) throws IOException {
		final int BUFFER_LENGTH = 1024;
		byte[] buffer;

		if (bytes.remaining() >= BUFFER_LENGTH) {
			buffer = new byte[BUFFER_LENGTH];
			do {
				bytes.get(buffer);
				out.write(buffer);
			} while (bytes.remaining() >= BUFFER_LENGTH);
		} else {
			buffer = new byte[bytes.remaining()];
		}

		int remaining = bytes.remaining();
		if (remaining > 0) {
			bytes.get(buffer, 0, remaining);
			out.write(buffer, 0, remaining);
		}
	}

	/** Declared private to prevent this class from being instantiated. */
	private StreamUtil() {}

}
