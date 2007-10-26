/**
 *
 */
package org.jmist.framework;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Processes a task that is part of a parallelizable job.
 * @author bkimmel
 */
public interface ITaskWorker extends Serializable {

	/**
	 * Process a task.
	 * @param task The <code>Object</code> describing the task to be performed.
	 * @param monitor The <code>IProgressMonitor</code> to report progress of
	 * 		the task to.
	 * @return The <code>Object</code> describing the results obtained from
	 * 		the execution of the task.
	 */
	Object performTask(Object task, IProgressMonitor monitor);

}
