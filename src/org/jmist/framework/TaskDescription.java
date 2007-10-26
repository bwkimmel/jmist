/**
 *
 */
package org.jmist.framework;

import java.util.UUID;

/**
 * @author bkimmel
 *
 */
public final class TaskDescription {

	public TaskDescription(UUID jobId, int taskId, Object task) {
		this.jobId = jobId;
		this.taskId = taskId;
		this.task = task;
	}

	public Object getTask() {
		return this.task;
	}

	public UUID getJobId() {
		return this.jobId;
	}

	public int getTaskId() {
		return this.taskId;
	}

	private final UUID jobId;
	private final int taskId;
	private final Object task;

}
