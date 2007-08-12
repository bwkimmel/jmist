/**
 *
 */
package org.jmist.framework;

/**
 * A random number generator.
 * @author bkimmel
 */
public interface IRandom {

	/**
	 * Returns the next random number in the sequence.
	 * @return A random number in [0, 1).
	 */
	double next();

	/**
	 * Resets the sequence of random numbers.
	 */
	void reset();

}
