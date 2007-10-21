/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IProgressMonitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * @author bkimmel
 *
 */
public final class SwingProgressMonitor implements IProgressMonitor {

	public SwingProgressMonitor() {

	}

	public SwingProgressMonitor(String title) {
		this.frame.setTitle(title);
	}

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

		this.progressBar.setIndeterminate(false);

		if (this.progressBar.getMaximum() != maximum) {
			this.progressBar.setMaximum(maximum);
		}

		this.progressBar.setValue(value);
		return !this.isCancelPending();

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyProgress(double)
	 */
	public boolean notifyProgress(double progress) {
		return this.notifyProgress((int) Math.floor(100.0 * progress), 100);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	public void notifyStatusChanged(String status) {
		this.ensureInitialized();
		this.statusLabel.setText(status);
	}

	private void showWindow() {

		JButton cancelButton = new JButton();

		cancelButton.setText(CANCEL_BUTTON_TEXT);
		cancelButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setCancelPending();
					}
				}
		);

		this.progressBar.setIndeterminate(true);

		this.frame.getContentPane().add(this.statusLabel);
		this.frame.getContentPane().add(this.progressBar);
		this.frame.getContentPane().add(cancelButton);
		this.frame.pack();
		this.frame.setVisible(true);

	}

	private void ensureInitialized() {

		if (!this.initialized) {

			SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							showWindow();
						}
					}
			);

			this.initialized = true;

		} // !this.initialized

	}

	private synchronized void setCancelPending() {
		this.cancelPending = true;
	}

	private synchronized boolean isCancelPending() {
		return this.cancelPending;
	}

	private static final String		CANCEL_BUTTON_TEXT	= "Cancel";

	private final JFrame			frame				= new JFrame();
	private final JLabel			statusLabel			= new JLabel();
	private final JProgressBar		progressBar			= new JProgressBar();
	private boolean					cancelPending		= false;
	private boolean					initialized			= false;

}
