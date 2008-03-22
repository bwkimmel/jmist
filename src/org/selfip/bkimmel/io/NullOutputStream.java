/**
 *
 */
package org.selfip.bkimmel.io;

import java.io.OutputStream;

/**
 * An <code>OutputStream</code> that ignores everything written to it.  This
 * class is a singleton.
 * @author brad
 */
public final class NullOutputStream extends OutputStream {

	/** The single instance of <code>NullOutputStream</code>. */
	private static NullOutputStream instance;

	/**
	 * Creates a new <code>NullOutputStream</code>.  This constructor is
	 * private because this class is a singleton.
	 */
	private NullOutputStream() {
		/* nothing to do */
	}

	/**
	 * Gets the single instance of <code>NullOutputStream</code>.
	 * @return The single instance of <code>NullOutputStream</code>.
	 */
	public static NullOutputStream getInstance() {
		if (instance == null) {
			instance = new NullOutputStream();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int arg0) {
		/* do nothing */
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) {
		/* do nothing */
	}

	/* s(non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) {
		/* do nothing */
	}

}
