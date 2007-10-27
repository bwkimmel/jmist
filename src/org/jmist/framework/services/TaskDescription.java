/**
 *
 */
package org.jmist.framework.services;

import java.util.UUID;

/**
 * A description of a task assigned by an <code>IJobMasterService</code>.
 * @author bkimmel
 * @see {@link IJobMasterService#requestTask()}.
 */
public final class TaskDescription {

	/**
	 * Initializes the task description.
	 * @param jobId The <code>UUID</code> of the job that the task is for.
	 * @param taskId The ID of the task to be performed.
	 * @param task An <code>Object</code> describing the task to be performed.
	 * 		This should be passed to <code>ITaskWorker.performTask</code>.
	 * @see {@link org.jmist.framework.ITaskWorker#performTask(Object, org.jmist.framework.IProgressMonitor)}.
	 */
	public TaskDescription(UUID jobId, int taskId, Object task) {
		this.jobId = jobId;
		this.taskId = taskId;
		this.task = task;
	}

	/**
	 * Gets the <code>Object</code> describing the task to be performed.  This
	 * should be passed to <code>ITaskWorker.performTask</code> for the
	 * <code>ITaskWorker</code> corresponding to the job with the
	 * <code>UUID</code> given by {@link #getJobId()}.  The <code>ITaskWorker</code>
	 * may be obtained by calling {@link IJobMasterService#getTaskWorker(UUID)}.
	 * @return The <code>Object</code> describing the task to be performed.
	 * @see {@link #getJobId()},
	 * 		{@link org.jmist.framework.ITaskWorker#performTask(Object, org.jmist.framework.IProgressMonitor)},
	 * 		{@link IJobMasterService#getTaskWorker(UUID)}.
	 */
	public Object getTask() {
		return this.task;
	}

	/**
	 * Gets the <code>UUID</code> of the job whose <code>ITaskWorker</code>
	 * should perform this task.  Call {@link IJobMasterService#getTaskWorker(UUID)}
	 * to get the <code>ITaskWorker</code> to use to perform this task.
	 * @return The <code>UUID</code> of the job that this task is associated
	 * 		with.
	 * @see {@link IJobMasterService#getTaskWorker(UUID)}.
	 */
	public UUID getJobId() {
		return this.jobId;
	}

	/**
	 * The ID of the task to be performed.  This should be passed back to
	 * <code>IJobMasterService.submitTaskResults</code> when submitting the
	 * results of this task.
	 * @return The ID of the task to be performed.
	 * @see {@link IJobMasterService#submitTaskResults(UUID, int, Object)}.
	 */
	public int getTaskId() {
		return this.taskId;
	}

	/** The <code>UUID</code> of the job that this task is a part of. */
	private final UUID jobId;

	/** The ID of this task. */
	private final int taskId;

	/** The <code>Object</code> describing the task to be performed. */
	private final Object task;

}
