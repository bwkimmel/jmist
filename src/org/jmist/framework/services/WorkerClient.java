/**
 *
 */
package org.jmist.framework.services;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JDialog;

import org.jmist.framework.Job;
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

		String host = args[0].trim().length() > 0 ? args[0] : "localhost";
		JDialog dialog = new JDialog();
		ProgressTreePanel monitor = new ProgressTreePanel();
		Executor threadPool = Executors.newFixedThreadPool(2, new BackgroundThreadFactory());
		Job workerJob = new ThreadServiceWorkerJob(host, 10000, 2, threadPool);

		dialog.add(monitor);
		dialog.setBounds(0, 0, 400, 300);
		dialog.setVisible(true);

		workerJob.go(monitor);

	}

}
