/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.ProgressMonitor;

/**
 * A dummy progress monitor that does not report the progress to
 * anything.
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
	 * @see org.jmist.framework.ProgressMonitor#createChildProgressMonitor()
	 */
	@Override
	public ProgressMonitor createChildProgressMonitor(String title) {

		/* Report progress to subtasks to the same monitor. */
		return this;

	}

}
