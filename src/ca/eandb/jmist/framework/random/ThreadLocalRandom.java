/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.random;

import java.io.Serializable;

import ca.eandb.jmist.framework.Random;

/**
 * A <code>Random</code> decorator that behaves as if each thread had a
 * separate instance.  For example, if used to decorate a
 * {@link StratifiedRandom}, the stratification would be done independently
 * on each thread.
 * @author Brad Kimmel
 */
public final class ThreadLocalRandom implements Random {

	/**
	 * A thread local container for a random number generator.
	 * @author Brad Kimmel
	 */
	private final class Container extends ThreadLocal<Random> implements
			Serializable {

		/**
		 * Serialization version ID.
		 */
		private static final long serialVersionUID = 2870785400712895313L;

		/**
		 * The prototype <code>Random</code> from which to make thread-local
		 * copies.
		 */
		private final Random prototype;

		/**
		 * Initializes this thread local <code>Random</code> container.
		 * @param prototype The prototype <code>Random</code> from which to
		 * 		make thread-local copies.
		 */
		private Container(Random prototype) {
			this.prototype = prototype;
		}

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected Random initialValue() {
			return prototype.createCompatibleRandom();
		}

	}

	/**
	 * Creates a new <code>ThreadLocalRandom</code>.
	 * @param prototype The prototype
	 */
	public ThreadLocalRandom(final Random prototype) {

		this.random = new Container(prototype);

		/* Don't bother creating a copy of the prototype for the thread on
		 * which this ThreadLocalRandom instance was created.
		 */
		this.random.set(prototype);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#next()
	 */
	public double next() {
		return this.random.get().next();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#reset()
	 */
	public void reset() {
		this.random.get().reset();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#createCompatibleRandom()
	 */
	public ThreadLocalRandom createCompatibleRandom() {
		return new ThreadLocalRandom(this.random.get().createCompatibleRandom());
	}

	/** The inner random number generator. */
	private final ThreadLocal<Random> random;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4519791871156787526L;

}
