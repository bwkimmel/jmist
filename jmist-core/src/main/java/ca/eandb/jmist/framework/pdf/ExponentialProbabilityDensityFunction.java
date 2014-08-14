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
package ca.eandb.jmist.framework.pdf;

/**
 * Represents the probability density function for an exponential random
 * variable.
 *
 * @author Brad Kimmel
 */
public final class ExponentialProbabilityDensityFunction extends
    AbstractProbabilityDensityFunction {

  /** Serialization version ID. */
  private static final long serialVersionUID = 2821787249029006451L;

  /** The parameter for an exponential random variable. */
  private final double lambda;

  /**
   * Creates a new <code>ExponentialProbabilityDensityFunction</code>.
   * @param lambda The parameter for the exponential random variable.  The
   *     distribution will be <code>p(x) = &lambda;e<sup>-&lambda;x</sup></code>.
   *     The expected value is <code>E[X] = 1/&lambda;</code>.
   */
  public ExponentialProbabilityDensityFunction(double lambda) {
    this.lambda = lambda;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#warp(double)
   */
  @Override
  public double warp(double seed) {
    return -Math.log(1.0 - seed) / lambda;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#evaluate(double)
   */
  @Override
  public double evaluate(double x) {
    return x > 0.0 ? lambda * Math.exp(-lambda * x) : 0.0;
  }

}
