/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IProgressMonitor;

import javax.swing.*;

/**
 * @author bkimmel
 *
 */
public final class SwingProgressMonitor implements IProgressMonitor {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyCancelled()
	 */
	public void notifyCancelled() {
		this.ensureInitialized();

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyComplete()
	 */
	public void notifyComplete() {
		this.ensureInitialized();

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyProgress(int, int)
	 */
	public boolean notifyProgress(int value, int maximum) {

		this.ensureInitialized();

		if (this.progressBar.getMaximum() != maximum) {
			this.progressBar.setMaximum(maximum);
		}

		this.progressBar.setValue(value);
		return !this.cancelPending;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyProgress(double)
	 */
	public boolean notifyProgress(double progress) {
		return this.notifyProgress((int) Math.floor(100.0 * progress), 100);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyStatusChanged(java.lang.String, java.lang.String)
	 */
	public void notifyStatusChanged(String key, String value) {
		this.ensureInitialized();

	}

	private void ensureInitialized() {

		if (!this.initialized) {

			JFrame	frame	= new JFrame();

			frame.setTitle("Progress");
			frame.getContentPane().add(this.progressBar);

			frame.pack();
			frame.setVisible(true);

			this.initialized = true;

		}

	}

	private final JProgressBar progressBar = new JProgressBar();
	private boolean cancelPending = false;
	private boolean initialized = false;

}
