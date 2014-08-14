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
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class TransformedEyeNode extends TransformedPathNode implements
    EyeNode {

  /**
   * @param inner
   * @param localToWorld
   * @param worldToLocal
   */
  public TransformedEyeNode(EyeNode inner, AffineMatrix3 localToWorld,
      AffineMatrix3 worldToLocal) {
    super(inner, localToWorld, worldToLocal);
  }

  /**
   * @param inner
   * @param worldToLocal
   */
  public TransformedEyeNode(EyeNode inner, AffineMatrix3 worldToLocal) {
    super(inner, worldToLocal);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.EyeNode#project(ca.eandb.jmist.math.HPoint3)
   */
  public Point2 project(HPoint3 x) {
    x = worldToLocal.times(x);
    return ((EyeNode) inner).project(x);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
   */
  public double getPDF(Vector3 v) {
    v = worldToLocal.times(v);
    return inner.getPDF(v);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
   */
  public double getReversePDF(Vector3 v) {
    return 1.0;
  }
  
  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF()
   */
  public double getReversePDF() {
    return 1.0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#reverse(ca.eandb.jmist.framework.path.PathNode, ca.eandb.jmist.framework.path.PathNode)
   */
  public PathNode reverse(PathNode newParent, PathNode grandChild) {
    return null;
  }

}
