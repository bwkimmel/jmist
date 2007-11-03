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
	public boolean go(ProgressMonitor monitor) {

		/* Get the task worker. */
		TaskWorker		worker			= this.worker();
		int			 	taskNumber		= 0;

		/* Check to see if the process has been cancelled. */
		monitor.notifyIndeterminantProgress();

		/* Main loop. */
		while (!monitor.isCancelPending()) {

			/* Get the next task and create a progress monitor to monitor it. */
			String				taskDesc		= String.format("Task %d", ++taskNumber);
			ProgressMonitor		taskMonitor		= monitor.createChildProgressMonitor(taskDesc);
			Object				task			= this.getNextTask();

			/* If there is no next task, then we're done. */
			if (task == null) {
				monitor.notifyComplete();
				return true;
			}

			/* Perform the task. */
			Object				results			= worker.performTask(task, taskMonitor);

			/* If the task was cancelled, then cancel the job. */
			if (taskMonitor.isCancelPending()) {
				break;
			}

			/* Submit the task results. */
			this.submitTaskResults(task, results, monitor);

		}

		/* If we get to this point, then the job was cancelled. */
		monitor.notifyCancelled();
		return false;

	}

}
