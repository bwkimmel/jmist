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
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Vector3;

/**
 * Abstract base class for internal <code>PathNode</code>s.
 * @author Brad Kimmel
 */
public abstract class AbstractScatteringNode extends AbstractPathNode implements
    ScatteringNode {

  private final PathNode parent;

  private final int depth;

  private final Color cumulativeWeight;

  private final boolean specular;

  private final double pdf;

  private final boolean onLightPath;

  private double geometricFactor;

  private double reversePDF;

  public AbstractScatteringNode(PathNode parent, ScatteredRay sr, double ru, double rv, double rj) {
    super(parent.getPathInfo(), ru, rv, rj);
    this.parent = parent;
    this.depth = parent.getDepth() + 1;
    this.cumulativeWeight = parent.getCumulativeWeight().times(sr.getColor());
    this.specular = (sr.getType() == Type.SPECULAR);
    this.pdf = sr.getPDF();
    this.geometricFactor = Double.NaN;
    this.reversePDF = Double.NaN;
    this.onLightPath = parent.isOnLightPath();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getCumulativeWeight()
   */
  public final Color getCumulativeWeight() {
    return cumulativeWeight;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getDepth()
   */
  public final int getDepth() {
    return depth;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getGeometricFactor()
   */
  public final double getGeometricFactor() {
    if (Double.isNaN(geometricFactor)) {
      geometricFactor = PathUtil.getGeometricFactor(parent, this);
    }
    return geometricFactor;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
   */
  public final double getPDF() {
    return pdf;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getParent()
   */
  public final PathNode getParent() {
    return parent;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#getReversePDF()
   */
  public final double getReversePDF() {
    assert(parent != null);
    if (Double.isNaN(reversePDF)) {
      if (specular) {
        // FIXME should not assume symmetry in PDF
        reversePDF = (parent.getParent() != null) ? pdf : 1.0;
      } else {
        Vector3 v = PathUtil.getDirection(this, parent);
        reversePDF = parent.getReversePDF(v);
      }
    }
    return reversePDF;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#isOnLightPath()
   */
  public final boolean isOnLightPath() {
    return onLightPath;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
   */
  public final boolean isSpecular() {
    return specular;
  }

}
