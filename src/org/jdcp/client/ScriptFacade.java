/**
 *
 */
package org.jdcp.client;

import java.util.UUID;

import org.jdcp.job.ParallelizableJob;
import org.selfip.bkimmel.rmi.Serialized;

/**
 * @author brad
 *
 */
public final class ScriptFacade {

	private final Configuration config;

	public ScriptFacade(Configuration config) {
		this.config = config;
	}

	public void setIdleTime(int seconds) throws Exception {
		config.getJobService().setIdleTime(seconds);
	}

	public void setJobPriority(UUID jobId, int priority) throws Exception {
		config.getJobService().setJobPriority(jobId, priority);
	}

	public void cancelJob(UUID jobId) throws Exception {
		config.getJobService().cancelJob(jobId);
	}

	public UUID submitJob(ParallelizableJob job) throws Exception {
		return submitJob(job, job.getClass().getSimpleName());
	}

	public UUID submitJob(ParallelizableJob job, String description) throws Exception {
		return config.getJobService().submitJob(
				new Serialized<ParallelizableJob>(job), description);
	}

	public void cancelJob(String jobId) throws Exception {
		cancelJob(UUID.fromString(jobId));
	}




}
