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

/**
 * An abstract <code>Intersection</code> that decorates another (e.g., by
 * applying a transformation).  Default implementations are provided that call
 * the corresponding methods on the decorated <code>Intersection</code>.  It is
 * up to the derived class to override whichever methods are necessary to
 * provide the specific decoration required.
 * @author Brad Kimmel
 */
public abstract class IntersectionDecorator implements Intersection {

  /** The decorated <code>Intersection</code>. */
  protected final Intersection inner;

  /**
   * Initializes an <code>IntersectionDecorator</code>.
   * @param inner The <code>Intersection</code> to be decorated.
   */
  protected IntersectionDecorator(Intersection inner) {
    this.inner = inner;
  }

  @Override
  public double getDistance() {
    return this.inner.getDistance();
  }

  @Override
  public double getTolerance() {
    return this.inner.getTolerance();
  }

  @Override
  public boolean isFront() {
    return this.inner.isFront();
  }

  @Override
  public final void prepareShadingContext(ShadingContext context) {
    inner.prepareShadingContext(context);
    transformShadingContext(context);
  }

  protected abstract void transformShadingContext(ShadingContext context);

}
