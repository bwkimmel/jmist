/**
 *
 */
package org.jmist.packages.job;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jmist.framework.AbstractParallelizableJob;
import org.jmist.framework.TaskWorker;
import org.jmist.framework.reporting.ProgressMonitor;

/**
 * A dummy parallelizable job to test remote method invocation.
 * @author bkimmel
 */
public final class DummyParallelizableJob extends AbstractParallelizableJob
		implements Serializable {

	/**
	 * Initializes the number of tasks to serve and the amount of time to
	 * delay to simulate the processing of a task.
	 * @param tasks The number of tasks to serve.
	 * @param minSleepTime The minimum time (in milliseconds) to sleep to
	 * 		simulate the processing of a task.
	 * @param maxSleepTime The maximum time (in milliseconds) to sleep to
	 * 		simulate the processing of a task.
	 */
	public DummyParallelizableJob(int tasks, int minSleepTime, int maxSleepTime) {
		this.tasks = tasks;
		this.minSleepTime = minSleepTime;
		this.maxSleepTime = maxSleepTime;
		assert(minSleepTime <= maxSleepTime);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#getNextTask()
	 */
	public Object getNextTask() {

		if (this.nextTask < this.tasks) {

			System.out.printf("Task %d requested.\n", this.nextTask);
			return this.nextTask++;

		} else { /* this.nextTask >= this.tasks */

			System.out.println("No more tasks.");
			return null;

		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#submitResults(java.lang.Object, java.lang.Object, org.jmist.framework.reporting.ProgressMonitor)
	 */
	public void submitTaskResults(Object task, Object results, ProgressMonitor monitor) {

		int taskValue = (Integer) task;
		int resultValue = (Integer) results;
		System.out.printf("Received results for task %d: %d.\n", taskValue, resultValue);

		monitor.notifyProgress(++this.numResultsReceived, this.tasks);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#isComplete()
	 */
	public boolean isComplete() {
		return this.numResultsReceived >= this.tasks;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#writeJobResults(java.util.zip.ZipOutputStream)
	 */
	public void writeJobResults(ZipOutputStream stream) throws IOException {

		stream.putNextEntry(new ZipEntry("results.txt"));

		PrintStream results = new PrintStream(stream);
		results.printf("DummyParallelizableJob complete (%d tasks).\n", this.tasks);
		results.flush();

		stream.closeEntry();

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#worker()
	 */
	public TaskWorker worker() {
		return this.worker;
	}

	/**
	 * A random number generator.
	 */
	private static final java.util.Random random = new java.util.Random();

	/**
	 * The minimum time (in milliseconds) to sleep to simulate the processing
	 * of a task.
	 */
	private final int minSleepTime;

	/**
	 * The maximum time (in milliseconds) to sleep to simulate the processing
	 * of a task.
	 */
	private final int maxSleepTime;

	/** The number of tasks to serve. */
	private final int tasks;

	/** The index of the next task to serve. */
	private int nextTask = 0;

	/** The number of results that have been received. */
	private int numResultsReceived = 0;

	/**
	 * The task worker to use to process tasks.
	 */
	private final TaskWorker worker = new TaskWorker() {

		/*
		 * (non-Javadoc)
		 * @see org.jmist.framework.TaskWorker#performTask(java.lang.Object, org.jmist.framework.ProgressMonitor)
		 */
		public Object performTask(Object task, ProgressMonitor monitor) {

			int value = (Integer) task;
			String msg = String.format("Processing task %d.", value);

			monitor.notifyStatusChanged(msg);
			System.out.println(msg);

			int sleepTime = minSleepTime + random.nextInt(maxSleepTime - minSleepTime + 1);

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// nothing to do.
			}

			msg = String.format("Done task %d.", value);
			monitor.notifyStatusChanged(msg);
			System.out.println(msg);

			return value;

		}

		/** The serialization version ID. */
		private static final long serialVersionUID = -4687914341839279922L;

	};

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 4328712633325360415L;

}
