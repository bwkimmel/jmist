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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Ray3;

/**
 * A strategy for determining with what objects to perform ray intersection
 * tests.
 * @author Brad Kimmel
 */
public interface RayTraversalStrategy3 extends Serializable {

  /**
   * Intersects the specified <code>Ray3</code> with the objects in this
   * collection.
   * @param ray The <code>Ray3</code> to intersect with the objects in this
   *     hierarchy.
   * @param I The <code>Interval</code> along the <code>ray</code> to
   *     consider.
   * @param visitor The <code>Visitor</code> to notify when the bounding box
   *     of an item is hit.
   * @return A value indicating whether the operation was completed without
   *     being canceled.
   */
  boolean intersect(Ray3 ray, Interval I, Visitor visitor);

}
