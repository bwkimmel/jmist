/**
 *
 */
package org.jmist.framework.services;

import java.rmi.RMISecurityManager;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JDialog;

import org.jmist.framework.Job;
import org.jmist.framework.reporting.ProgressPanel;
import org.jmist.framework.reporting.ProgressTreePanel;

/**
 * @author brad
 *
 */
public final class WorkerClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

		String host = args.length > 0 ? args[0] : "localhost";
		JDialog dialog = new JDialog();
		ProgressPanel monitor = new ProgressPanel();

		int numberOfCpus = Runtime.getRuntime().availableProcessors();
		Executor threadPool = Executors.newFixedThreadPool(numberOfCpus, new BackgroundThreadFactory());
		Job workerJob = new ThreadServiceWorkerJob(host, 10000, numberOfCpus, threadPool);

		dialog.add(monitor);
		dialog.setBounds(0, 0, 400, 300);
		dialog.setVisible(true);

		workerJob.go(monitor);

	}

}
