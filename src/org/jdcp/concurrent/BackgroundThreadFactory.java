/**
 *
 */
package org.jdcp.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * A <code>ThreadFactory</code> that creates daemon threads of low priority.
 * @author bkimmel
 */
public final class BackgroundThreadFactory implements ThreadFactory {

	/**
	 * Creates a new <code>BackgroundThreadFactory</code>.
	 */
	public BackgroundThreadFactory() {
		this.inner = Executors.defaultThreadFactory();
	}

	/**
	 * Creates a new <code>BackgroundThreadFactory</code>.
	 * @param inner The <code>ThreadFactory</code> to use to create new
	 * 		threads (must not be null).
	 * @throws IllegalArgumentException if <code>inner</code> is null.
	 */
	public BackgroundThreadFactory(ThreadFactory inner) throws IllegalArgumentException {
		if (this.inner == null) {
			throw new IllegalArgumentException("inner must not be null.");
		}
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	public Thread newThread(Runnable r) {

		/* Use the inner ThreadFactory to create a new thread. */
		Thread thread = this.inner.newThread(r);

		/* Make the thread a daemon thread that runs at the lowest possible
		 * priority.
		 */
		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);

		return thread;

	}

	/** The thread factory to use to create new threads. */
	private final ThreadFactory inner;

}
