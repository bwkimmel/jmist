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

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 */
public final class BackgroundNode extends AbstractScatteringNode {

  private final Vector3 direction;

  /**
   * @param parent
   * @param sr
   */
  public BackgroundNode(PathNode parent, ScatteredRay sr, double ru, double rv, double rj) {
    super(parent, sr, ru, rv, rj);
    this.direction = sr.getRay().direction();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.ScatteringNode#getSourcePDF()
   */
  public double getSourcePDF() {
    return 0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.ScatteringNode#getSourcePDF(ca.eandb.jmist.math.Vector3)
   */
  public double getSourcePDF(Vector3 v) {
    return 0.0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.ScatteringNode#getSourceRadiance()
   */
  public Color getSourceRadiance() {
    return getBlack();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.ScatteringNode#isOnLightSource()
   */
  public boolean isOnLightSource() {
    return false;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#sample(double, double, double)
   */
  public ScatteredRay sample(double ru, double rv, double rj) {
    return null;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
   */
  public double getCosine(Vector3 v) {
    return 1.0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
   */
  public HPoint3 getPosition() {
    return direction;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#scatter(ca.eandb.jmist.math.Vector3)
   */
  public Color scatter(Vector3 v) {
    return getBlack();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
   */
  public double getPDF(Vector3 v) {
    return 0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
   */
  public double getReversePDF(Vector3 v) {
    return 0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#reverse(ca.eandb.jmist.framework.path.PathNode, ca.eandb.jmist.framework.path.PathNode)
   */
  public PathNode reverse(PathNode newParent, PathNode grandChild) {
    return null;
  }

}
