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
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A camera that captures light at a single point.  This is equivalent to the
 * limit as the aperature width and shutter speed approach zero for a normal
 * camera.  A pinhole camera has an infinite depth of field (i.e., no depth of
 * field effects are observed).
 * @author Brad Kimmel
 */
public final class PinholeLens extends AbstractLens {

  /** Serialization version ID. */
  private static final long serialVersionUID = 4259241970405882988L;

  /** The width of the virtual image plane. */
  private final double width;

  /** The height of the virtual image plane. */
  private final double height;

  /**
   * Initializes the pinhole camera from the specified dimensions of the
   * virtual image plane.  The virtual image plane is one meter from the
   * origin along the negative z-axis.
   * @param width The width of the virtual image plane (in meters).
   * @param height The height of the virtual image plane (in meters).
   */
  public PinholeLens(double width, double height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Initializes the pinhole camera from the specified
   * field of view and aspect ratio.
   * @param horizontalFieldOfView The field of view in the horizontal
   *     direction (in radians).  This value must be in
   *     (0, PI).
   * @param aspectRatio The ratio between the width and
   *     height of the image.  This value must be positive.
   * @return The new <code>PinholeLens</code>.
   */
  public static PinholeLens fromHfovAndAspect(double horizontalFieldOfView, double aspectRatio) {

    // Compute the width and height of the virtual
    // image plane from the provided field of view
    // and aspect ratio.  The image plane is assumed
    // to be one unit away from the origin.
    double width = 2.0 * Math.tan(0.5 * horizontalFieldOfView);
    double height = width / aspectRatio;
    return new PinholeLens(width, height);

  }

  /**
   * Initializes the pinhole camera from the specified
   * field of view and aspect ratio.
   * @param verticalFieldOfView The field of view in the vertical
   *     direction (in radians).  This value must be in
   *     (0, PI).
   * @param aspectRatio The ratio between the width and
   *     height of the image.  This value must be positive.
   * @return The new <code>PinholeLens</code>.
   */
  public static PinholeLens fromVfovAndAspect(double verticalFieldOfView, double aspectRatio) {

    // Compute the width and height of the virtual
    // image plane from the provided field of view
    // and aspect ratio.  The image plane is assumed
    // to be one unit away from the origin.
    double height = 2.0 * Math.tan(0.5 * verticalFieldOfView);
    double width = height * aspectRatio;
    return new PinholeLens(width, height);

  }

  /**
   * Initializes the pinhole camera from the specified
   * field of view in the horizontal and vertical directions
   * @param horizontalFieldOfView The field of view in the horizontal
   *     direction (in radians).  This value must be in
   *     (0, PI).
   * @param verticalFieldOfView The field of view in the vertical
   *     direction (in radians).  This value must be in
   *     (0, PI).
   * @return The new <code>PinholeLens</code>.
   */
  public static PinholeLens fromFieldOfView(double horizontalFieldOfView,
      double verticalFieldOfView) {

    // Compute the width and height of the virtual
    // image plane from the provided field of view
    // and aspect ratio.  The image plane is assumed
    // to be one unit away from the origin.
    double width = 2.0 * Math.tan(0.5 * horizontalFieldOfView);
    double height = 2.0 * Math.tan(0.5 * verticalFieldOfView);
    return new PinholeLens(width, height);

  }

  @Override
  public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv, double rj) {
    return new Node(p, pathInfo, ru, rv, rj);
  }

  /** An <code>EyeNode</code> generated by a <code>PinholeLens</code>. */
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

    @Override
    public ScatteredRay sample(double ru, double rv, double rj) {
      Point2 p = pointOnImagePlane;
      Vector3 v = new Vector3(
          width * (p.x() - 0.5),
          height * (0.5 - p.y()),
          -1.0);
      Ray3 ray = new Ray3(Point3.ORIGIN, v.unit());
      Color color = getWhite();
      double z = v.x() * v.x() + v.y() * v.y() + 1.0;
      double pdf = z * z / (width * height);
      return ScatteredRay.diffuse(ray, color, pdf);
    }

    @Override
    public Color scatter(Vector3 v) {
      return getGray(getPDF(v));
    }

    @Override
    public Point2 project(HPoint3 x) {
      Ray3 ray = new Ray3(Point3.ORIGIN, x);
      Vector3 v = ray.direction();
      if (-v.z() < MathUtil.EPSILON) {
        return null;
      }
      Point2 p = new Point2(
          0.5 - v.x() / (width * v.z()),
          0.5 + v.y() / (height * v.z()));
      return Box2.UNIT.contains(p) ? p : null;
    }

    @Override
    public double getCosine(Vector3 v) {
      return -v.z() / v.length();
    }

    @Override
    public HPoint3 getPosition() {
      return Point3.ORIGIN;
    }

    @Override
    public double getPDF() {
      return 1.0;
    }

    @Override
    public boolean isSpecular() {
      return true;
    }

    @Override
    public double getPDF(Vector3 v) {
      double x = -v.x() / v.z();
      double y = -v.y() / v.z();
      if (-v.z() >= MathUtil.EPSILON
          && MathUtil.inRangeCC(x, -0.5 * width, 0.5 * width)
          && MathUtil.inRangeCC(y, -0.5 * height, 0.5 * height)) {
        double z = x * x + y * y + 1.0;
        return z * z / (width * height);
      } else {
        return 0.0;
      }
    }

  }

}
