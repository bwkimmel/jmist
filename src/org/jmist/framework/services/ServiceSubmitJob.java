/**
 *
 */
package org.jmist.framework.services;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import org.jmist.framework.IJob;
import org.jmist.framework.IParallelizableJob;
import org.jmist.framework.IProgressMonitor;

/**
 * @author bkimmel
 *
 */
public final class ServiceSubmitJob implements IJob {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJob#go(org.jmist.framework.IProgressMonitor)
	 */
	@Override
	public void go(IProgressMonitor monitor) {

		try {

			monitor.notifyIndeterminantProgress();
			monitor.notifyStatusChanged("Submitting job...");

			Registry registry = LocateRegistry.getRegistry(this.masterHost);
			IJobMasterService service = (IJobMasterService) registry.lookup("IJobMasterService");
			UUID jobId = service.submitJob(this.job, this.priority);

			if (jobId.compareTo(new UUID(0, 0)) != 0) {

				monitor.notifyStatusChanged(String.format("Submitted, ID=%s.", jobId.toString()));
				monitor.notifyComplete();

			} else {

				monitor.notifyStatusChanged("Failed to submit job.");
				monitor.notifyCancelled();

			}

		} catch (Exception e) {

			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

			monitor.notifyStatusChanged(String.format("Failed to submit job: %s.", e.toString()));
			monitor.notifyCancelled();

		}

	}

	private String masterHost;
	private IParallelizableJob job;
	private int priority;

}
