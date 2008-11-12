/**
 *
 */
package org.jdcp.remote;

import java.rmi.RemoteException;
import java.util.UUID;

import org.jdcp.job.TaskDescription;
import org.jdcp.job.TaskWorker;

/**
 * @author brad
 *
 */
public interface TaskService {

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

}
