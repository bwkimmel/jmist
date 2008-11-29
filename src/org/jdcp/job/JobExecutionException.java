/**
 *
 */
package org.jdcp.job;

/**
 * An <code>Exception</code> wrapper for exceptions thrown by a
 * <code>ParallelizableJob</code>.
 * @author brad
 * @see org.jdcp.job.ParallelizableJob
 */
public final class JobExecutionException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -8692323071682734864L;

	/**
	 * @param cause
	 */
	public JobExecutionException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JobExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

}
