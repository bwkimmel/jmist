/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Random;

/**
 * A <code>Random</code> decorator that behaves as if each thread had a
 * separate instance.  For example, if used to decorate a
 * {@link StratifiedRandom}, the stratification would be done independently
 * on each thread.
 * @author bkimmel
 */
public final class ThreadLocalRandom implements Random {

	/**
	 * Creates a new <code>ThreadLocalRandom</code>.
	 * @param prototype The prototype
	 */
	public ThreadLocalRandom(final Random prototype) {

		this.random = new ThreadLocal<Random>() {

			/* (non-Javadoc)
			 * @see java.lang.ThreadLocal#initialValue()
			 */
			@Override
			protected Random initialValue() {
				return prototype.createCompatibleRandom();
			}

		};

		/* Don't bother creating a copy of the prototype for the thread on
		 * which this ThreadLocalRandom instance was created.
		 */
		this.random.set(prototype);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Random#next()
	 */
	@Override
	public double next() {
		return this.random.get().next();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Random#reset()
	 */
	@Override
	public void reset() {
		this.random.get().reset();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Random#createCompatibleRandom()
	 */
	@Override
	public ThreadLocalRandom createCompatibleRandom() {
		return new ThreadLocalRandom(this.random.get().createCompatibleRandom());
	}

	/** The inner random number generator. */
	private final ThreadLocal<Random> random;

}