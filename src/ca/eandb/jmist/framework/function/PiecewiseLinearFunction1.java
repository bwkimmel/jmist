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
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.math.MathUtil;

/**
 * A piecewise linear <code>Function1</code>.
 * @author Brad Kimmel
 */
public final class PiecewiseLinearFunction1 implements Function1 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -4156156016195155933L;

	/** The array of domain points. */
	private final double[] xs;

	/** The array of values corresponding to the domain points. */
	private final double[] ys;

	/**
	 * Creates a new <code>PiecewiseLinearFunction1</code>.
	 * @param xs The array of domain points.
	 * @param ys The array of values corresponding to the domain points.
	 * 		The lengths of <code>xs</code> and <code>ys</code> must be equal.
	 * @throws IllegalArgumentException if <code>xs.length != ys.length</code>.
	 */
	public PiecewiseLinearFunction1(double[] xs, double[] ys) {
		if (xs.length != ys.length) {
			throw new IllegalArgumentException("xs.length != ys.length");
		}
		this.xs = xs.clone();
		this.ys = ys.clone();
	}
	
	/**
	 * Creates a new <code>PiecewiseLinearFunction1</code> by sampling another
	 * <code>Function1</code> at the specified domain points.
	 * @param f The <code>Function1</code> to sample.
	 * @param xs The array of domain points at which to sample <code>f</code>.
	 * 		The array must be ascending.
	 */
	private PiecewiseLinearFunction1(Function1 f, double[] xs) {
		this.xs = xs.clone();
		this.ys = new double[xs.length];
		for (int i = 0; i < xs.length; i++) {
			ys[i] = f.evaluate(xs[i]);
		}
	}
	
	/**
	 * Creates a new <code>PiecewiseLinearFunction1</code> by sampling another
	 * <code>Function1</code> at the specified domain points.
	 * @param f The <code>Function1</code> to sample.
	 * @param xs The array of domain points at which to sample <code>f</code>.
	 * 		The array must be ascending.
	 */
	public static PiecewiseLinearFunction1 sample(Function1 f, double[] xs) {
		return new PiecewiseLinearFunction1(f, xs);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	public double evaluate(double x) {
		return MathUtil.interpolate(xs, ys, x);
	}

}
