/**
 *
 */
package org.jmist.framework.services;

import java.rmi.Remote;
import java.util.UUID;

import org.jmist.framework.IParallelizableJob;
import org.jmist.framework.ITaskWorker;
import org.jmist.framework.TaskDescription;

/**
 * A remote service for accepting <code>IParallelizableJob</code>s,
 * managing the distribution of tasks to workers, and aggregating the results
 * submitted by workers.
 * @author bkimmel
 */
public interface IJobMasterService extends Remote {

	/**
	 * Gets the task worker for a job.
	 * @param jobId The <code>UUID</code> of the job to obtain the task worker
	 * 		for.
	 * @return The <code>ITaskWorker</code> to use to process tasks for the job
	 * 		with the specified <code>UUID</code>, or <code>null</code> if that
	 * 		job is no longer available.
	 */
	ITaskWorker	getTaskWorker(UUID jobId);

	/**
	 * Gets a task to perform.
	 * @return A <code>TaskDescription</code> describing the task to be
	 * 		performed.
	 */
	TaskDescription requestTask();

	/**
	 * Submits the results of a task.
	 * @param jobId The <code>UUID</code> identifying the job for which the
	 * 		task was performed.
	 * @param taskId The ID of the task that was performed.
	 * @param results The results of the task.
	 */
	void submitTaskResults(UUID jobId, int taskId, Object results);

	/**
	 * Submits a new job to be processed.
	 * @param job The <code>IParallelizableJob</code> to be processed.
	 * @param priority The priority to assign to the job.
	 * @return The <code>UUID</code> assigned to the job, or <code>null</code>
	 * 		if the job was not accepted.
	 */
	UUID submitJob(IParallelizableJob job, int priority);

}
