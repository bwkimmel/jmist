/**
 *
 */
package org.jdcp.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import org.jdcp.job.ParallelizableJob;
import org.jdcp.job.TaskDescription;
import org.jdcp.job.TaskWorker;

/**
 * A remote service for accepting <code>ParallelizableJob</code>s,
 * managing the distribution of tasks to workers, and aggregating the results
 * submitted by workers.
 * @author bkimmel
 */
public interface JobMasterService extends Remote {

	/**
	 * Gets the task worker for a job.
	 * @param jobId The <code>UUID</code> of the job to obtain the task worker
	 * 		for.
	 * @return The <code>TaskWorker</code> to use to process tasks for the job
	 * 		with the specified <code>UUID</code>, or <code>null</code> if that
	 * 		job is no longer available.
	 */
	TaskWorker getTaskWorker(UUID jobId) throws RemoteException;

	/**
	 * Gets a task to perform.
	 * @return A <code>TaskDescription</code> describing the task to be
	 * 		performed.
	 */
	TaskDescription requestTask() throws RemoteException;

	/**
	 * Submits the results of a task.
	 * @param jobId The <code>UUID</code> identifying the job for which the
	 * 		task was performed.
	 * @param taskId The ID of the task that was performed.
	 * @param results The results of the task.
	 */
	void submitTaskResults(UUID jobId, int taskId, Object results) throws RemoteException;

	/**
	 * Submits a new job to be processed.
	 * @param job The <code>ParallelizableJob</code> to be processed.
	 * @param priority The priority to assign to the job.
	 * @return The <code>UUID</code> assigned to the job, or <code>null</code>
	 * 		if the job was not accepted.
	 */
	UUID submitJob(ParallelizableJob job, int priority) throws RemoteException;

	/**
	 * Sets the amount of time (in seconds) that workers should idle when there
	 * are no tasks to be performed.
	 * @param idleSeconds The amount of time (in seconds) that workers should
	 * 		idle when there are no tasks to be performed.
	 */
	void setIdleTime(int idleSeconds) throws IllegalArgumentException, RemoteException;

}
