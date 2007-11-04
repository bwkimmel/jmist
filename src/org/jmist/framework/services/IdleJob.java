/**
 *
 */
package org.jmist.framework.services;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.jmist.framework.AbstractParallelizableJob;
import org.jmist.framework.TaskWorker;
import org.jmist.framework.reporting.ProgressMonitor;

/**
 * A <code>ParallelizableJob</code> whose <code>TaskWorker</code>s idle for a
 * specified amount of time.
 * @author bkimmel
 */
public final class IdleJob extends AbstractParallelizableJob {

	/**
	 * Creates a new <code>IdleJob</code>.
	 */
	public IdleJob() {
		/* nothing to do. */
	}

	/**
	 * Creates a new <code>IdleJob</code>.
	 * @param idleSeconds The amount of time (in seconds) that workers should
	 * 		idle (must be non-negative).
	 * @throws IllegalArgumentException if <code>idleSeconds</code> is
	 * 		negative.
	 */
	public IdleJob(int idleSeconds) throws IllegalArgumentException {
		if (idleSeconds < 0) {
			throw new IllegalArgumentException("idleSeconds must be non-negative.");
		}
		this.idleSeconds = idleSeconds;
	}

	/**
	 * Sets the amount of time (in seconds) that workers should idle.
	 * @param idleSeconds The amount of time (in seconds) that workers should
	 * 		idle (must be non-negative).
	 * @throws IllegalArgumentException if <code>idleSeconds</code> is
	 * 		negative.
	 */
	public void setIdleTime(int idleSeconds) throws IllegalArgumentException {
		if (idleSeconds < 0) {
			throw new IllegalArgumentException("idleSeconds must be non-negative.");
		}
		this.idleSeconds = idleSeconds;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#getNextTask()
	 */
	@Override
	public Object getNextTask() {
		return this.idleSeconds;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#isComplete()
	 */
	@Override
	public boolean isComplete() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, org.jmist.framework.reporting.ProgressMonitor)
	 */
	@Override
	public void submitTaskResults(Object task, Object results,
			ProgressMonitor monitor) {
		monitor.notifyIndeterminantProgress();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#worker()
	 */
	@Override
	public TaskWorker worker() {
		return worker;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#writeJobResults(java.util.zip.ZipOutputStream)
	 */
	@Override
	public void writeJobResults(ZipOutputStream stream) throws IOException {
		assert(false);
	}

	/**
	 * A <code>TaskWorker</code> that sleeps for the specified amount of time
	 * (in seconds), waking up each second to notify the
	 * <code>ProgressMonitor</code>.
	 * @author bkimmel
	 */
	private static class IdleTaskWorker implements TaskWorker {

		/* (non-Javadoc)
		 * @see org.jmist.framework.TaskWorker#performTask(java.lang.Object, org.jmist.framework.reporting.ProgressMonitor)
		 */
		@Override
		public Object performTask(Object task, ProgressMonitor monitor) {

			int seconds = (Integer) task;

			monitor.notifyStatusChanged("Idling...");

			for (int i = 0; i < seconds; i++) {

				if (!monitor.notifyProgress(i, seconds)) {
					monitor.notifyCancelled();
					return null;
				}

				this.sleep();

			}

			monitor.notifyProgress(seconds, seconds);
			monitor.notifyComplete();

			return null;

		}

		/**
		 * Sleeps for one second.
		 */
		private void sleep() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Serialization version ID.
		 */
		private static final long serialVersionUID = 4249862961124801480L;

	}

	/** The amount of time (in seconds) that workers should idle. */
	private int idleSeconds = DEFAULT_IDLE_SECONDS;

	/** The <code>TaskWorker</code> that idles for the given amount of time. */
	private static final TaskWorker worker = new IdleTaskWorker();

	/** The default amount of time (in seconds) that workers should idle. */
	private static final int DEFAULT_IDLE_SECONDS = 60;

}
