/**
 *
 */
package org.selfip.bkimmel.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * @author brad
 *
 */
public final class AlternateClassLoaderObjectInputStream extends
		ObjectInputStream {

	private final ClassLoader loader;

	/**
	 * @param in
	 * @throws IOException
	 */
	public AlternateClassLoaderObjectInputStream(InputStream in, ClassLoader loader)
			throws IOException {
		super(in);
		this.loader = loader;
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {
		return Class.forName(desc.getName(), true, loader);
	}

}
