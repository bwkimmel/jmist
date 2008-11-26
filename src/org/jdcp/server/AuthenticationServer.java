/**
 *
 */
package org.jdcp.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Properties;

import javax.security.auth.Subject;

import org.jdcp.remote.AuthenticationService;
import org.jdcp.remote.JobService;
import org.selfip.bkimmel.util.StringUtil;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class AuthenticationServer extends UnicastRemoteObject implements
		AuthenticationService {

	/**
	 *
	 */
	private static final long serialVersionUID = 509665934852613891L;

	private final JobService service;

	private final Properties passwords = new Properties();

	/**
	 * @throws RemoteException
	 */
	public AuthenticationServer(JobService service) throws RemoteException {
		this.service = service;
		loadPasswords(new File("/Users/brad/jmist/passwd"));
	}

	/**
	 * @param port
	 * @throws RemoteException
	 */
	public AuthenticationServer(JobService service, int port) throws RemoteException {
		super(port);
		this.service = service;
		loadPasswords(new File("/Users/brad/jmist/passwd"));
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
		loadPasswords(new File("/Users/brad/jmist/passwd"));
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.AuthenticationService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public JobService authenticate(final String username, String password)
			throws SecurityException, RemoteException {

		String stored = passwords.getProperty(username);
		if (stored != null) {
			byte[] storedHash = StringUtil.hexToByteArray(stored);
			byte[] hash = computeHash(password);
			if (Arrays.equals(storedHash, hash)) {

				Subject user = new Subject();
				user.getPrincipals().add(new JdcpPrincipal(username));

				return new JobServiceProxy(user, service);
			}
		}

		throw new InvalidUserException(username);

	}

	private byte[] computeHash(String password) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			sha.update(password.getBytes());
			return sha.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new UnexpectedException(e);
		}
	}

	private void loadPasswords(File file) {
		try {
			passwords.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
