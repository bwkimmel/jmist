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

			Registry registry = LocateRegistry.getRegistry(this.masterHost);
			IJobMasterService service = (IJobMasterService) registry.lookup("IJobMasterService");
			ITaskWorker worker = null;
			UUID jobId = new UUID(0, 0);

			while (true) {

				TaskDescription taskDesc = service.requestTask();

				if (taskDesc != null) {

					if (jobId != taskDesc.getJobId()) {
						worker = service.getTaskWorker(taskDesc.getJobId());
						jobId = taskDesc.getJobId();
					}

					assert(worker != null);

					Object results = worker.performTask(taskDesc.getTask(), monitor);

					service.submitTaskResults(jobId, taskDesc.getTaskId(), results);

				} else { /* taskDesc == null */

					try {
						Thread.sleep(this.idleTime);
					} catch (InterruptedException e) {
						// continue.
					}

				}

			}

		} catch (Exception e) {

			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

		}

	}

	private String masterHost;
	private long idleTime;

}
