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
 * @author bkimmel
 *
 */
public final class ServiceWorkerJob implements IJob {

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

	private String masterHost;
	private long idleTime;

}
