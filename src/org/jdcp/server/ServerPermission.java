/**
 *
 */
package org.jdcp.server;

import java.security.BasicPermission;

/**
 * @author brad
 *
 */
public final class ServerPermission extends BasicPermission {

	/**
	 *
	 */
	private static final long serialVersionUID = 7740220114085223696L;

	/**
	 * @param name
	 */
	public ServerPermission(String name) {
		super(name);
	}

}
