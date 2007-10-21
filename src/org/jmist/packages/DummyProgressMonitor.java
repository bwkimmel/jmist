/**
 * 
 */
package org.jmist.packages;

import org.jmist.framework.IProgressMonitor;

/**
 * A dummy progress monitor that does not report the progress to
 * anything.
 * @author bkimmel
 */
public final class DummyProgressMonitor implements IProgressMonitor {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyCancelled()
	 */
	@Override
	public void notifyCancelled() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyComplete()
	 */
	@Override
	public void notifyComplete() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyProgress(int, int)
	 */
	@Override
	public boolean notifyProgress(int value, int maximum) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyProgress(double)
	 */
	@Override
	public boolean notifyProgress(double progress) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	@Override
	public void notifyStatusChanged(String status) {
		// do nothing
	}

}
