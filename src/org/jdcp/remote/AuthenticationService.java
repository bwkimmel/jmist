/**
 *
 */
package org.jdcp.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A remote service for authenticating <code>JobService</code> users.
 * @author bwkimmel
 */
public interface AuthenticationService extends Remote {

	/**
	 * Authenticates a user.
	 * @param username The username identifying the user to authenticate.
	 * @param password The password of the user to authenticate.
	 * @return The <code>JobService</code> to use for this session.
	 * @throws SecurityException if the user name or password are invalid.
	 */
	JobService authenticate(String username, String password) throws RemoteException, SecurityException;

}
