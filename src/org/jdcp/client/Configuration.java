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

	public JobService getJobService() {
		if (service == null) {
			try {
				Registry registry = LocateRegistry.getRegistry(host);
				service = (JobService) registry.lookup("JobService");
			} catch (NotBoundException e) {
				System.err.println("Job service not found at remote host.");
				System.exit(1);
			} catch (RemoteException e) {
				System.err.println("Could not connect to job service.");
				System.exit(1);
			}
		}
		return service;
	}

}
