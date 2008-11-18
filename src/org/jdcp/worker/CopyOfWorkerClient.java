/**
 *
 */
package org.jdcp.worker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.jdcp.concurrent.BackgroundThreadFactory;
import org.selfip.bkimmel.jnlp.PersistenceCheckedCache;
import org.selfip.bkimmel.jobs.Job;
import org.selfip.bkimmel.progress.ProgressPanel;
import org.selfip.bkimmel.rmi.ClassLoaderService;
import org.selfip.bkimmel.rmi.RMIClassLoaderStrategy;
import org.selfip.bkimmel.util.CheckedCacheClassLoaderStrategy;
import org.selfip.bkimmel.util.classloader.ClassLoaderStrategy;
import org.selfip.bkimmel.util.classloader.StrategyClassLoader;

/**
 * @author brad
 *
 */
public final class CopyOfWorkerClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new RMISecurityManager());
//        }

		String host = args.length > 0 ? args[0] : "localhost";
		JDialog dialog = new JDialog();
		ProgressPanel monitor = new ProgressPanel();

		int numberOfCpus = Runtime.getRuntime().availableProcessors();
		Executor threadPool = Executors.newFixedThreadPool(numberOfCpus, new BackgroundThreadFactory());
		Job workerJob = new ThreadServiceWorkerJob(host, 10000, numberOfCpus, threadPool);

		monitor.setRootVisible(false);

		dialog.add(monitor);
		dialog.setBounds(0, 0, 400, 300);
		dialog.setVisible(true);

		ClassLoaderStrategy strategy = null;
		try {
			strategy = new CheckedCacheClassLoaderStrategy(
					new PersistenceCheckedCache(),
					new CheckedCacheClassLoaderStrategy.DigestLookup() {

						public byte[] getDigest(String name) {
							return null;
						}

					}, new RMIClassLoaderStrategy());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {
			try {

				ClassLoader loader = new StrategyClassLoader(strategy);

				Class<?> cl = loader
						.loadClass("org.jmist.framework.services.Test");
				Object obj = cl.newInstance();

				Method method = cl.getMethod("test", null);

				method.invoke(obj, null);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				JDialog dialog1 = new JDialog();
				StringWriter msg = new StringWriter();
				PrintWriter w = new PrintWriter(msg);
				e.printStackTrace(w);
				w.flush();
				JTextArea label = new JTextArea(msg.toString());
				dialog1.add(label);
				dialog1.setBounds(0, 0, 800, 600);
				dialog1.setVisible(true);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dialog1.setVisible(false);
			}
		}
		//workerJob.go(monitor);

	}

}
