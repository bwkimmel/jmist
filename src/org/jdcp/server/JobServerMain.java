/**
 *
 */
package org.jdcp.server;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.prefs.Preferences;

import javax.swing.JDialog;

import org.jdcp.remote.JobService;
import org.jdcp.scheduling.PrioritySerialTaskScheduler;
import org.jdcp.scheduling.TaskScheduler;
import org.jdcp.server.classmanager.FileClassManager;
import org.jdcp.server.classmanager.ParentClassManager;
import org.selfip.bkimmel.progress.ProgressPanel;

/**
 * @author brad
 *
 */
public final class JobServerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			System.err.print("Initializing progress monitor...");
			JDialog dialog = new JDialog();
			ProgressPanel monitor = new ProgressPanel();
			dialog.add(monitor);
			dialog.setBounds(100, 100, 500, 350);
			System.err.println("OK");

			System.err.print("Initializing folders...");
			Preferences pref = Preferences
					.userNodeForPackage(JobServer.class);
			String path = pref.get("rootDirectory", "");
			File rootDirectory = new File(path);
			File classesDirectory = new File(rootDirectory, "classes");
			File jobsDirectory = new File(rootDirectory, "jobs");

			classesDirectory.mkdir();
			jobsDirectory.mkdir();
			System.err.println("OK");

			System.err.print("Initializing service...");
			ParentClassManager classManager = new FileClassManager(classesDirectory);
			TaskScheduler scheduler = new PrioritySerialTaskScheduler();
			JobServer jobServer = new JobServer(jobsDirectory, monitor, scheduler, classManager);
			System.err.println("OK");

			System.err.print("Exporting service stubs...");
			JobService jobStub = (JobService) UnicastRemoteObject.exportObject(
					jobServer, 0);
			System.err.println("OK");

			System.err.print("Binding service...");
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("JobService", jobStub);
			System.err.println("OK");

			System.err.println("Server ready");

			monitor.setRootVisible(false);

			dialog.setTitle("JobServer");
			dialog.setModal(true);
			dialog.setVisible(true);

			System.err.print("Shutting down...");
			registry.unbind("JobService");
			System.err.println("OK");
			System.exit(0);

		} catch (Exception e) {

			System.err.println("Server exception:");
			e.printStackTrace();

		}

	}

}
