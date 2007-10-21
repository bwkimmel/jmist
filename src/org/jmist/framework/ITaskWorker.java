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
	 * @param task The <code>InputStream</code> to read a description of the
	 * 		task from.
	 * @param results The <code>OutputStream</code> to write the results to.
	 * @param monitor The <code>IProgressMonitor</code> to report progress of
	 * 		the task to.
	 */
	void performTask(InputStream task, OutputStream results, IProgressMonitor monitor);

}
