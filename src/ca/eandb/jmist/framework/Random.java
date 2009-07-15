/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

/**
 * A random number generator.
 * @author Brad Kimmel
 */
public interface Random extends Serializable {

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

	/**
	 * A default implementation of <code>Random</code>.
	 */
	public static final Random DEFAULT = new Random() {

		/**
		 * Serialization version ID.
		 */
		private static final long serialVersionUID = -6299190146689205359L;

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Random#createCompatibleRandom()
		 */
		public Random createCompatibleRandom() {
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Random#next()
		 */
		public double next() {
			return Math.random();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Random#reset()
		 */
		public void reset() {
			/* nothing to do. */
		}

	};

}
