/**
 *
 */
package org.jmist.framework.reporting;

/**
 * A dummy progress monitor that does not report the progress to
 * anything.  This class is a singleton.
 * @author bkimmel
 */
public final class DummyProgressMonitor implements ProgressMonitor {

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyCancelled()
	 */
	@Override
	public void notifyCancelled() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyComplete()
	 */
	@Override
	public void notifyComplete() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyProgress(int, int)
	 */
	@Override
	public boolean notifyProgress(int value, int maximum) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyProgress(double)
	 */
	@Override
	public boolean notifyProgress(double progress) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyIndeterminantProgress()
	 */
	@Override
	public boolean notifyIndeterminantProgress() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	@Override
	public void notifyStatusChanged(String status) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#isCancelPending()
	 */
	@Override
	public boolean isCancelPending() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#createChildProgressMonitor()
	 */
	@Override
	public ProgressMonitor createChildProgressMonitor(String title) {

		/* Report progress to subtasks to the same monitor. */
		return this;

	}

	/**
	 * Creates a new <code>DummyProgressMonitor</code>.  This constructor is
	 * private because this class is a singleton.
	 */
	private DummyProgressMonitor() {
		// nothing to do.
	}

	/**
	 * Gets the single instance of <code>DummyProgressMonitor</code>.
	 * @return The single instance of <code>DummyProgressMonitor</code>.
	 */
	public static DummyProgressMonitor getInstance() {

		if (instance == null) {
			instance = new DummyProgressMonitor();
		}

		return instance;

	}

	/** The single instance of <code>DummyProgressMonitor</code>. */
	private static DummyProgressMonitor instance = null;

}