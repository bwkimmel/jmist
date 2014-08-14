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
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;

/**
 * A piecewise linear <code>Function1</code> interpolating a sequence of points
 * spaced uniformly in the domain.
 *
 * @author Brad Kimmel
 */
public final class UniformPiecewiseLinearFunction1 implements Function1 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 816595096646372284L;

  /** The <code>Interval</code> within which values are specified. */
  private final Interval domain;

  /** The array of values to interpolate. */
  private final double[] values;

  /**
   * Creates a new <code>UniformPiecewiseLinearFunction1</code>.
   * @param domain The <code>Interval</code> within which values are
   *     specified.  It must be non-empty and finite.
   * @param values The array of function values.  There must be at least two
   *     values.
   * @throws IllegalArgumentException if <code>domain</code> is empty or
   *     infinite, or if <code>values.length &lt; 2</code>.
   * @see Interval#isEmpty()
   * @see Interval#isInfinite()
   */
  public UniformPiecewiseLinearFunction1(Interval domain, double[] values) {
    if (values.length < 2) {
      throw new IllegalArgumentException("values.length must be at least 2");
    }
    if (domain.isEmpty() || domain.isInfinite()) {
      throw new IllegalArgumentException("domain must be finite and non-empty");
    }
    this.domain = domain;
    this.values = values.clone();
  }

  /**
   * Creates a new <code>UniformPiecewiseLinearFunction1</code> by sampling
   * the provided <code>Function1</code> uniformly within the specified
   * domain.
   * @param f The <code>Function1</code> to sample.
   * @param domain The <code>Interval</code> within which to sample
   *     <code>f</code>.  This interval must be non-empty and finite.
   * @param count The number of sub-intervals to divide the <code>domain</code>
   *     into.  The function <code>f</code> will be sampled at
   *     <code>count + 1</code> points uniformly spaced within
   *     <code>domain</code>.
   * @return A new <code>UniformPiecewiseLinearFunction1</code>.
   */
  private UniformPiecewiseLinearFunction1(Function1 f, Interval domain, int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("count must be positive");
    }
    if (domain.isEmpty() || domain.isInfinite()) {
      throw new IllegalArgumentException("domain must be finite and non-empty");
    }
    this.domain = domain;
    this.values = new double[count + 1];
    for (int i = 0; i <= count; i++) {
      double x = domain.interpolate((double) i / (double) count);
      values[i] = f.evaluate(x);
    }
  }

  /**
   * Creates a new <code>UniformPiecewiseLinearFunction1</code> by sampling
   * the provided <code>Function1</code> uniformly within the specified
   * domain.
   * @param f The <code>Function1</code> to sample.
   * @param domain The <code>Interval</code> within which to sample
   *     <code>f</code>.  This interval must be non-empty and finite.
   * @param count The number of sub-intervals to divide the <code>domain</code>
   *     into.  The function <code>f</code> will be sampled at
   *     <code>count + 1</code> points uniformly spaced within
   *     <code>domain</code>.
   * @return A new <code>UniformPiecewiseLinearFunction1</code>.
   */
  public static UniformPiecewiseLinearFunction1 sample(Function1 f, Interval domain, int count) {
    return new UniformPiecewiseLinearFunction1(f, domain, count);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Function1#evaluate(double)
   */
  @Override
  public double evaluate(double x) {
    return MathUtil.interpolate(domain.minimum(), domain.maximum(), values, x);
  }

}
