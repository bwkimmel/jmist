/**
 *
 */
package org.selfip.bkimmel.auth;

import java.security.Principal;

/**
 * A <code>Principal</code> that represents an individual user.
 * @author brad
 */
public final class UserPrincipal implements Principal {

	/** The name of the user. */
	private final String name;

	/**
	 * Creates a new <code>UserPrincipal</code>.
	 * @param name The name of the user.
	 */
	public UserPrincipal(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

}
