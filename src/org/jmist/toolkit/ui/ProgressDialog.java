/*
 * ProgressDialog.java
 *
 * Created on October 21, 2007, 3:34 PM
 */

package org.jmist.toolkit.ui;

/**
 * A dialog for indicating the progress of an operation.
 * @author  bkimmel
 */
public class ProgressDialog extends javax.swing.JDialog {

	/** Creates new form ProgressDialog */
	public ProgressDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		statusLabel = new javax.swing.JLabel();
		progressBar = new javax.swing.JProgressBar();
		cancelButton = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		statusLabel.setText("Progress...");

		cancelButton.setMnemonic('C');
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
					.addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
					.addComponent(cancelButton, javax.swing.GroupLayout.Alignment.TRAILING))
				.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(statusLabel)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(cancelButton)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Fires when the user clicks on the cancel button.
	 * @param evt The event arguments.
	 */
	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		this.onCancel();
	}//GEN-LAST:event_cancelButtonActionPerformed

	/**
	 * Initiates the process of cancelling the operation.
	 */
	private void onCancel() {
		this.setCancelPending();
	}

	/**
	 * Causes a cancellation to be pending.
	 */
	private synchronized void setCancelPending() {
		this.cancelPending = true;
	}

	/**
	 * Indicates if the cancel button was clicked.
	 */
	public synchronized boolean isCancelPending() {
		return this.cancelPending;
	}

	/**
	 * Sets the value of the progress bar.
	 * @param progress The fraction of the operation that has been completed.
	 */
	public void setProgress(double progress) {
		this.setProgressBarValue((int) Math.floor(100.0 * progress), 100);
		this.clearProgressText();
	}

	/**
	 * Sets the value of the progress bar.
	 * @param value The number of parts of the operation that have been
	 *	    completed.
	 * @param maximum The number of parts that compose the operation.
	 */
	public void setProgress(int value, int maximum) {
		this.setProgressBarValue(value, maximum);
		this.setProgressText(value, maximum);
	}

	/**
	 * Updates the progress bar on this dialog.
	 * @param value The number of parts of the operation that have been
	 *	    completed.
	 * @param maximum The number of parts that compose the operation.
	 */
	private void setProgressBarValue(int value, int maximum) {

		if (this.progressBar.getMaximum() != maximum) {
			this.progressBar.setMaximum(maximum);
		}

		this.progressBar.setValue(value);

	}

	/**
	 * Sets the status of the operation.
	 * @param statusText A <code>String</code> describing the status of the
	 *	    operation.
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
		this.updateStatusLabel();
	}

	/**
	 * Updates the text on the status label on this dialog.
	 */
	private void updateStatusLabel() {
		this.statusLabel.setText(this.statusText + " " + this.progressText);
	}

	/**
	 * Displays the progress as part of the status label on this dialog.
	 * @param value The number of parts of the operation that have been
	 *	    completed.
	 * @param maximum The number of parts that compose this operation.
	 */
	private void setProgressText(int value, int maximum) {
		this.progressText = String.format("(%d/%d)", value, maximum);
		this.updateStatusLabel();
	}

	/**
	 * Removes the text showing the progress from the status label on this
	 * dialog.
	 */
	private void clearProgressText() {
		if (!this.progressText.isEmpty()) {
			this.progressText = "";
			this.updateStatusLabel();
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton cancelButton;
	private javax.swing.JProgressBar progressBar;
	private javax.swing.JLabel statusLabel;
	// End of variables declaration//GEN-END:variables

	/** Indicates if the cancel button was clicked. */
	private boolean cancelPending = false;

	/** The text describing the status of the operation. */
	private String statusText = "Progress...";

	/** The text describing the progress of the operation. */
	private String progressText = "";

	/** Serialization version. */
	private static final long serialVersionUID = -8886817308462948089L;

}