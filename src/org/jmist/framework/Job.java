/**
 *
 */
package org.jmist.framework;

/**
 * Represents a job.
 * @author bkimmel
 */
public interface Job {

	/**
	 * Runs the job.
	 * @param monitor The <code>ProgressMonitor</code> to report the progress
	 * 		of the job to.
	 */
	void go(ProgressMonitor monitor);

}
