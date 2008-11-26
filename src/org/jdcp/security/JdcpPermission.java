/**
 *
 */
package org.jdcp.security;

import java.security.BasicPermission;

/**
 * Represents a <code>Permission</code> for a JDCP-related action.
 * @author brad
 */
public final class JdcpPermission extends BasicPermission {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 7740220114085223696L;

	/**
	 * Creates a new <code>JdcpPermission</code>.
	 * @param name The name of the permission.
	 */
	public JdcpPermission(String name) {
		super(name);
	}

}
