/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.loader.ply;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;

/**
 * Reads lines of ASCII text from an <code>InputStream</code> without losing
 * track of the position within the underlying stream.  This allows for another
 * reader (e.g., a binary reader) to take over afterward without losing data
 * to buffering by this <code>LineReader</code>.
 */
final class LineReader {

	/** The underlying <code>InputStream</code> to read from. */
	private final PushbackInputStream inner;

	/** The buffer in which to store data to be read. */
	private final ByteBuffer buffer;

	/**
	 * Creates a new <code>LineReader</code>.
	 * @param inner The <code>PushbackInputStream</code> to read from.
	 * @param size The size of the buffer to use (should not exceed the size
	 * 		of the pushback buffer on <code>inner</code>).
	 */
	public LineReader(PushbackInputStream inner, int size) {
		this.inner = inner;
		this.buffer = ByteBuffer.allocate(size);

		// Ensure that the buffer is empty initially so that it will be
		// populated on the first attempt to read from it.
		buffer.clear();
		buffer.flip();
	}

	/**
	 * Reads a byte from the input.
	 * @return The byte read from the input, or <code>-1</code> if input has
	 * 		been exhausted.
	 * @throws IOException If an error occurs while reading from the underlying
	 * 		<code>InputStream</code>.
	 */
	private int read() throws IOException {
		if (!buffer.hasRemaining()) {
			buffer.clear();
			int read = inner.read(buffer.array(), 0, buffer.capacity());
			if (read < 0) { // EOF
				return -1;
			}
			buffer.position(read);
			buffer.flip();
		}

		return buffer.get();
	}

	/**
	 * Reads a byte from the input without consuming it.
	 * @return The byte read from the input, or <code>-1</code> if input has
	 * 		been exhausted.
	 * @throws IOException If an error occurs while reading from the underlying
	 * 		<code>InputStream</code>.
	 */
	private int peek() throws IOException {
		if (buffer.hasRemaining()) {
			return buffer.get(buffer.position());
		} else {
			int result = inner.read();
			if (result >= 0) {
				inner.unread(result);
			}
			return result;
		}
	}

	/**
	 * Reads a CR, LF, CR+LF, or LF+CR terminated line of ASCII text from the
	 * underlying <code>InputStream</code>.
	 * @return The line read from the underlying stream, or <code>null</code>
	 * 		if the input has been exhausted.
	 * @throws IOException If an error occurs while reading from the underlying
	 * 		<code>InputStream</code>.
	 */
	public String readLine() throws IOException {
		StringBuilder line = new StringBuilder();
		char c;
		while ((c = (char) read()) >= 0) {
			if (c == '\n' || c == '\r') {
				if ((c == '\n' && peek() == '\r') || (c == '\r' && peek() == '\n')) {
					read();
				}
				break;
			}
			line.append(c);
		}
		if (c < 0 && line.length() == 0) {
			return null;
		}
		return line.toString();
	}

	/**
	 * Push any unconsumed buffered input back onto the underlying
	 * <code>PushbackInputStream</code>.
	 * @throws IOException If there is not enough room on the underlying
	 * 		stream's pushback buffer to accomodate the unconsumed data in this
	 * 		<code>LineReader</code>'s buffer.
	 */
	public void unreadBuffer() throws IOException {
		inner.unread(buffer.array(), buffer.position(), buffer.remaining());
	}

}
