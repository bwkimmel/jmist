/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.ParallelizableJob;
import org.jmist.framework.ProgressMonitor;
import org.jmist.framework.TaskWorker;

/**
 * A dummy parallelizable job to test remote method invocation.
 * @author bkimmel
 */
public final class DummyParallelizableJob implements ParallelizableJob {

	/**
	 * Initializes the number of tasks to serve.
	 * @param tasks The number of tasks to serve.
	 */
	public DummyParallelizableJob(int tasks) {
		this.tasks = tasks;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#getNextTask()
	 */
	@Override
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
	 * @see org.jmist.framework.ParallelizableJob#submitResults(java.lang.Object)
	 */
	@Override
	public void submitTaskResults(Object results) {

		int task = (Integer) results;
		System.out.printf("Received results for task %d.\n", task);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#worker()
	 */
	@Override
	public TaskWorker worker() {
		return this.worker;
	}

	/** The number of tasks to serve. */
	private final int tasks;

	/** The index of the next task to serve. */
	private int nextTask = 0;

	/**
	 * The task worker to use to process tasks.
	 */
	private final TaskWorker worker = new TaskWorker() {

		/*
		 * (non-Javadoc)
		 * @see org.jmist.framework.TaskWorker#performTask(java.lang.Object, org.jmist.framework.ProgressMonitor)
		 */
		@Override
		public Object performTask(Object task, ProgressMonitor monitor) {

			int value = (Integer) task;
			String msg = String.format("Processing task %d.", value);

			monitor.notifyStatusChanged(msg);
			System.out.println(msg);

			return value;

		}

		/** The serialization version ID. */
		private static final long serialVersionUID = -4687914341839279922L;

	};

}
