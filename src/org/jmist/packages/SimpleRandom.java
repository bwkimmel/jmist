/**
 *
 */
package org.jmist.packages;

import java.util.Random;
import org.jmist.framework.IRandom;

/**
 * Adapts java.util.Random to the org.jmist.framework.IRandom interface
 * @author bkimmel
 * @see {@link java.util.Random}.
 */
public class SimpleRandom implements IRandom {

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.IRandom#next()
	 */
	public double next() {
		return rnd.nextDouble();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.IRandom#reset()
	 */
	public void reset() {
		// nothing to do.
	}

	/** The random number generator. */
	private final Random rnd = new Random();

}
