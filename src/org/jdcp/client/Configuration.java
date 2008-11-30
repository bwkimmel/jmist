/**
 *
 */
package org.jdcp.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.security.auth.login.LoginException;

import org.jdcp.remote.AuthenticationService;
import org.jdcp.remote.JobService;

/**
 * @author brad
 *
 */
public final class Configuration {

	public boolean verbose = false;
	public String host = "localhost";
	public String digestAlgorithm = "MD5";

	public String username = "guest";
	public String password = "";


	private JobService service = null;

	public JobService getJobService() {
		if (service == null) {
			try {
				Registry registry = LocateRegistry.getRegistry(host);
				AuthenticationService auth = (AuthenticationService) registry.lookup("AuthenticationService");
				service = auth.authenticate(username, password);
			} catch (NotBoundException e) {
				System.err.println("Job service not found at remote host.");
				System.exit(1);
			} catch (RemoteException e) {
				System.err.println("Could not connect to job service.");
				e.printStackTrace();
				System.exit(1);
			} catch (LoginException e) {
				System.err.println("Login failed.");
				System.exit(1);
			}
		}
		return service;
	}

}
