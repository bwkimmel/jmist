/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Random;

/**
 * Adapts java.util.Random to the org.jmist.framework.Random interface
 * @author bkimmel
 * @see {@link java.util.Random}.
 */
public class SimpleRandom implements Random {

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.Random#next()
	 */
	public double next() {
		return rnd.nextDouble();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.Random#reset()
	 */
	public void reset() {
		// nothing to do.
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Random#createCompatibleRandom()
	 */
	@Override
	public SimpleRandom createCompatibleRandom() {
		return this;
	}

	/** The random number generator. */
	private final java.util.Random rnd = new java.util.Random();

}
