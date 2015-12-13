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
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>PathNode</code> that has had an affine transformation applied to it.
 * @author Brad Kimmel
 */
public abstract class TransformedPathNode extends AbstractPathNode {

  protected final PathNode inner;

  protected final AffineMatrix3 localToWorld;

  protected final AffineMatrix3 worldToLocal;

  public TransformedPathNode(PathNode inner, AffineMatrix3 localToWorld, AffineMatrix3 worldToLocal) {
    super(inner.getPathInfo(), inner.getRU(), inner.getRV(), inner.getRJ());
    this.inner = inner;
    this.localToWorld = localToWorld;
    this.worldToLocal = worldToLocal;
  }

  public TransformedPathNode(PathNode inner, AffineMatrix3 worldToLocal) {
    this(inner, worldToLocal, worldToLocal.inverse());
  }

  @Override
  public double getCosine(Vector3 v) {
    v = worldToLocal.times(v);
    return inner.getCosine(v);
  }

  @Override
  public Color getCumulativeWeight() {
    return inner.getCumulativeWeight();
  }

  @Override
  public int getDepth() {
    return inner.getDepth();
  }

  @Override
  public double getGeometricFactor() {
    return inner.getGeometricFactor();
  }

  @Override
  public double getPDF() {
    return inner.getPDF();
  }

  @Override
  public PathNode getParent() {
    return inner.getParent();
  }

  @Override
  public HPoint3 getPosition() {
    return localToWorld.times(inner.getPosition());
  }

  @Override
  public boolean isOnLightPath() {
    return inner.isOnLightPath();
  }

  @Override
  public boolean isSpecular() {
    return inner.isSpecular();
  }

  @Override
  public ScatteredRay sample(double ru, double rv, double rj) {
    ScatteredRay sr = inner.sample(ru, rv, rj);
    return sr != null ? sr.transform(localToWorld) : null;
  }

  @Override
  public Color scatter(Vector3 v) {
    v = worldToLocal.times(v);
    return inner.scatter(v);
  }

}
