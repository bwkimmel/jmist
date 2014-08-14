/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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

import ca.eandb.jmist.framework.ProbabilityDensityFunction;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * A <code>ProbabilityDensityFunction</code> that always generates the
 * specified value.
 */
public final class DiracProbabilityDensityFunction implements
    ProbabilityDensityFunction {

  /** Serialization version ID. */
  private static final long serialVersionUID = -1453075946221988460L;

  /** The value to generate. */
  private final double value;

  /**
   * Create a new <code>DiracProbabilityDensityFunction</code>.
   * @param value The value to generate.
   */
  public DiracProbabilityDensityFunction(double value) {
    this.value = value;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#sample(ca.eandb.jmist.framework.Random)
   */
  @Override
  public double sample(Random random) {
    return value;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#warp(double)
   */
  @Override
  public double warp(double seed) {
    return value;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#evaluate(double)
   */
  @Override
  public double evaluate(double x) {
    return (x == value) ? Double.POSITIVE_INFINITY : 0.0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#sample(ca.eandb.jmist.framework.Random, double[])
   */
  @Override
  public double[] sample(Random random, double[] results) {
    return ArrayUtil.setAll(results, value);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#warp(double[], double[])
   */
  @Override
  public double[] warp(double[] seeds, double[] results) {
    return ArrayUtil.setAll(results, value);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#evaluate(double[], double[])
   */
  @Override
  public double[] evaluate(double[] x, double[] results) {
    for (int i = 0; i < x.length; i++) {
      results[i] = evaluate(x[i]);
    }
    return results;
  }

}
