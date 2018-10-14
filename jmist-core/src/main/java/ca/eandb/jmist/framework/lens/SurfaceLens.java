/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.EyeTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A camera <code>Lens</code> that captures all light incident on the geometry
 * associated with a given <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class SurfaceLens extends AbstractLens {

  /** Serialization version ID. */
  private static final long serialVersionUID = -7507678606010224670L;

  /** The <code>SceneElement</code> from which rays are emitted. */
  private final SceneElement e;

  /**
   * Creates a new <code>SurfaceLens</code>.
   * @param e The <code>SceneElement</code> to capture light.
   */
  public SurfaceLens(SceneElement e) {
    this.e = e;
  }

  @Override
  public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv, double rj) {
    return new Node(p, pathInfo, ru, rv, rj);
  }

  /** An <code>EyeNode</code> generated by a <code>SurfaceLens</code>. */
  private final class Node extends EyeTerminalNode {

    /** Projected point on the image plane. */
    private final Point2 pointOnImagePlane;

    /** The <code>ShadingContext</code> for this node. */
    private final ShadingContext context = new MinimalShadingContext();

    /**
     * Creates a <code>Node</code>.
     * @param pointOnImagePlane The <code>Point2</code> on the image plane.
     * @param pathInfo The <code>PathInfo</code> describing the context for
     *     this node.
     */
    public Node(Point2 pointOnImagePlane, PathInfo pathInfo, double ru, double rv, double rj) {
      super(pathInfo, ru, rv, rj);
      this.pointOnImagePlane = pointOnImagePlane;
      e.generateRandomSurfacePoint(context, pointOnImagePlane.x(), pointOnImagePlane.y(), 0.0);
    }

    @Override
    public ScatteredRay sample(double ru, double rv, double rj) {
      Point3 o = context.getPosition();
      Vector3 n = context.getNormal();
      Vector3 v = RandomUtil.diffuse(ru, rv).toCartesian(Basis3.fromW(n));
      Ray3 ray = new Ray3(o, v);
      Color color = getWhite();
      return ScatteredRay.diffuse(ray, color, 1.0 / Math.PI);
    }

    @Override
    public Color scatter(Vector3 v) {
      return getGray(getPDF(v));
    }

    @Override
    public Point2 project(HPoint3 x) {
      return pointOnImagePlane;
    }

    @Override
    public double getCosine(Vector3 v) {
      Vector3 n = context.getNormal();
      return n.dot(v);
    }

    @Override
    public HPoint3 getPosition() {
      return context.getPosition();
    }

    @Override
    public double getPDF() {
      return 1.0 / e.getSurfaceArea();
    }

    @Override
    public boolean isSpecular() {
      return false;
    }

    @Override
    public double getPDF(Vector3 v) {
      Vector3 n = context.getNormal();
      return (n.dot(v) > 0.0) ? 1.0 / Math.PI : 0.0;
    }

  }

}
