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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.function.ConstantFunction1;

/**
 * Represents a unary function.
 * @author Brad Kimmel
 */
public interface Function1 extends Serializable {

  /**
   * Evaluates the function at the specified point in its domain.
   * @param x The value at which to evaluate the function.
   * @return The value of the function (units defined
   *     by the concrete class).
   */
  double evaluate(double x);

  /**
   * The identity <code>Function1</code>.
   */
  Function1 IDENTITY = new Function1() {
    private static final long serialVersionUID = -1707801506094527459L;
    public double evaluate(double x) {
      return x;
    }
  };

  /**
   * A <code>Function1</code> that evaluates to zero everywhere.
   */
  Function1 ZERO = new ConstantFunction1(0.0);

  /**
   * A <code>Function1</code> that evaluates to one everywhere.
   */
  Function1 ONE = new ConstantFunction1(1.0);

  /**
   * A <code>Function1</code> whose value is <code>POSITIVE_INFINITY</code>
   * everywhere.
   * @see Double#POSITIVE_INFINITY
   */
  Function1 POSITIVE_INFINITY = new ConstantFunction1(Double.POSITIVE_INFINITY);

  /**
   * A <code>Function1</code> whose value is <code>NEGATIVE_INFINITY</code>
   * at all wavelengths.
   * @see Double#NEGATIVE_INFINITY
   */
  Function1 NEGATIVE_INFINITY = new ConstantFunction1(Double.NEGATIVE_INFINITY);

}
