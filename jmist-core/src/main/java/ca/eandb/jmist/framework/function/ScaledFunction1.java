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
 * A <code>Function1</code> that differs from another by a constant factor.
 * @author Brad Kimmel
 */
public final class ScaledFunction1 implements Function1 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 7790001536461093763L;

  /**
   * Creates a new <code>ScaledFunction1</code>.
   * @param factor The factor by which to multiply the decorated
   *     <code>Function1</code>.
   * @param inner The <code>Function1</code> to be multiplied by a constant
   *     factor.
   */
  public ScaledFunction1(double factor, Function1 inner) {
    /* Combine successive ScaledFunction1 instances into one. */
    while (inner instanceof ScaledFunction1) {
      ScaledFunction1 f = (ScaledFunction1) inner;
      factor *= f.factor;
      inner = f.inner;
    }

    this.factor = factor;
    this.inner = inner;
  }

  @Override
  public double evaluate(double x) {
    return this.factor * this.inner.evaluate(x);
  }

  /** The factor by which to multiply the decorated <code>Function1</code>. */
  private final double factor;

  /** The <code>Function1</code> to be multiplied by a constant factor. */
  private final Function1 inner;

}
