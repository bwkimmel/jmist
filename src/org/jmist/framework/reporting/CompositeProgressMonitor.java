/**
 *
 */
package org.jmist.framework.reporting;

import java.util.Collection;
import java.util.HashSet;

/**
 * A <code>ProgressMonitor</code> which aggregates individual progress
 * monitors.  Notifications are copied to each of the individual progress
 * monitors.  Cancellation is considered to be pending by this
 * <code>CompositeProgressMonitor</code> if cancellation is pending on any of
 * the individual <code>ProgressMonitor</code>s.  Child
 * <code>ProgressMonitor</code>s are created by aggregating child monitors
 * from each individual monitor.
 * @author bkimmel
 */
public final class CompositeProgressMonitor implements ProgressMonitor {

	/**
	 * Adds a <code>ProgressMonitor</code> to the collection.
	 * @param monitor The <code>ProgressMonitor</code> to add to the
	 * 		collection.
	 * @return This <code>CompositeProgressMonitor</code> is returns so that
	 * 		calls to this method may be chained.
	 */
	public CompositeProgressMonitor addProgressMonitor(ProgressMonitor monitor) {
		this.monitors.add(monitor);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#createChildProgressMonitor(java.lang.String)
	 */
	@Override
	public ProgressMonitor createChildProgressMonitor(String title) {

		CompositeProgressMonitor result = new CompositeProgressMonitor();

		/* Create a child progress monitor for each of this composite's
		 * monitors and add it to the resulting composite progress
		 * monitor.
		 */
		for (ProgressMonitor monitor : this.monitors) {

			ProgressMonitor child = monitor.createChildProgressMonitor(title);

			/* Don't bother adding dummy progress monitors. */
			if (!(child instanceof DummyProgressMonitor)) {
				result.addProgressMonitor(child);
			}

		}

		return result;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#isCancelPending()
	 */
	@Override
	public boolean isCancelPending() {

		for (ProgressMonitor monitor : this.monitors) {
			if (monitor.isCancelPending()) {
				return true;
			}
		}

		return false;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyCancelled()
	 */
	@Override
	public void notifyCancelled() {
		for (ProgressMonitor monitor : this.monitors) {
			monitor.notifyCancelled();
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyComplete()
	 */
	@Override
	public void notifyComplete() {
		for (ProgressMonitor monitor : this.monitors) {
			monitor.notifyComplete();
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyIndeterminantProgress()
	 */
	@Override
	public boolean notifyIndeterminantProgress() {

		boolean result = true;

		for (ProgressMonitor monitor : this.monitors) {
			if (!monitor.notifyIndeterminantProgress()) {
				result = false;
			}
		}

		return result;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyProgress(int, int)
	 */
	@Override
	public boolean notifyProgress(int value, int maximum) {

		boolean result = true;

		for (ProgressMonitor monitor : this.monitors) {
			if (!monitor.notifyProgress(value, maximum)) {
				result = false;
			}
		}

		return result;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyProgress(double)
	 */
	@Override
	public boolean notifyProgress(double progress) {

		boolean result = true;

		for (ProgressMonitor monitor : this.monitors) {
			if (!monitor.notifyProgress(progress)) {
				result = false;
			}
		}

		return result;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.reporting.ProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	@Override
	public void notifyStatusChanged(String status) {
		for (ProgressMonitor monitor : this.monitors) {
			monitor.notifyStatusChanged(status);
		}
	}

	/**
	 * The <code>Collection</code> of individual <code>ProgressMonitor</code>s
	 * that make up this <code>CompositeProgressMonitor</code>.
	 */
	private final Collection<ProgressMonitor> monitors = new HashSet<ProgressMonitor>();

}
