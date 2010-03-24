/**
 *
 */
package ca.eandb.jmist.framework.random;

import ca.eandb.jmist.framework.Random;

/**
 * A <code>ca.eandb.jmist.framework.Random</code> that generates random numbers
 * using a <code>java.util.Random</code>.
 * @author Brad Kimmel
 * @see java.util.Random
 */
public class SimpleRandom implements Random {

	/** Serialization version ID. */
	private static final long serialVersionUID = 1563928294146389596L;

	/** The random number generator to use. */
	private final java.util.Random inner = new java.util.Random();

	/*
	 * (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#next()
	 */
	public double next() {
		return inner.nextDouble();
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
		return new SimpleRandom();
	}

}
