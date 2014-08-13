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