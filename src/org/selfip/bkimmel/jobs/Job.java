/**
 *
 */
package org.selfip.bkimmel.jobs;

import org.selfip.bkimmel.progress.ProgressMonitor;

/**
 * Represents a job.
 * @author bkimmel
 */
public interface Job {

	/**
	 * Runs the job.
	 * @param monitor The <code>ProgressMonitor</code> to report the progress
	 * 		of the job to.
	 * @return A value indicating if the job was completed.
	 */
	boolean go(ProgressMonitor monitor);

}
