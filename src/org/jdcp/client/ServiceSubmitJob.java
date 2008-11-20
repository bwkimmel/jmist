/**
 *
 */
package org.jdcp.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import org.jdcp.job.ParallelizableJob;
import org.jdcp.remote.JobService;
import org.selfip.bkimmel.jobs.Job;
import org.selfip.bkimmel.progress.ProgressMonitor;
import org.selfip.bkimmel.rmi.Serialized;

/**
 * A job that submits a <code>ParallelizableJob</code> to submit to a remote
 * <code>JobMasterServer</code>.
 * @author bkimmel
 */
public final class ServiceSubmitJob implements Job {

	/**
	 * Initializes the job to submit, the priority to assign the job, and the
	 * URL of the master to submit the job to.
	 * @param job The <code>ParallelizableJob</code> to submit.
	 * @param priority The priority to assign the job.
	 * @param masterHost The URL of the remote <code>JobMasterServer</code> to
	 * 		submit the job to.
	 */
	public ServiceSubmitJob(ParallelizableJob job, int priority, String masterHost) {
		this.job = job;
		this.priority = priority;
		this.masterHost = masterHost;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.ProgressMonitor)
	 */
	public boolean go(ProgressMonitor monitor) {

		try {

			monitor.notifyIndeterminantProgress();
			monitor.notifyStatusChanged("Submitting job...");

			Registry registry = LocateRegistry.getRegistry(this.masterHost);
			JobService service = (JobService) registry.lookup("JobMasterService");
			UUID jobId = service.submitJob(new Serialized<ParallelizableJob>(this.job), "no description");

			if (jobId != null) {

				monitor.notifyStatusChanged(String.format("Submitted, ID=%s.", jobId.toString()));
				monitor.notifyComplete();
				return true;

			} else {

				monitor.notifyStatusChanged("Failed to submit job.");
				monitor.notifyCancelled();
				return false;

			}

		} catch (Exception e) {

			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

			monitor.notifyStatusChanged(String.format("Failed to submit job: %s.", e.toString()));
			monitor.notifyCancelled();
			return false;

		}

	}

	/**
	 * The URL of the remote <code>JobMasterServer</code> to submit the job
	 * to.
	 */
	private final String masterHost;

	/** The <code>ParallelizableJob</code> to submit. */
	private final ParallelizableJob job;

	/** The priority to assign to the job. */
	private final int priority;

}
