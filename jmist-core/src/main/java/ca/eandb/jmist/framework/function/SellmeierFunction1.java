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
 * A <code>Function1</code> that evaluates the Sellmeier dispersion equation.
 * Wavelengths are specified in meters.  See
 * <a href="http://en.wikipedia.org/wiki/Sellmeier_equation">
 * http://en.wikipedia.org/wiki/Sellmeier_equation</a> for more information.
 * @author Brad Kimmel
 */
public final class SellmeierFunction1 implements Function1 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 8860787592075376017L;

  /** The B coefficients. */
  private final double[] b;

  /** The C coefficients (m^2). */
  private final double[] c;

  /**
   * The <code>Interval</code> indicating the valid wavelength range for the
   * formula.
   */
  private final Interval range;

  /**
   * Creates a <code>SellmeierFunction1</code> with a limited valid
   * wavelength range.
   * @param b The B coefficients.
   * @param c The C coefficients (in m^2).
   * @param range The valid wavelength range (in m).  If <code>null</code>,
   *     there is no range limit.
   * @throws IllegalArgumentException If <code>b</code> or <code>c</code> is
   *     <code>null</code>.
   * @throws IllegalArgumentException If <code>b.length != c.length</code>.
   */
  public SellmeierFunction1(double[] b, double[] c, Interval range) {
    if (b == null || c == null) {
      throw new IllegalArgumentException("b == null || c == null");
    }
    if (b.length != c.length) {
      throw new IllegalArgumentException("b.length != c.length");
    }
    this.b = b;
    this.c = c;
    this.range = range;
  }

  /**
   * Creates a <code>SellmeierFunction1</code>.
   * @param b The B coefficients.
   * @param c The C coefficients (in m^2).
   * @throws IllegalArgumentException If <code>b</code> or <code>c</code> is
   *     <code>null</code>.
   * @throws IllegalArgumentException If <code>b.length != c.length</code>.
   */
  public SellmeierFunction1(double[] b, double[] c) {
    this(b, c, null);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Function1#evaluate(double)
   */
  public double evaluate(double x) {
    if (range == null || range.contains(x)) {
      double x2 = x * x;
      double n2 = 1.0;
      for (int i = 0; i < b.length; i++) {
        n2 += (b[i] * x2) / (x2 - c[i]);
      }
      return Math.sqrt(n2);
    } else {
      return 0.0;
    }
  }

}
