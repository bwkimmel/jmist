/**
 *
 */
package org.selfip.bkimmel.util;

/**
 * An <code>UnexpectedException</code> is thrown to indicate that a checked
 * exception was caught that was not expected to be thrown.  For example, if a
 * method declares that it throws <code>MyException</code>, but steps were
 * taken in the calling code to ensure that <code>MyException</code> would not
 * be thrown and <code>MyException</code> was nonetheless thrown, the calling
 * code may throw an <code>UnexpectedException</code>.  In other words, a
 * statement throwing an <code>UnexpectedException</code> is essentially an
 * assertion declaring that that line of code will not be reached.
 *
 * @author brad
 */
public class UnexpectedException extends RuntimeException {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 5836524376615051737L;

	/**
	 * Creates a new <code>UnexpectedException</code>.
	 */
	public UnexpectedException() {
		super();
	}

	/**
	 * Creates a new <code>UnexpectedException</code>.
	 * @param message The detailed message describing the condition.
	 * @param cause The cause of the <code>UnexpectedException</code>.
	 */
	public UnexpectedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new <code>UnexpectedException</code>.
	 * @param message The detailed message describing the condition.
	 */
	public UnexpectedException(String message) {
		super(message);
	}

	/**
	 * Creates a new <code>UnexpectedException</code>.
	 * @param cause The cause of the <code>UnexpectedException</code>.
	 */
	public UnexpectedException(Throwable cause) {
		super(cause);
	}


}
