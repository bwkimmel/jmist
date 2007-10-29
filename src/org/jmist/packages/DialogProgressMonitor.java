/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.ProgressMonitor;
import org.jmist.toolkit.ui.ProgressDialog;

/**
 * A progress monitor that uses a progress dialog to indicate that progress
 * and status of the operation.
 * @author bkimmel
 */
public final class DialogProgressMonitor implements ProgressMonitor {

	/**
	 * Creates a new progress monitor that displays the progress on a dialog.
	 * @param parent The dialog's parent frame.
	 * @param modal A value indicating whether the dialog should be modal.
	 */
	public DialogProgressMonitor(javax.swing.JFrame parent, boolean modal) {
		this.progressDialog = new ProgressDialog(parent, modal);
	}

	/**
	 * Creates a new progress monitor that displays the progress on a dialog.
	 */
	public DialogProgressMonitor() {
		this.progressDialog = new ProgressDialog(new javax.swing.JFrame(), true);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyCancelled()
	 */
	public void notifyCancelled() {
		this.progressDialog.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyComplete()
	 */
	public void notifyComplete() {
		this.progressDialog.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyProgress(int, int)
	 */
	public boolean notifyProgress(int value, int maximum) {
		this.ensureInitialized();
		this.progressDialog.setProgress(value, maximum);
		return !this.progressDialog.isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyProgress(double)
	 */
	public boolean notifyProgress(double progress) {
		this.ensureInitialized();
		this.progressDialog.setProgress(progress);
		return !this.progressDialog.isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyIndeterminantProgress()
	 */
	@Override
	public boolean notifyIndeterminantProgress() {
		this.ensureInitialized();
		this.progressDialog.setProgressIndeterminant();
		return !this.progressDialog.isCancelPending();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	public void notifyStatusChanged(String status) {
		this.ensureInitialized();
		this.progressDialog.setStatusText(status);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProgressMonitor#createChildProgressMonitor()
	 */
	@Override
	public ProgressMonitor createChildProgressMonitor(String title) {
		return new DummyProgressMonitor();
	}

	/**
	 * Ensures that the progress dialog is visible.
	 */
	private synchronized void ensureInitialized() {

		if (!this.initialized) {

			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					progressDialog.setVisible(true);
				}
			});

			this.initialized = true;

		} // !this.initialized

	}

	/** The progress dialog. */
	private final ProgressDialog progressDialog;

	/** Indicates whether the progress monitor has been initialized. */
	private boolean initialized = false;

}
