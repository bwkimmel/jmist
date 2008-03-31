/**
 *
 */
package org.jmist.framework.services;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RMISecurityManager;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JDialog;

import org.jmist.framework.Job;
import org.jmist.framework.reporting.ProgressPanel;

/**
 * @author brad
 *
 */
public final class WorkerClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String host = args.length > 0 ? args[0] : "localhost";

		int numberOfCpus = Runtime.getRuntime().availableProcessors();
		Executor threadPool = Executors.newFixedThreadPool(numberOfCpus, new BackgroundThreadFactory());
		Job workerJob = new ThreadServiceWorkerJob(host, 10000, numberOfCpus, threadPool);

		ProgressPanel monitor = new ProgressPanel();
		monitor.setRootVisible(false);

		final JDialog dialog = new JDialog();
		dialog.addWindowListener(new WindowAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				dialog.setVisible(false);
				System.exit(0);
			}

		});

		dialog.add(monitor);
		dialog.setBounds(0, 0, 400, 300);
		dialog.setVisible(true);

		workerJob.go(monitor);

	}

}
