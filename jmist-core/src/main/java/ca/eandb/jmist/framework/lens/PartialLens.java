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
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.path.AbstractPathNode;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

/**
 *
 * @author Brad Kimmel
 */
public final class PartialLens extends AbstractLens {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3343619784708416059L;

  private final Lens inner;

  private final Box2 bounds;

  private final double area;

  public PartialLens(Box2 bounds, Lens inner) {
    this.inner = inner;
    this.bounds = bounds;
    this.area = bounds.area();
  }

  @Override
  public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv,
      double rj) {
    p = bounds.interpolate(p);
    return new PartialEyeNode(inner.sample(p, pathInfo, ru, rv, rj));
  }

  private final class PartialEyeNode extends AbstractPathNode implements EyeNode {

    private final EyeNode inner;

    public PartialEyeNode(EyeNode inner) {
      super(inner.getPathInfo(), inner.getRU(), inner.getRV(), inner.getRJ());
      this.inner = inner;
    }

    @Override
    public Point2 project(HPoint3 x) {
      Point2 p = inner.project(x);
      return p != null && bounds.contains(p)
        ? new Point2(
          (p.x() - bounds.minimumX()) / bounds.lengthX(),
          (p.y() - bounds.minimumY()) / bounds.lengthY())
        : null;
    }

    @Override
    public double getCosine(Vector3 v) {
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
    public double getPDF(Vector3 v) {
      return inner.getPDF(v) / area;
    }

    @Override
    public PathNode getParent() {
      return inner.getParent();
    }

    @Override
    public HPoint3 getPosition() {
      return inner.getPosition();
    }

    @Override
    public double getReversePDF() {
      return 1.0;
    }

    @Override
    public double getReversePDF(Vector3 v) {
      return 1.0;
    }

    @Override
    public boolean isAtInfinity() {
      return inner.isAtInfinity();
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
    public PathNode reverse(PathNode newParent, PathNode grandChild) {
      return null;
    }

    @Override
    public ScatteredRay sample(double ru, double rv, double rj) {
      return inner.sample(ru, rv, rj);
    }

    @Override
    public Color scatter(Vector3 v) {
      return inner.scatter(v);
    }

  }

}
