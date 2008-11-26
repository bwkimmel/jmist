/**
 *
 */
package org.jdcp.server;

import java.security.Principal;

/**
 * @author brad
 *
 */
public final class JdcpPrincipal implements Principal {

	private final String name;

	/**
	 * @param name
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
