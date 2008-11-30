/**
 *
 */
package org.selfip.bkimmel.auth;

import java.security.Principal;

/**
 * A <code>Principal</code> that represents a role.
 * @author brad
 */
public final class RolePrincipal implements Principal {

	/** The name of the role. */
	private final String role;

	/**
	 * Creates a new <code>RolePrincipal</code>.
	 * @param role The name of the role.
	 */
	public RolePrincipal(String role) {
		this.role = role;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return role;
	}

}
