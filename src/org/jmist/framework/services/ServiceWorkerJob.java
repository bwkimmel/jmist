/**
 *
 */
package org.jmist.framework.services;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import org.jmist.framework.IJob;
import org.jmist.framework.IProgressMonitor;
import org.jmist.framework.ITaskWorker;
import org.jmist.framework.TaskDescription;

/**
 * A job that submits a parallelizable job to a remote
 * <code>JobServiceMaster<code>.
 * @author bkimmel
 */
public final class ServiceWorkerJob implements IJob {

	/**
	 * Initializes the address of the master and the amount of time to idle
	 * when no task is available.
	 * @param masterHost The URL of the master.
	 * @param idleTime The time (in milliseconds) to idle when no task is
	 * 		available.
	 */
	public ServiceWorkerJob(String masterHost, long idleTime) {
		this.masterHost = masterHost;
		this.idleTime = idleTime;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJob#go(org.jmist.framework.IProgressMonitor)
	 */
	@Override
	public void go(IProgressMonitor monitor) {

		try {

			monitor.notifyIndeterminantProgress();
			monitor.notifyStatusChanged("Looking up master...");

			Registry registry = LocateRegistry.getRegistry(this.masterHost);
			IJobMasterService service = (IJobMasterService) registry.lookup("IJobMasterService");
			ITaskWorker worker = null;
			UUID jobId = new UUID(0, 0);

			while (monitor.notifyIndeterminantProgress()) {

				monitor.notifyStatusChanged("Requesting task...");

				TaskDescription taskDesc = service.requestTask();

				if (taskDesc != null) {

					if (jobId != taskDesc.getJobId()) {
						worker = service.getTaskWorker(taskDesc.getJobId());
						jobId = taskDesc.getJobId();
					}

					assert(worker != null);

					monitor.notifyStatusChanged("Performing task...");
					Object results = worker.performTask(taskDesc.getTask(), monitor);

					monitor.notifyStatusChanged("Submitting task results...");
					service.submitTaskResults(jobId, taskDesc.getTaskId(), results);

				} else { /* taskDesc == null */

					monitor.notifyStatusChanged("Idling...");

					try {
						Thread.sleep(this.idleTime);
					} catch (InterruptedException e) {
						// continue.
					}

				}

			}

			monitor.notifyStatusChanged("Cancelled.");

		} catch (Exception e) {

			monitor.notifyStatusChanged("Exception: " + e.toString());

			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

		} finally {

			monitor.notifyCancelled();

		}

	}

	/** The URL of the master. */
	private final String masterHost;

	/**
	 * The amount of time (in milliseconds) to idle when no task is available.
	 */
	private final long idleTime;

}
