/**
 *
 */
package org.jmist.framework.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * An object input stream that uses a provided <code>ClassLoader</code> to
 * resolve class names.
 * @author bkimmel
 */
public class CustomObjectInputStream extends ObjectInputStream {

	/**
	 * Initializes the input stream and the class loader to use.
	 * @param in The <code>InputStream</code> to read from.
	 * @param classLoader The <code>ClassLoader</code> to use to resolve
	 * 		class names.
	 * @throws IOException if an I/O error occurs while reading stream header
	 */
	public CustomObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
		super(in);
		this.classLoader = classLoader;
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
	 */
	@Override
	protected final Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {

		return this.classLoader.loadClass(desc.getName());

	}

	/** The <code>ClassLoader</code> to use to resolve class names. */
	private final ClassLoader classLoader;

}
