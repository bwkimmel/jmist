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

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Vector3;

/**
 * Abstract base class for terminal (eye, light) <code>PathNode</code>s.
 * @author Brad Kimmel
 */
public abstract class AbstractTerminalNode extends AbstractPathNode {

  /**
   * @param pathInfo
   */
  public AbstractTerminalNode(PathInfo pathInfo, double ru, double rv, double rj) {
    super(pathInfo, ru, rv, rj);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getCumulativeWeight()
   */
  public final Color getCumulativeWeight() {
    return getWhite();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getDepth()
   */
  public final int getDepth() {
    return 0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getParent()
   */
  public final PathNode getParent() {
    return null;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getGeometricFactor()
   */
  public final double getGeometricFactor() {
    return 1.0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
   */
  public final double getReversePDF(Vector3 v) {
    return 1.0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF()
   */
  public final double getReversePDF() {
    return 1.0;
  }

}
