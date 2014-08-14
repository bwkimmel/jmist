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

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.EyeTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A circular fisheye lens (http://en.wikipedia.org/wiki/Fisheye_lens).
 * @author Brad Kimmel
 */
public final class FisheyeLens extends AbstractLens {

  /** Serialization version ID. */
  private static final long serialVersionUID = 4393119937901820155L;

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Lens#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, double, double, double)
   */
  @Override
  public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv, double rj) {
    return new Node(p, pathInfo, ru, rv, rj);
  }

  /**
   * An <code>EyeNode</code> generated by a <code>FisheyeLens</code>.
   */
  private final class Node extends EyeTerminalNode {

    /** Projected point on the image plane. */
    private final Point2 pointOnImagePlane;

    /**
     * Creates a <code>Node</code>.
     * @param pointOnImagePlane The <code>Point2</code> on the image plane.
     * @param pathInfo The <code>PathInfo</code> describing the context for
     *     this node.
     */
    public Node(Point2 pointOnImagePlane, PathInfo pathInfo, double ru, double rv, double rj) {
      super(pathInfo, ru, rv, rj);
      this.pointOnImagePlane = pointOnImagePlane;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.EyeNode#project(ca.eandb.jmist.math.HPoint3)
     */
    @Override
    public Point2 project(HPoint3 x) {
      Vector3 v = x.isPoint() ? x.toPoint3().vectorFromOrigin()
                              : x.toVector3();
      double d = v.length();

      if (-v.z() / d < MathUtil.EPSILON) {
        return null;
      }
      return new Point2(
          (v.x() / d + 1.0) / 2.0,
          (1.0 - v.y() / d) / 2.0);
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
     */
    @Override
    public double getCosine(Vector3 v) {
      return -v.z() / v.length();
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
     */
    @Override
    public double getPDF() {
      return 1.0;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
     */
    @Override
    public double getPDF(Vector3 v) {
      if (getCosine(v) < MathUtil.EPSILON) {
        return 0.0;
      }
      return 0.25;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
     */
    @Override
    public HPoint3 getPosition() {
      return Point3.ORIGIN;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
     */
    @Override
    public boolean isSpecular() {
      return true;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#sample(double, double, double)
     */
    @Override
    public ScatteredRay sample(double ru, double rv, double rj) {
      Point2  p = pointOnImagePlane;
      double  nx = 2.0 * (p.x() - 0.5);
      double  ny = 2.0 * (0.5 - p.y());
      double  d2 = nx * nx + ny * ny;

      if (d2 > 1.0)
        return null;

      Ray3 ray = new Ray3(
          Point3.ORIGIN,
          new Vector3(nx, ny,  -Math.sqrt(1.0 - d2)));
      Color color = getWhite();
      double pdf = 0.25;

      return ScatteredRay.diffuse(ray, color, pdf);
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#scatter(ca.eandb.jmist.math.Vector3)
     */
    @Override
    public Color scatter(Vector3 v) {
      return getGray(getPDF(v));
    }

  }

}
