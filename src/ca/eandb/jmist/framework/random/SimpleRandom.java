/**
 *
 */
package ca.eandb.jmist.framework.random;

import ca.eandb.jmist.framework.Random;

/**
 * Adapts java.util.Random to the ca.eandb.jmist.framework.Random interface
 * @author Brad Kimmel
 * @see {@link java.util.Random}.
 */
public class SimpleRandom implements Random {

	/*
	 * (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#next()
	 */
	public double next() {
		return rnd.nextDouble();
	}

	/*
	 * (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#reset()
	 */
	public void reset() {
		// nothing to do.
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#createCompatibleRandom()
	 */
	public SimpleRandom createCompatibleRandom() {
		return this;
	}

	/** The random number generator. */
	private final java.util.Random rnd = new java.util.Random();

}
