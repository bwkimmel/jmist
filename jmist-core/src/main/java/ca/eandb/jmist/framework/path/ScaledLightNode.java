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
 * @author Brad
 *
 */
public final class ScaledLightNode extends AbstractPathNode implements
    LightNode {

  private final LightNode inner;

  private final double pdf;

  private ScaledLightNode(double pdf, LightNode inner, double rj) {
    super(inner.getPathInfo(), inner.getRU(), inner.getRV(), rj);
    this.pdf = pdf;
    this.inner = inner;
  }

  public static LightNode create(double pdf, LightNode node, double rj) {
    if (node instanceof ScaledLightNode) {
      ScaledLightNode n = (ScaledLightNode) node;
      return new ScaledLightNode(pdf * n.pdf, n.inner, rj);
    } else {
      return new ScaledLightNode(pdf, node, rj);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.framework.path.PathNode)
   */
  public double getCosine(Vector3 v) {
    return inner.getCosine(v);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getCumulativeWeight()
   */
  public Color getCumulativeWeight() {
    return inner.getCumulativeWeight().divide(pdf);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getDepth()
   */
  public int getDepth() {
    return inner.getDepth();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getGeometricFactor()
   */
  public double getGeometricFactor() {
    return inner.getGeometricFactor();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
   */
  public double getPDF() {
    return inner.getPDF() * pdf;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getParent()
   */
  public PathNode getParent() {
    return inner.getParent();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
   */
  public HPoint3 getPosition() {
    return inner.getPosition();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF()
   */
  public double getReversePDF() {
    return 1.0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#isAtInfinity()
   */
  public boolean isAtInfinity() {
    return inner.isAtInfinity();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#isOnLightPath()
   */
  public boolean isOnLightPath() {
    return inner.isOnLightPath();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
   */
  public boolean isSpecular() {
    return inner.isSpecular();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#sample(double, double, double)
   */
  public ScatteredRay sample(double ru, double rv, double rj) {
    return inner.sample(ru, rv, rj);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#scatter(ca.eandb.jmist.math.Vector3)
   */
  public Color scatter(Vector3 v) {
    return inner.scatter(v);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
   */
  public double getPDF(Vector3 v) {
    return inner.getPDF(v);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF(ca.eandb.jmist.math.Vector3)
   */
  public double getReversePDF(Vector3 v) {
    return inner.getReversePDF(v);
  }

  @Override
  public PathNode reverse(PathNode newParent, PathNode grandChild) {
    return inner.reverse(newParent, grandChild);
  }

}
