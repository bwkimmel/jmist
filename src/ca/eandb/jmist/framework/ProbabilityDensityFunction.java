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
package ca.eandb.jmist.framework;

import java.io.Serializable;

/**
 * A continuous probability density function from which random samples may be
 * generated.
 * @author Brad Kimmel
 */
public interface ProbabilityDensityFunction extends Serializable {

	/**
	 * Generates a random value distributed according to this
	 * <code>ProbabilityDensityFunction</code>.  Equivalent to
	 * <code>this.warp(RandomUtil.canonical())</code>.
	 * @param random The <code>Random</code> number generator to use.
	 * @return A random value distributed according to this
	 * 		<code>ProbabilityDensityFunction</code>.
	 * @see #warp(double)
	 * @see ca.eandb.jmist.framework.random.RandomUtil#canonical()
	 */
	double sample(Random random);

	/**
	 * Generates the domain value at which the cumulative density function
	 * evaluates to the specified value.
	 * @param seed The value of the cumulative density function.  This is
	 * 		expected to fall in [0, 1).  The results are undefined otherwise.
	 * @return C<sup>-1</sup>(<code>seed</code>), where C(x) is the integral
	 * 		from zero to x of P(t)dt, and P(t) is the value of this
	 * 		<code>ProbabilityDensityFunction</code> at t.
	 * @see #evaluate(double)
	 */
	double warp(double seed);

	/**
	 * Evaluates this probability density function at the specified point in
	 * the domain.
	 * @param x The point in the domain.
	 * @return The probability density of <code>x</code>.
	 */
	double evaluate(double x);

	/**
	 * Populates an array with random values sampled from this
	 * <code>ProbabilityDensityFunction</code>.
	 * @param random The <code>Random</code> number generator to use.
	 * @param results The array to populate (must not be null).
	 * @return A reference to <code>results</code>.
	 * @see #sample()
	 */
	double[] sample(Random random, double[] results);

	/**
	 * Generates the domain values at which the cumulative density function
	 * evaluates to the specified values.
	 * @param seeds The values of the cumulative density function (must not be
	 * 		null).  The values are expected to fall in [0, 1).  The results are
	 * 		undefined for seed values outside this range.
	 * @param results An optional preallocated array to populate with the
	 * 		results (if non-null, must be the same length as
	 * 		<code>seeds</code>).
	 * @return C<sup>-1</sup>(<code>seeds[i]</code>) for
	 * 		<code>0 &lt;= i &lt; seeds.length</code>, where C(x) is the
	 * 		integral from zero to x of P(t)dt, and P(t) is the value of this
	 * 		<code>ProbabilityDensityFunction</code> at t.
	 * @throws IllegalArgumentException if <code>results != null</code> and
	 * 		<code>results.length != seeds.length</code>.
	 * @see #evaluate(double)
	 * @see #warp(double)
	 */
	double[] warp(double[] seeds, double[] results);

	/**
	 * Evaluates this probability density function at the specified points in
	 * the domain.
	 * @param x The points in the domain (must not be null).
	 * @param results An optional preallocated array to populate with the
	 * 		results (if non-null, must be the same length as <code>x</code>).
	 * @return The probability densities of <code>x[i]</code> for
	 * 		<code>0 &lt;= i &lt; x.length</code>.
	 * @throws IllegalArgumentException if <code>results != null</code> and
	 * 		<code>results.length != x.length</code>.
	 * @see #evaluate(double)
	 */
	double[] evaluate(double[] x, double[] results);

}
