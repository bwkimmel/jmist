/**
 *
 */
package org.jdcp.security;

import java.security.Principal;

/**
 * Represents the identity of a remote user of a JDCP server.
 * @author brad
 */
public final class JdcpPrincipal implements Principal {

	/** The name of the user. */
	private final String name;

	/**
	 * Creates a new <code>JdcpPrincipal</code>.
	 * @param name The name of the user.
	 */
	public JdcpPrincipal(String name) {
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
