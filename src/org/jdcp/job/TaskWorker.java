/**
 *
 */
package org.jdcp.job;

import java.io.Serializable;

import org.selfip.bkimmel.progress.ProgressMonitor;

/**
 * Processes a task that is part of a parallelizable job.
 * @author bkimmel
 */
public interface TaskWorker extends Serializable {

	/**
	 * Process a task.
	 * @param task The <code>Object</code> describing the task to be performed.
	 * @param monitor The <code>ProgressMonitor</code> to report progress of
	 * 		the task to.
	 * @return The <code>Object</code> describing the results obtained from
	 * 		the execution of the task.
	 */
	Object performTask(Object task, ProgressMonitor monitor);

}
