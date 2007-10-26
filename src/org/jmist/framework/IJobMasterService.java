/**
 *
 */
package org.jmist.framework;

import java.rmi.Remote;
import java.util.UUID;

/**
 * @author bkimmel
 *
 */
public interface IJobMasterService extends Remote {

	ITaskWorker	getTaskWorker(UUID jobId);

	TaskDescription requestTask();

	void submitTaskResults(UUID jobId, int taskId, Object results);

	UUID submitJob(IParallelizableJob job, int priority);

}
