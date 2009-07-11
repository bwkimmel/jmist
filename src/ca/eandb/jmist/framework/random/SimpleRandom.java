/**
 *
 */
package ca.eandb.jmist.framework.random;

import ca.eandb.jmist.framework.Random;

/**
 * A <code>ca.eandb.jmist.framework.Random</code> that generates random numbers
 * using <code>{@link Math#random()}</code>.
 * @author Brad Kimmel
 * @see java.lang.Math#random()
 */
public class SimpleRandom implements Random {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 6964387117913110454L;

	/*
	 * (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#next()
	 */
	public double next() {
		return Math.random();
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

}
