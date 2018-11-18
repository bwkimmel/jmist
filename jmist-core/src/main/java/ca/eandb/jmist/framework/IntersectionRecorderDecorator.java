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

import ca.eandb.jmist.math.Interval;

/**
 * An abstract <code>IntersectionRecorder</code> that decorates another.  This
 * will typically be used to decorate an <code>Intesection</code> prior to
 * recording it to the decorated <code>IntersectionRecorder</code>.
 * @author Brad Kimmel
 */
public abstract class IntersectionRecorderDecorator implements
    IntersectionRecorder {

  /**
   * Initializes an <code>IntersectionRecorderDecorator</code>.
   * @param inner The <code>IntersectionRecorder</code> to be decorated.
   */
  protected IntersectionRecorderDecorator(IntersectionRecorder inner) {
    this.inner = inner;
  }

  @Override
  public Interval interval() {
    return this.inner.interval();
  }

  @Override
  public boolean needAllIntersections() {
    return this.inner.needAllIntersections();
  }

  @Override
  public boolean isEmpty() {
    return this.inner.isEmpty();
  }

  @Override
  public abstract void record(Intersection intersection);

  /** The decorated <code>IntersectionRecorder</code>. */
  protected final IntersectionRecorder inner;

}
