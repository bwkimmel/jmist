/**
 *
 */
package org.jdcp.server;

/**
 * @author brad
 *
 */
public final class InvalidUserException extends SecurityException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4736720965280053480L;

	/**
	 * @param username
	 */
	public InvalidUserException(String username) {
		super("Invalid user: " + username);
	}

}
