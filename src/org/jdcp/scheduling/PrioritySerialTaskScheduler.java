/**
 *
 */
package org.jdcp.scheduling;

import java.util.UUID;

import org.jdcp.job.TaskDescription;

/**
 * @author brad
 *
 */
public final class PrioritySerialTaskScheduler implements TaskScheduler {

	/* (non-Javadoc)
	 * @see org.jdcp.scheduling.TaskScheduler#add(java.util.UUID, java.lang.Object)
	 */
	public int add(UUID jobId, Object task) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.scheduling.TaskScheduler#getNextTask()
	 */
	public TaskDescription getNextTask() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.scheduling.TaskScheduler#remove(java.util.UUID, int)
	 */
	public Object remove(UUID jobId, int taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.scheduling.TaskScheduler#setJobPriority(java.util.UUID, int)
	 */
	public void setJobPriority(UUID jobId, int priority) {
		// TODO Auto-generated method stub

	}

	public void removeJob(UUID jobId) {
		// TODO Auto-generated method stub

	}

}
