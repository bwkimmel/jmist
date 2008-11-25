/**
 *
 */
package org.jdcp.client;

import java.rmi.RemoteException;

import org.selfip.bkimmel.util.args.AbstractCommand;

/**
 * @author brad
 *
 */
public class SetIdleTimeCommand extends AbstractCommand<Configuration> {

	/* (non-Javadoc)
	 * @see org.jdcp.client.Command#run(java.util.List, org.jdcp.client.Configuration)
	 */
	public void run(String[] args, Configuration conf) {
		int seconds = Integer.parseInt(args[0]);
		try {
			conf.getJobService().setIdleTime(seconds);
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid priority: " + args[0]);
		} catch (SecurityException e) {
			System.err.println("Access denied.");
		} catch (RemoteException e) {
			System.err.println("Failed to set idle time on remote host.");
			e.printStackTrace();
		}
	}

}
