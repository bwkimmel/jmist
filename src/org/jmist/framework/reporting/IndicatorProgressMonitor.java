/**
 *
 */
package org.jmist.framework.reporting;

import org.jmist.packages.DummyProgressMonitor;

/**
 * A <code>ProgressMonitor</code> that uses a <code>ProgressIndicator</code> to
 * report progress.
 * @author bkimmel
 */
public final class IndicatorProgressMonitor implements ProgressMonitor {

	/**
	 * Creates a new <code>IndicatorProgressMonitor</code>.
	 * @param indicator The underlying <code>ProgressIndicator</code> to use.
	 */
	public IndicatorProgressMonitor(ProgressIndicator indicator) {
		this.indicator = indicator;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#createChildProgressMonitor()
	 */
	@Override
	public ProgressMonitor createChildProgressMonitor(String title) {

		ProgressIndicator childIndicator = this.indicator.addChildIndicator(title);
		return (childIndicator != null ? new IndicatorProgressMonitor(childIndicator) : new DummyProgressMonitor());

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyCancelled()
	 */
	@Override
	public void notifyCancelled() {

		ProgressIndicator parentIndicator = this.indicator.getParentIndicator();

		if (parentIndicator != null) {
			parentIndicator.removeChildIndicator(this.indicator);
		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyComplete()
	 */
	@Override
	public void notifyComplete() {

		ProgressIndicator parentIndicator = this.indicator.getParentIndicator();

		if (parentIndicator != null) {
			parentIndicator.removeChildIndicator(this.indicator);
		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyIndeterminantProgress()
	 */
	@Override
	public boolean notifyIndeterminantProgress() {
		this.indicator.setProgressIndeterminant();
		return !this.indicator.isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyProgress(int, int)
	 */
	@Override
	public boolean notifyProgress(int value, int maximum) {
		this.indicator.setProgress(value, maximum);
		return !this.indicator.isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyProgress(double)
	 */
	@Override
	public boolean notifyProgress(double progress) {
		this.indicator.setProgress(progress);
		return !this.indicator.isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	@Override
	public void notifyStatusChanged(String status) {
		this.indicator.setStatusText(status);
	}

	/** The underlying <code>ProgressIndicator</code> to use. */
	private final ProgressIndicator indicator;

}
