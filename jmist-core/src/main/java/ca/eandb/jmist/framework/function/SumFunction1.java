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

/**
 * A <code>Function1</code> that is the sum of other functions.
 * @author Brad Kimmel
 */
public final class SumFunction1 extends CompositeFunction1 {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 2089927530431867078L;

  @Override
  public SumFunction1 addChild(Function1 child) {
    if (child instanceof SumFunction1) {
      SumFunction1 sum = (SumFunction1) child;
      for (Function1 grandChild : sum.children()) {
        this.addChild(grandChild);
      }
    } else {
      super.addChild(child);
    }
    return this;
  }

  @Override
  public double evaluate(double x) {

    double sum = 0.0;

    for (Function1 component : this.children()) {
      sum += component.evaluate(x);
    }

    return sum;

  }

}
