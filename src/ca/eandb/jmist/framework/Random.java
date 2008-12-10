/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * A random number generator.
 * @author Brad Kimmel
 */
public interface Random {

	/**
	 * Returns the next random number in the sequence.
	 * @return A random number in [0, 1).
	 */
	double next();

	/**
	 * Resets the sequence of random numbers.
	 */
	void reset();

	/**
	 * Creates a new <code>Random</code> with the same behavior as this
	 * <code>Random</code>.
	 * @return The new <code>Random</code>.
	 */
	Random createCompatibleRandom();

}
