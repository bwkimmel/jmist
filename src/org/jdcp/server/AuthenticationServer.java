/**
 *
 */
package org.jdcp.server;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jdcp.remote.AuthenticationService;
import org.jdcp.remote.JobService;
import org.selfip.bkimmel.auth.FixedCallbackHandler;

/**
 * @author brad
 *
 */
public final class AuthenticationServer extends UnicastRemoteObject implements
		AuthenticationService {

	/**
	 *
	 */
	private static final long serialVersionUID = 6823054390091081114L;

	private static final String LOGIN_CONFIGURATION_NAME = "JobService";

	private final JobService service;

	/**
	 * @throws RemoteException
	 */
	public AuthenticationServer(JobService service) throws RemoteException {
		this.service = service;
	}

	/**
	 * @param port
	 * @throws RemoteException
	 */
	public AuthenticationServer(JobService service, int port) throws RemoteException {
		super(port);
		this.service = service;
	}

	/**
	 * @param port
	 * @param csf
	 * @param ssf
	 * @throws RemoteException
	 */
	public AuthenticationServer(JobService service, int port, RMIClientSocketFactory csf,
			RMIServerSocketFactory ssf) throws RemoteException {
		super(port, csf, ssf);
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.AuthenticationService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public JobService authenticate(final String username, final String password)
			throws RemoteException, LoginException {

		CallbackHandler handler = FixedCallbackHandler.forNameAndPassword(username, password);
		LoginContext context = new LoginContext(LOGIN_CONFIGURATION_NAME, handler);
		context.login();

		return new JobServiceProxy(context.getSubject(), service);

	}

}
