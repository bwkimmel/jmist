/**
 *
 */
package org.jmist.framework;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a job that can be split into smaller chunks.
 * @author bkimmel
 */
public interface IParallelizableJob {

	/**
	 * Writes a description of the next task to the specified
	 * output stream.
	 * @param task The stream to write the task description to.
	 * @return A value indicating whether a new task was obtained.
	 */
	boolean getNextTask(OutputStream task);

	/**
	 * Reads the results of a task from the specified input
	 * stream.
	 * @param results The input stream containing the results of
	 * 		a task.
	 */
	void submitResults(InputStream results);

	/**
	 * Gets the task worker to use to process the tasks of this
	 * job.
	 * @return The task worker to use to process the tasks of
	 * 		this job.
	 */
	ITaskWorker worker();

}
