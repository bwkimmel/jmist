/**
 *
 */
package org.selfip.bkimmel.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * TODO: Implement this class.
 * @author brad
 *
 */
public final class ClassDigestObjectInputStream extends ObjectInputStream {

	/**
	 * @throws IOException
	 * @throws SecurityException
	 */
	public ClassDigestObjectInputStream() throws IOException, SecurityException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param in
	 * @throws IOException
	 */
	public ClassDigestObjectInputStream(InputStream in) throws IOException {
		super(in);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		return super.resolveClass(desc);
	}

}
