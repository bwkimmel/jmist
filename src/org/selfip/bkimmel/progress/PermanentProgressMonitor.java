/**
 *
 */
package org.selfip.bkimmel.progress;

/**
 * A <code>ProgressMonitor</code> decorator that ignores complete and cancelled
 * notifications.
 * @author brad
 */
public final class PermanentProgressMonitor implements ProgressMonitor {

	/** The <code>ProgressMonitor</code> to decorate. */
	private final ProgressMonitor monitor;

	/**
	 * A value indicating whether permanency should be inherited by children of
	 * this <code>ProgressMonitor</code>.
	 */
	private final boolean inherit;

	/**
	 * Creates a new <code>PermanentProgressMonitor</code>.  Descendants of
	 * this <code>ProgressMonitor</code> will not be made permanent.
	 * @param monitor The <code>ProgressMonitor</code> to decorate.
	 */
	public PermanentProgressMonitor(ProgressMonitor monitor) {
		this(monitor, false);
	}

	/**
	 * Creates a new <code>PermanentProgressMonitor</code>.
	 * @param monitor The <code>ProgressMonitor</code> to decorate.
	 * @param inherit A value indicating whether descendants of this
	 * 		<code>ProgressMonitor</code> should be made permanent.
	 */
	public PermanentProgressMonitor(ProgressMonitor monitor, boolean inherit) {
		this.monitor = monitor;
		this.inherit = inherit;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.progress.ProgressMonitor#createChildProgressMonitor(java.lang.String)
	 */
	public ProgressMonitor createChildProgressMonitor(String title) {
		ProgressMonitor child = monitor.createChildProgressMonitor(title);
		return inherit ? new PermanentProgressMonitor(child, true) : child;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.progress.ProgressMonitor#isCancelPending()
	 */
	public boolean isCancelPending() {
		return monitor.isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.progress.ProgressMonitor#notifyCancelled()
	 */
	public void notifyCancelled() {
		/* This request is intentionally ignored. */
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.progress.ProgressMonitor#notifyComplete()
	 */
	public void notifyComplete() {
		/* This request is intentionally ignored. */
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.progress.ProgressMonitor#notifyIndeterminantProgress()
	 */
	public boolean notifyIndeterminantProgress() {
		return monitor.notifyIndeterminantProgress();
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.progress.ProgressMonitor#notifyProgress(int, int)
	 */
	public boolean notifyProgress(int value, int maximum) {
		return monitor.notifyProgress(value, maximum);
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.progress.ProgressMonitor#notifyProgress(double)
	 */
	public boolean notifyProgress(double progress) {
		return monitor.notifyProgress(progress);
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.progress.ProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	public void notifyStatusChanged(String status) {
		monitor.notifyStatusChanged(status);
	}

}
