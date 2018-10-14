/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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

/**
 * A <code>Function1</code> that adjusts another function (<code>x</code>)
 * according to <code>a*x + b</code>.
 * @author Brad Kimmel
 */
public final class AXpBFunction1 implements Function1 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 6722994298922073207L;

  /**
   * Creates a new <code>ScaledFunction1</code>.
   * @param a The factor by which to multiply the decorated
   *     <code>Function1</code>.
   * @param b The value to add to the product of <code>a</code> and
   *     <code>inner</code>
   * @param inner The <code>Function1</code> to be adjusted.
   */
  public AXpBFunction1(double a, double b, Function1 inner) {
    this.a = a;
    this.b = b;
    this.inner = inner;

  }

  @Override
  public double evaluate(double x) {
    return a * this.inner.evaluate(x) + b;
  }

  /** The factor by which to multiply the decorated <code>Function1</code>. */
  private final double a;

  /**
   * The value to add to the product of <code>a</code> and the decorated
   * <code>Function1</code>.
   */
  private final double b;

  /** The <code>Function1</code> to be adjusted. */
  private final Function1 inner;

}
