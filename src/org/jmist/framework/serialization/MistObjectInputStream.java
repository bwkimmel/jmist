/**
 *
 */
package org.jmist.framework.serialization;

import java.io.IOException;
import java.io.InputStream;

/**
 * An <code>ObjectInputStream</code> that uses the <code>MistClassLoader</code>
 * to resolve classes.
 * @author bkimmel
 */
public final class MistObjectInputStream extends CustomObjectInputStream {

	/**
	 * Initializes the underlying stream to read from.
	 * @param in The underlying stream to read from.
	 * @throws IOException if an I/O error occurs while reading stream header
	 */
	public MistObjectInputStream(InputStream in)
			throws IOException {
		super(in, MistClassLoader.getInstance());
	}

}
