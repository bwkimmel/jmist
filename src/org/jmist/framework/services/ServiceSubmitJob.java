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
 * A job that submits an <code>IParallelizableJob</code> to submit to a remote
 * <code>JobMasterService</code>.
 * @author bkimmel
 */
public final class ServiceSubmitJob implements IJob {

	/**
	 * Initializes the job to submit, the priority to assign the job, and the
	 * URL of the master to submit the job to.
	 * @param job The <code>IParallelizableJob</code> to submit.
	 * @param priority The priority to assign the job.
	 * @param masterHost The URL of the remote <code>JobMasterService</code> to
	 * 		submit the job to.
	 */
	public ServiceSubmitJob(IParallelizableJob job, int priority, String masterHost) {
		this.job = job;
		this.priority = priority;
		this.masterHost = masterHost;
	}

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

	/**
	 * The URL of the remote <code>JobMasterService</code> to submit the job
	 * to.
	 */
	private final String masterHost;

	/** The <code>IParallelizableJob</code> to submit. */
	private final IParallelizableJob job;

	/** The priority to assign to the job. */
	private final int priority;

}
