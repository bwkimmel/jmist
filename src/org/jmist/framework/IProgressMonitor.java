package org.jmist.framework;

/**
 * Receives progress updates from a long running job, and potentially requests
 * that the job be cancelled.
 * @author bkimmel
 */
public interface IProgressMonitor {

	/**
	 * Notify the progress monitor that <code>value</code> of
	 * <code>maximum</code> subtasks have been completed.
	 * @param value The number of subtasks that have been completed.
	 * @param maximum The total number of subtasks.
	 * @return A value indicating whether the job should continue.
	 */
	boolean notifyProgress(int value, int maximum);

	/**
	 * Notify the progress monitor what fraction of the job has been completed.
	 * @param progress The fraction of the job that has been completed (should
	 * 		be in <code>[0, 1]</code>).
	 * @return A value indicating whether the job should continue.
	 */
	boolean notifyProgress(double progress);

	/**
	 * Notify the progress monitor that the progress is unknown or that the
	 * job runs indefinitely.
	 * @return A value indicating whether the job should continue.
	 */
	boolean notifyIndeterminantProgress();

	/**
	 * Notify the progress monitor that the job has been completed.
	 */
	void notifyComplete();

	/**
	 * Notify the progress monitor that the job has been cancelled.
	 */
	void notifyCancelled();

	/**
	 * Notify the progress monitor that the status of the job has changed.
	 * @param status The <code>String</code> describing the new status of the
	 * 		job.
	 */
	void notifyStatusChanged(String status);

	/**
	 * Creates an <code>IProgressMonitor</code> to monitor the progress of a
	 * subtask.
	 * @return A new child <code>IProgressMonitor</code>.
	 */
	IProgressMonitor createChildProgressMonitor();

}
