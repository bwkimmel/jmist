/**
 *
 */
package org.jmist.packages;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.jmist.framework.Job;
import org.jmist.framework.ParallelizableJob;
import org.jmist.framework.TaskWorker;
import org.jmist.framework.reporting.ProgressMonitor;

/**
 * A <code>Job</code> that runs a <code>ParallelizableJob</code> using multiple
 * threads.
 * @author bkimmel
 */
public final class ParallelizableJobRunner implements Job {

	/**
	 * Creates a new <code>ParallelizableJobRunner</code>.
	 * @param job The <code>ParallelizableJob</code> to run.
	 * @param executor The <code>Executor</code> to use to run worker threads.
	 * @param maxConcurrentWorkers The maximum number of concurrent tasks to
	 * 		process.
	 */
	public ParallelizableJobRunner(ParallelizableJob job, Executor executor, int maxConcurrentWorkers) {
		this.job = job;
		this.worker = job.worker();
		this.executor = executor;
		this.workerSlot = new Semaphore(maxConcurrentWorkers, true);
		this.maxConcurrentWorkers = maxConcurrentWorkers;
	}

	/**
	 * Creates a new <code>ParallelizableJobRunner</code>.
	 * @param job The <code>ParallelizableJob</code> to run.
	 * @param maxConcurrentWorkers The maximum number of concurrent tasks to
	 * 		process.
	 */
	public ParallelizableJobRunner(ParallelizableJob job, int maxConcurrentWorkers) {
		this(job, Executors.newFixedThreadPool(maxConcurrentWorkers), maxConcurrentWorkers);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.reporting.ProgressMonitor)
	 */
	public synchronized boolean go(final ProgressMonitor monitor) {

		int taskNumber = 0;
		boolean complete = false;

		this.monitor = monitor;

		/* Task loop. */
		while (!this.monitor.isCancelPending()) {

			try {

				/* Acquire one of the slots for processing a task -- this
				 * limits the processing to the specified number of concurrent
				 * tasks.
				 */
				this.workerSlot.acquire();

				/* Get the next task to run.  If there are no further tasks,
				 * then wait for the remaining tasks to finish.
				 */
				Object task = this.job.getNextTask();
				if (task == null) {
					this.workerSlot.acquire(); //.acquire(this.maxConcurrentWorkers - 1);
					complete = true;
					break;
				}

				/* Create a worker and process the task. */
				String workerTitle = String.format("Worker (%d)", taskNumber);
				notifyStatusChanged(String.format("Starting worker %d", ++taskNumber));

				this.executor.execute(new Worker(task, monitor.createChildProgressMonitor(workerTitle)));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		this.monitor = null;

		if (!complete) {
			monitor.notifyCancelled();
		} else {
			monitor.notifyComplete();
		}

		return true;

	}

	/**
	 * Notifies the progress monitor that the job is being processed, but
	 * that the progress is unknown.
	 * @return A value indicating whether the job should continue.
	 */
	private boolean notifyIndeterminantProgress() {
		synchronized (monitor) {
			return this.monitor.notifyIndeterminantProgress();
		}
	}

	/**
	 * Notifies the progress monitor that the status has changed.
	 * @param status A <code>String</code> describing the status.
	 */
	private void notifyStatusChanged(String status) {
		synchronized (monitor) {
			this.monitor.notifyStatusChanged(status);
		}
	}

	/**
	 * Submits results for a task.
	 * @param task An <code>Object</code> describing the task for which results
	 * 		are being submitted.
	 * @param results An <code>Object</code> describing the results.
	 */
	private void submitResults(Object task, Object results) {
		synchronized (monitor) {
			this.job.submitTaskResults(task, results, monitor);
		}
	}

	/**
	 * Processes tasks for a <code>ParallelizableJob</code>.
	 * @author bkimmel
	 */
	private class Worker implements Runnable {

		/**
		 * Creates a new <code>Worker</code>.
		 * @param task An <code>Object</code> describing the task to be
		 * 		processed by the worker.
		 * @param monitor The <code>ProgressMonitor</code> to report progress
		 * 		to.
		 */
		public Worker(Object task, ProgressMonitor monitor) {
			this.task = task;
			this.monitor = monitor;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				submitResults(task, worker.performTask(task, monitor));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				workerSlot.release();
			}
		}

		/** The <code>ProgressMonitor</code> to report progress to. */
		private final ProgressMonitor monitor;

		/** An <code>Object</code> describing the task to be processed. */
		private final Object task;

	}

	/** The <code>ProgressMonitor</code> to report progress to. */
	private ProgressMonitor monitor = null;

	/** The <code>ParallelizableJob</code> to be run. */
	private final ParallelizableJob job;

	/** The <code>TaskWorker</code> to use to process tasks. */
	private final TaskWorker worker;

	/**
	 * The <code>Semaphore</code> to use to limit the number of concurrent
	 * threads.
	 */
	private final Semaphore workerSlot;

	/** The <code>Executor</code> to use to run worker threads. */
	private final Executor executor;

	/** The maximum number of concurrent tasks to process. */
	private final int maxConcurrentWorkers;

}
