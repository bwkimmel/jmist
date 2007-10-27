/**
 *
 */
package org.jmist.framework;

import java.io.Serializable;

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
