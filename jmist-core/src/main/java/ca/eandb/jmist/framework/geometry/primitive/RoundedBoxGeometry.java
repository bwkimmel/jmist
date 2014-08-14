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
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.util.UnimplementedException;

/**
 * A <code>PrimitiveGeometry</code> that represents a box with rounded edges.
 * 
 * @author Brad Kimmel
 */
public final class RoundedBoxGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = 1671007598641076289L;
  
  /** The bounding <code>Box3</code>. */
  private final Box3 box;
  
  /** The radius of the bevel. */
  private final double bevelRadius;

  /**
   * Creates a new <code>RoundedBoxGeometry</code>.
   * @param box The bounding <code>Box3</code>.
   * @param bevelRadius The radius of the bevel along the edges of the box.
   *     This must be non-negative and no more than half the length of the
   *     shortest side of the <code>box</code>. 
   */
  public RoundedBoxGeometry(Box3 box, double bevelRadius) {
    double maxBevelRadius = Math.min(Math.min(box.lengthX(), box.lengthY()), box.lengthZ());
    this.box = box;
    this.bevelRadius = MathUtil.clamp(bevelRadius, 0.0, maxBevelRadius);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    throw new UnimplementedException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#isClosed()
   */
  public boolean isClosed() {
    return true;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
   */
  public Box3 boundingBox() {
    return box;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
   */
  public Sphere boundingSphere() {
    return new Sphere(box.center(), 0.5 * box.diagonal() + bevelRadius);
  }

}
