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
 * A <code>Function1</code> that is the product of other functions.
 * @author Brad Kimmel
 */
public final class ProductFunction1 extends CompositeFunction1 {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 3613122771597657293L;

  @Override
  public ProductFunction1 addChild(Function1 child) {
    if (child instanceof ProductFunction1) {
      ProductFunction1 product = (ProductFunction1) child;
      for (Function1 grandChild : product.children()) {
        this.addChild(grandChild);
      }
    } else {
      super.addChild(child);
    }
    return this;
  }

  /**
   * Creates a new <code>ProductFunction1</code>.
   */
  public ProductFunction1() {
    /* do nothing */
  }

  /**
   * Creates a new <code>ProductFunction1</code>.
   * @param a The first <code>Function1</code>.
   * @param b The second <code>Function1</code>.
   */
  public ProductFunction1(Function1 a, Function1 b) {
    this.addChild(a).addChild(b);
  }

  @Override
  public double evaluate(double x) {

    double product = 1.0;

    for (Function1 child : this.children()) {
      product *= child.evaluate(x);
    }

    return product;

  }

}
