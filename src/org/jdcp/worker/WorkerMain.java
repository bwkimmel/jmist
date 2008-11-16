/**
 *
 */
package org.jdcp.worker;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JDialog;

import org.jdcp.concurrent.BackgroundThreadFactory;
import org.selfip.bkimmel.jobs.Job;
import org.selfip.bkimmel.progress.ProgressPanel;

/**
 * @author brad
 *
 */
public final class WorkerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String host = getParentHost(args);
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

	/**
	 * Gets the host name to get tasks from.
	 * @param args The command line arguments.
	 * @return The master host.
	 */
	private static String getParentHost(String[] args) {

		/* If a host was passed on the command line, use it. */
		if (args.length > 0) {

			return args[0];

		} else { // args.length == 0

			/*
			 * If this application is being run via Java Web Start, then look
			 * up the code base URL and use that host, otherwise, the parent
			 * is "localhost".
			 */
			try {
				BasicService service = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
				return service.getCodeBase().getHost();
			} catch (UnavailableServiceException e) {
				return "localhost";
			}

		}

	}

}
