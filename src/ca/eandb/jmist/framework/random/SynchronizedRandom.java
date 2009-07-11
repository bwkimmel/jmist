/**
 *
 */
package ca.eandb.jmist.framework.random;

import ca.eandb.jmist.framework.Random;

/**
 * A <code>Random</code> decorator that provides synchronization.
 * @author Brad Kimmel
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
	 * @see ca.eandb.jmist.framework.Random#next()
	 */
	public synchronized double next() {
		return this.inner.next();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#reset()
	 */
	public synchronized void reset() {
		this.inner.reset();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#createCompatibleRandom()
	 */
	public SynchronizedRandom createCompatibleRandom() {
		return new SynchronizedRandom(this.inner.createCompatibleRandom());
	}

	/** The <code>Random</code> to be synchronized. */
	private final Random inner;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -6623891709591571067L;

}
