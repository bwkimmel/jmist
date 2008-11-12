/**
 *
 */
package org.selfip.bkimmel.progress;

/**
 * An abstract base class for <code>ProgressMonitor</code>s.
 * @author brad
 */
public abstract class AbstractProgressMonitor implements ProgressMonitor {

	/** Indicates if the operation has been cancelled. */
	private boolean cancelled = false;

	/** Indicates if the operation has been completed. */
	private boolean complete = false;

	/** The number of units of the operation that have been completed. */
	private int value = 0;

	/**
	 * The total number of units comprising this operation (0 for undefined).
	 */
	private int maximum = 0;

	/**
	 * The fraction of the operation completed (negative for indeterminant).
	 */
	private double progress = -1.0;

	/**
	 * A <code>String</code> describing the current status of the operation.
	 */
	private String status = "";

	/** A <code>String</code> describing the operation. */
	private final String title;

	/**
	 * Initializes this <code>ProgressMonitor</code> with no title.
	 */
	protected AbstractProgressMonitor() {
		this("");
	}

	/**
	 * Initializes this <code>ProgressMonitor</code>.
	 * @param title A <code>String</code> describing the operation.
	 */
	protected AbstractProgressMonitor(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#createChildProgressMonitor(java.lang.String)
	 */
	public ProgressMonitor createChildProgressMonitor(String title) {
		return DummyProgressMonitor.getInstance();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#isCancelPending()
	 */
	public boolean isCancelPending() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyCancelled()
	 */
	public void notifyCancelled() {
		cancelled = true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyComplete()
	 */
	public void notifyComplete() {
		complete = true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyIndeterminantProgress()
	 */
	public boolean notifyIndeterminantProgress() {
		value = 0;
		maximum = 0;
		progress = -1.0;
		return !isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyProgress(int, int)
	 */
	public boolean notifyProgress(int value, int maximum) {
		this.value = value;
		this.maximum = maximum;
		this.progress = (double) value / (double) maximum;
		return !isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyProgress(double)
	 */
	public boolean notifyProgress(double progress) {
		this.value = 0;
		this.maximum = 0;
		this.progress = progress;
		return !isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	public void notifyStatusChanged(String status) {
		this.status = status;
	}

	/**
	 * Gets the description of the operation.
	 * @return The title.
	 */
	protected String getTitle() {
		return title;
	}

	/**
	 * Gets a value indicating whether the operation has been cancelled.
	 * @return A value indicating whether the operation has been cancelled.
	 */
	protected boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Gets a value indicating whether the operation has been completed.
	 * @return A value indicating whether the operation has been completed.
	 */
	protected boolean isComplete() {
		return complete;
	}

	/**
	 * Gets the current status of the operation.
	 * @return A <code>String</code> describing the current status of the
	 * 		operation.
	 */
	protected String getStatus() {
		return status;
	}

	/**
	 * Gets a value indicating whether the progress is indeterminant.
	 * @return A value indicating whether the progress is indeterminant.
	 */
	protected boolean isIndeterminant() {
		return (progress >= 0.0);
	}

	/**
	 * Gets the current progress of the operation (negative for indeterminant).
	 * @return The current progress of the operation (negative for
	 * 		indeterminant).
	 */
	protected double getProgress() {
		return progress;
	}

	/**
	 * Gets the number of units of the operation that have been completed.
	 * @return The number of units of the operation that have been completed.
	 */
	protected int getValue() {
		return value;
	}

	/**
	 * Gets the total number of units that comprise the operation (or zero for
	 * undefined).
	 * @return The total number of units that comprise the operation.
	 */
	protected int getMaximum() {
		return maximum;
	}

}
