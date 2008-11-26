/**
 *
 */
package org.jdcp.security;

/**
 * A <code>SecurityException</code> thrown when an attempt to authenticate
 * with a JDCP service fails.
 * @author brad
 */
public final class InvalidUserException extends SecurityException {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4736720965280053480L;

	/**
	 * Creates a new <code>InvalidUserException</code>.
	 * @param username The name of the user that failed to authenticate.
	 */
	public InvalidUserException(String username) {
		super("Invalid user: " + username);
	}

}
