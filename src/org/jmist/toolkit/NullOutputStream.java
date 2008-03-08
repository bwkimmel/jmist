/**
 *
 */
package org.jmist.toolkit;

import java.io.OutputStream;

/**
 * An <code>OutputStream</code> that ignores everything written to it.
 * @author brad
 */
public final class NullOutputStream extends OutputStream {

	/**
	 * Creates a new <code>NullOutputStream</code>.
	 */
	public NullOutputStream() {
		/* nothing to do */
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
