/**
 *
 */
package org.jdcp.security;

import java.security.BasicPermission;

/**
 * @author brad
 *
 */
public final class JdcpPermission extends BasicPermission {

	/**
	 *
	 */
	private static final long serialVersionUID = 7740220114085223696L;

	/**
	 * @param name
	 */
	public JdcpPermission(String name) {
		super(name);
	}

}
