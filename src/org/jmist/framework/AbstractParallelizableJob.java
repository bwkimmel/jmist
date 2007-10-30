/**
 *
 */
package org.jmist.framework;

import org.jmist.framework.reporting.ProgressMonitor;

/**
 * An abstract <code>ParallelizableJob</code> that provides a default
 * implementation for the <code>Job</code> interface.
 * @author bkimmel
 */
public abstract class AbstractParallelizableJob implements ParallelizableJob {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.reporting.ProgressMonitor)
	 */
	@Override
	public void go(ProgressMonitor monitor) {

		/* Get the task worker. */
		TaskWorker worker = this.worker();

		/* Main loop. */
		while (true) {

			/* Check to see if the process has been cancelled. */
			if (!monitor.notifyIndeterminantProgress()) {
				monitor.notifyCancelled();
				return;
			}

			/* Get the next task and create a progress monitor to monitor it. */
			ProgressMonitor		taskMonitor		= monitor.createChildProgressMonitor("Task");
			Object				task			= this.getNextTask();

			/* If there is no next task, then we're done. */
			if (task == null) {
				break;
			}

			/* Perform the task. */
			Object				results			= worker.performTask(task, taskMonitor);

			/* If we didn't get results, then the task was cancelled. */
			if (results == null) {
				monitor.notifyCancelled();
				return;
			}

			/* Submit the task results. */
			this.submitTaskResults(task, results);

		}

		monitor.notifyComplete();

	}

}
