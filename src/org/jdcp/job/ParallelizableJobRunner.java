/**
 *
 */
package org.jdcp.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.jdcp.concurrent.BackgroundThreadFactory;
import org.selfip.bkimmel.jobs.Job;
import org.selfip.bkimmel.progress.ProgressMonitor;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * A <code>Job</code> that runs a <code>ParallelizableJob</code> using multiple
 * threads.
 * @author bkimmel
 */
public final class ParallelizableJobRunner implements Job {

	/**
	 * Creates a new <code>ParallelizableJobRunner</code>.
	 * @param job The <code>ParallelizableJob</code> to run.
	 * @param workingDirectory The working directory for the job.
	 * @param executor The <code>Executor</code> to use to run worker threads.
	 * @param maxConcurrentWorkers The maximum number of concurrent tasks to
	 * 		process.
	 */
	public ParallelizableJobRunner(ParallelizableJob job, File workingDirectory, Executor executor, int maxConcurrentWorkers) {
		this.job = new JobExecutionWrapper(job);
		this.workingDirectory = workingDirectory;
		this.executor = executor;
		this.workerSlot = new Semaphore(maxConcurrentWorkers);
		this.maxConcurrentWorkers = maxConcurrentWorkers;
	}

	/**
	 * Creates a new <code>ParallelizableJobRunner</code>.
	 * @param job The <code>ParallelizableJob</code> to run.
	 * @param workingDirectory The working directory for the job.
	 * @param executor The <code>Executor</code> to use to run worker threads.
	 * @param maxConcurrentWorkers The maximum number of concurrent tasks to
	 * 		process.
	 */
	public ParallelizableJobRunner(ParallelizableJob job, String workingDirectory, Executor executor, int maxConcurrentWorkers) {
		this(job, new File(workingDirectory), executor, maxConcurrentWorkers);
	}

	/**
	 * Creates a new <code>ParallelizableJobRunner</code>.
	 * @param job The <code>ParallelizableJob</code> to run.
	 * @param workingDirectory The working directory for the job.
	 * @param maxConcurrentWorkers The maximum number of concurrent tasks to
	 * 		process.
	 */
	public ParallelizableJobRunner(ParallelizableJob job, File workingDirectory, int maxConcurrentWorkers) {
		this(job, workingDirectory, Executors.newFixedThreadPool(maxConcurrentWorkers, new BackgroundThreadFactory()), maxConcurrentWorkers);
	}

	/**
	 * Creates a new <code>ParallelizableJobRunner</code>.
	 * @param job The <code>ParallelizableJob</code> to run.
	 * @param workingDirectory The working directory for the job.
	 * @param maxConcurrentWorkers The maximum number of concurrent tasks to
	 * 		process.
	 */
	public ParallelizableJobRunner(ParallelizableJob job, String workingDirectory, int maxConcurrentWorkers) {
		this(job, new File(workingDirectory), maxConcurrentWorkers);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.reporting.ProgressMonitor)
	 */
	public synchronized boolean go(final ProgressMonitor monitor) {

		int taskNumber = 0;
		boolean complete = false;


		try {

			this.monitor = monitor;
			TaskWorker taskWorker = job.worker();
			this.job.initialize(host);

			/* Task loop. */
			while (!this.monitor.isCancelPending()) {

				try {

					/* Get the next task to run.  If there are no further tasks,
					 * then wait for the remaining tasks to finish.
					 */
					Object task = this.job.getNextTask();
					if (task == null) {
						this.workerSlot.acquire(this.maxConcurrentWorkers);
						complete = true;
						break;
					}

					/* Create a worker and process the task. */
					String workerTitle = String.format("Worker (%d)", taskNumber);
					Worker worker = new Worker(taskWorker, task, monitor.createChildProgressMonitor(workerTitle));

					/* Acquire one of the slots for processing a task -- this
					 * limits the processing to the specified number of concurrent
					 * tasks.
					 */
					this.workerSlot.acquire();

					notifyStatusChanged(String.format("Starting worker %d", ++taskNumber));
					this.executor.execute(worker);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			this.job.finish();
			this.monitor = null;

		} catch (JobExecutionException e) {

			throw new RuntimeException(e);

		}

		if (!complete) {
			monitor.notifyCancelled();
		} else {
			monitor.notifyComplete();
		}

		return true;

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
	 * @throws JobExecutionException
	 */
	private void submitResults(Object task, Object results) throws JobExecutionException {
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
		public Worker(TaskWorker worker, Object task, ProgressMonitor monitor) {
			this.worker = worker;
			this.task = task;
			this.monitor = monitor;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				submitResults(task, worker.performTask(task, monitor));
			} catch (JobExecutionException e) {
				throw new RuntimeException(e);
			} finally {
				workerSlot.release();
			}
		}

		/** The <code>ProgressMonitor</code> to report progress to. */
		private final ProgressMonitor monitor;

		/** An <code>Object</code> describing the task to be processed. */
		private final Object task;

		/** The <code>TaskWorker</code> to use to perform the task. */
		private final TaskWorker worker;

	}

	private final Host host = new Host() {

		@Override
		public FileOutputStream createFileOutputStream(String path) {
			File file = new File(workingDirectory, path);
			File directory = file.getParentFile();
			directory.mkdirs();
			try {
				return new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				throw new UnexpectedException(e);
			}
		}

		@Override
		public RandomAccessFile createRandomAccessFile(String path) {
			File file = new File(workingDirectory, path);
			File directory = file.getParentFile();
			directory.mkdirs();
			try {
				return new RandomAccessFile(file, "rw");
			} catch (FileNotFoundException e) {
				throw new UnexpectedException(e);
			}
		}

	};

	/** The <code>ProgressMonitor</code> to report progress to. */
	private ProgressMonitor monitor = null;

	/** The <code>ParallelizableJob</code> to be run. */
	private final JobExecutionWrapper job;

	/** The working directory for this job. */
	private final File workingDirectory;

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
