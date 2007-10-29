/**
 *
 */
package org.jmist.framework.reporting;

/**
 * Something that reports progress on a long running operation.
 * @author bkimmel
 */
public interface ProgressIndicator {

	/**
	 * Adds a child progress indicator to report on the progress of a
	 * subtask (optional operation).
	 * @param title The title of the new progress indicator.
	 * @return The new <code>ProgressIndicator</code>, or <code>null</code> if
	 * 		the operation is not supported.
	 */
	ProgressIndicator addChildIndicator(String title);

	/**
	 * Removes a child progress indicator.
	 * @param child The child <code>ProgressIndicator</code> to remove.
	 */
	void removeChildIndicator(ProgressIndicator child);

	/**
	 * Gets the parent <code>ProgressIndicator</code> of this
	 * <code>ProgressIndicator</code>.
	 * @return The parent of this <code>ProgressIndicator</code>, or
	 * 		<code>null</code> if this is a root level
	 * 		<code>ProgressIndicator</code>.
	 */
	ProgressIndicator getParentIndicator();

	/**
	 * Sets the status of the operation.
	 * @param statusText A <code>String</code> describing the status of the
	 *	    operation.
	 */
	void setStatusText(String statusText);

	/**
	 * Sets the value of the progress indicator.
	 * @param progress The fraction of the operation that has been completed.
	 */
	void setProgress(double progress);

	/**
	 * Sets the value of the progress indicator.
	 * @param value The number of parts of the operation that have been
	 *	    completed.
	 * @param maximum The number of parts that compose the operation.
	 */
	void setProgress(int value, int maximum);

	/**
	 * Updates the progress indicator to indeterminant mode.
	 */
	void setProgressIndeterminant();

	/**
	 * Indicates if the cancel button was clicked.
	 */
	boolean isCancelPending();

}
