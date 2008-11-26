/**
 *
 */
package org.jdcp.scheduling;

import java.util.UUID;

import org.jdcp.job.TaskDescription;

/**
 * Represents an object that is responsible for determine in what order to
 * serve tasks to worker clients.
 * @author brad
 */
public interface TaskScheduler {

	/**
	 * Sets the priority of a job.  The job priority is a hint to the scheduler
	 * which it may use to determine the order in which tasks are served.
	 * @param jobId The <code>UUID</code> identifying the job for which to set
	 * 		the priority.
	 * @param priority The priority to set for the specified job.
	 */
	void setJobPriority(UUID jobId, int priority);

	/**
	 * Adds a task to be scheduled.
	 * @param jobId The <code>UUID</code> identifying the job associated with
	 * 		the provided task.
	 * @param task An <code>Object</code> describing the task to be performed.
	 * 		This should have been obtained via a call to
	 * 		{@link org.jdcp.job.ParallelizableJob#getNextTask()}.
	 * @return An identifier for the newly scheduled task.
	 * @see org.jdcp.job.ParallelizableJob#getNextTask()
	 */
	int add(UUID jobId, Object task);

	/**
	 * Removes a task from the schedule.
	 * @param jobId The <code>UUID</code> identifying the job associated with
	 * 		the task to be removed.
	 * @param taskId The identifier for the task to be removed, which was
	 * 		obtained when the task was added ({@link #add(UUID, Object)}.
	 * @return The <code>Object</code> describing the specified task.
	 * @see #add(UUID, Object)
	 */
	Object remove(UUID jobId, int taskId);

	/**
	 * Gets the next task to be served.
	 * @return A <code>TaskDescription</code> describing the next task to be
	 * 		served.
	 * @see org.jdcp.job.TaskDescription
	 */
	TaskDescription getNextTask();

	/**
	 * Removes all tasks from the schedule that are associated with the
	 * specified job.
	 * @param jobId The <code>UUID</code> identifying the job for which all
	 * 		tasks are to be removed.
	 */
	void removeJob(UUID jobId);

}
