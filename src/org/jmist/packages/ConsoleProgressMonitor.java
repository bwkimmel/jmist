/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IProgressMonitor;

import java.io.PrintStream;

/**
 * A progress monitor that displays the status on the console.
 * @author bkimmel
 *
 */
public final class ConsoleProgressMonitor implements IProgressMonitor {

	/**
	 * Initializes the progress monitor to use a progress bar of the
	 * default length.
	 */
	public ConsoleProgressMonitor() {
		this.length = DEFAULT_PROGRESS_BAR_LENGTH;
	}

	/**
	 * Initializes the progress monitor to use a progress bar of the
	 * specified length.
	 * @param length The length of the progress bar.
	 */
	public ConsoleProgressMonitor(int length) {
		this.length = length;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyCancelled()
	 */
	public void notifyCancelled() {
		this.printProgressBar(this.progress);
		this.out.println(" CANCELLED");
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyComplete()
	 */
	public void notifyComplete() {
		this.printProgressBar(1.0);
		this.out.println(" DONE");
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyProgress(double)
	 */
	public boolean notifyProgress(double progress) {
		this.printProgressBar(progress);
		this.out.print("\r");
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyProgress(int, int)
	 */
	public boolean notifyProgress(int value, int maximum) {
		this.printProgressBar((double) value / (double) maximum);
		this.out.printf(" (%d/%d)\r", value, maximum);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IProgressMonitor#notifyStatusChanged(java.lang.String)
	 */
	public void notifyStatusChanged(String status) {
		this.out.println();
		this.out.println(status);
	}

	/**
	 * Writes the progress bar to the stream.
	 * @param progress
	 */
	private void printProgressBar(double progress) {

		double x;

		this.out.print(PROGRESS_BAR_END_CHAR);

		for (int i = 1; i <= this.length; i++) {
			x = (double) i / this.length;
			this.out.print(progress >= x ? PROGRESS_BAR_CHAR : PROGRESS_BACKGROUND_CHAR);
		}

		this.out.print(PROGRESS_BAR_END_CHAR);

		// Update the progress internally.
		this.progress = progress;

	}

	/** The stream to write the progress output to. */
	private final PrintStream out = System.out;

	/** The progress as last updated. */
	private double progress = 0.0;

	/** The length of the progress bar. */
	private final int length;

	/** The character to use for the progress bar. */
	private static final char PROGRESS_BAR_CHAR				= '=';

	/** The character to use for the background of the progress bar. */
	private static final char PROGRESS_BACKGROUND_CHAR		= ' ';

	/** The character to use to delimit the progress bar. */
	private static final char PROGRESS_BAR_END_CHAR			= '|';

	/** The default length of the progress bar. */
	private static final char DEFAULT_PROGRESS_BAR_LENGTH	= 40;

}
