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

/**
 * Static utility methods for working with <code>Function</code>s.
 * 
 * @author Brad Kimmel
 */
public final class FunctionUtil {
  
  /**
   * Applies a <code>Function1</code> to each value of an array in place.
   * @param f The <code>Function1</code> to apply.
   * @param x The values to apply the function to.
   * @return A reference to <code>x</code>.
   */
  public static double[] apply(Function1 f, double[] x) {
    for (int i = 0; i < x.length; i++) {
      x[i] = f.evaluate(x[i]);
    }
    return x;
  }
  
  /**
   * Returns a piecewise linear function defined by interpolating the
   * provided <code>Function1</code> at the specified domain points.
   * @param f The <code>Function1</code> to sample.
   * @param x The domain points at which to evaluate <code>f</code>.
   * @return A piecewise linear <code>Function1</code>.
   */
  public static Function1 sample(Function1 f, double[] x) {
    return PiecewiseLinearFunction1.sample(f, x);
  }
  
  /**
   * Returns a piecewise linear function defined by interpolating the
   * provided <code>Function1</code> at uniformly spaced points within
   * the specified domain.
   * @param f The <code>Function1</code> to sample.
   * @param domain The <code>Interval</code> in which to sample
   *     <code>f</code>.  The domain must be non-empty and finite.
   * @param count The number of sub-intervals to divide <code>domain</code>
   *     into.
   * @return A piecewise linear <code>Function1</code>.
   */
  public static Function1 sample(Function1 f, Interval domain, int count) {
    return UniformPiecewiseLinearFunction1.sample(f, domain, count);
  }

}
