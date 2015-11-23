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
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> that projects the scene onto a spherical virtual screen.
 * @author Brad Kimmel
 */
public final class SphericalLens extends AbstractLens {

  /** Serialization version ID. */
  private static final long serialVersionUID = 156342820309540922L;

  /** Horizontal field of view (in radians). */
  private final double hfov;

  /** Vertical field of view (in radians). */
  private final double vfov;

  /** The solid angle subtended by the image plane (in steradians). */
  private final double solidAngle;

  /** The default horizontal field of view (in radians). */
  public static final double DEFAULT_HORIZONTAL_FIELD_OF_VIEW = 2.0 * Math.PI;

  /** The default vertical field of view (in radians). */
  public static final double DEFAULT_VERTICAL_FIELD_OF_VIEW = Math.PI;

  /** Creates a new <code>SphericalLens</code>. */
  public SphericalLens() {
    this(DEFAULT_HORIZONTAL_FIELD_OF_VIEW, DEFAULT_VERTICAL_FIELD_OF_VIEW);
  }

  /**
   * Creates a new <code>SphericalLens</code>.
   * @param hfov The horizontal field of view (in radians).
   * @param vfov The vertical field of view (in radians).
   */
  public SphericalLens(double hfov, double vfov) {
    this.hfov = hfov;
    this.vfov = vfov;
    this.solidAngle = 2.0 * hfov * Math.sin(0.5 * vfov);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Lens#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, double, double, double)
   */
  @Override
  public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv,
      double rj) {
    return new Node(p, pathInfo, ru, rv, rj);
  }

  /** An <code>EyeNode</code> generated by a <code>SphericalLens</code>. */
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
     * @see ca.eandb.jmist.framework.path.EyeNode#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.Random)
     */
    public ScatteredRay sample(double ru, double rv, double rj) {
      Point2 p = pointOnImagePlane;

        double    nx = (p.x() - 0.5) * hfov;
        double    ny = (0.5 - p.y()) * vfov;

        double    sx = Math.sin(nx);
        double    sy = Math.sin(ny);
        double    cx = Math.cos(nx);
        double    cy = Math.cos(ny);

        Ray3    ray = new Ray3(
                  Point3.ORIGIN,
                  new Vector3(-sx * cy, sy, -cx * cy));
        Color    color = getWhite();
        double    pdf = 1.0 / solidAngle;
      return ScatteredRay.diffuse(ray, color, pdf);
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#scatterTo(ca.eandb.jmist.framework.path.PathNode)
     */
    public Color scatter(Vector3 v) {
      return getGray(getPDF(v));
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.EyeNode#project(ca.eandb.jmist.math.HPoint3)
     */
    public Point2 project(HPoint3 x) {
      Ray3 ray = new Ray3(Point3.ORIGIN, x);
      Vector3 v = ray.direction().unit();
      double phi = Math.atan2(v.x(), -v.z());
      if (Math.abs(phi) > 0.5 * hfov) {
        return null;
      }
      double theta = Math.asin(v.y());
      if (Math.abs(theta) > 0.5 * vfov) {
        return null;
      }
      return new Point2(
          0.5 + phi / hfov,
          0.5 - theta / vfov);
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
      return Point3.ORIGIN;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
     */
    public double getPDF() {
      return 1.0;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
     */
    public boolean isSpecular() {
      return true;
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.path.PathNode#getPDF(ca.eandb.jmist.math.Vector3)
     */
    public double getPDF(Vector3 v) {
      v = v.unit();
      double phi = Math.atan2(v.x(), -v.z());
      if (Math.abs(phi) > 0.5 * hfov) {
        return 0.0;
      }
      double theta = Math.asin(v.y());
      if (Math.abs(theta) > 0.5 * vfov) {
        return 0.0;
      }
      return 1.0 / solidAngle;
    }

  }

}
