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
public interface TaskScheduler {

	void setJobPriority(UUID jobId, int priority);

	int add(UUID jobId, Object task);

	Object remove(UUID jobId, int taskId);

	TaskDescription getNextTask();

}
