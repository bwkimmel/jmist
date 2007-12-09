/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Random;

/**
 * A <code>Random</code> decorator that provides synchronization.
 * @author bkimmel
 */
public final class SynchronizedRandom implements Random {

	/**
	 * Creates a new <code>SynchronizedRandom</code>.
	 * @param inner The <code>Random</code> to synchronize.
	 */
	public SynchronizedRandom(Random inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Random#next()
	 */
	@Override
	public synchronized double next() {
		return this.inner.next();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Random#reset()
	 */
	@Override
	public synchronized void reset() {
		this.inner.reset();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Random#createCompatibleRandom()
	 */
	@Override
	public SynchronizedRandom createCompatibleRandom() {
		return new SynchronizedRandom(this.inner.createCompatibleRandom());
	}

	/** The <code>Random</code> to be synchronized. */
	private final Random inner;

}
