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
 * @author bkimmel
 *
 */
public interface IJobMasterService extends Remote {

	ITaskWorker	getTaskWorker(UUID jobId);

	TaskDescription requestTask();

	void submitTaskResults(UUID jobId, int taskId, Object results);

	UUID submitJob(IParallelizableJob job, int priority);

}
