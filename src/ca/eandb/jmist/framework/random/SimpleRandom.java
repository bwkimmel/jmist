/**
 *
 */
package ca.eandb.jmist.framework.random;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.math.RandomUtil;

/**
 * A <code>ca.eandb.jmist.framework.Random</code> that generates random numbers
 * using <code>{@link RandomUtil#canonical()}</code>.
 * @author Brad Kimmel
 * @see ca.eandb.jmist.math.RandomUtil#canonical()
 */
public class SimpleRandom implements Random {

	/*
	 * (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#next()
	 */
	public double next() {
		return RandomUtil.canonical();
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
