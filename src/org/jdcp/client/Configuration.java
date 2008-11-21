/**
 *
 */
package org.jdcp.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.jdcp.remote.JobService;

/**
 * @author brad
 *
 */
public final class Configuration {

	public boolean verbose = false;
	public String host = "localhost";
	public String digestAlgorithm = "MD5";


	private JobService service = null;

	public JobService getJobService() throws RemoteException, NotBoundException {
		if (service == null) {
			Registry registry = LocateRegistry.getRegistry(host);
			service = (JobService) registry.lookup("JobService");
		}
		return service;
	}

}
