/**
 *
 */
package org.jmist.framework;

/**
 * Represents a job.
 * @author bkimmel
 */
public interface IJob {

	/**
	 * Runs the job.
	 * @param monitor The <code>IProgressMonitor</code> to report the progress
	 * 		of the job to.
	 */
	void go(IProgressMonitor monitor);

}
