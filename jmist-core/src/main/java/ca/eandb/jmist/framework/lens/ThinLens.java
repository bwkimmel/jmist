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
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.EyeTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector2;
import ca.eandb.jmist.math.Vector3;

/**
 * A thin <code>Lens</code>.
 * @author Brad Kimmel
 */
public final class ThinLens extends AbstractLens {

  /** Serialization version ID. */
  private static final long serialVersionUID = -4932532440872351450L;

  /** The default field of view (in radians). */
  public static final double  DEFAULT_FIELD_OF_VIEW = Math.PI / 2.0;

  /** The default aspect ratio. */
  public static final double  DEFAULT_ASPECT_RATIO = 1.0;

  /** The default focal length (in meters). */
  public static final double  DEFAULT_FOCAL_LENGTH = 0.050;

  /** The default aperture (f-number). */
  public static final double  DEFAULT_APERTURE = 3.6;

  /** The default distance to the plane in focus (in meters). */
  public static final double  DEFAULT_FOCUS_DISTANCE = 1.0;

  /** The focal length (in meters). */
  private final double focalLength;

  /** The aperture (f-number). */
  private final double aperture;

  /** The distance to the plane in focus (in meters). */
  private final double focusDistance;

  /** The field of view (in radians). */
  private final double fov;

  /** The aspect ratio. */
  private final double aspect;

  /** The radius of the aperture (in meters). */
  private final double apertureRadius;

  /** The area of the aperture (in meters squared). */
  private final double apertureArea;

  /** The width of the virtual screen at the focus distance (in meters). */
  private final double objPlaneWidth;

  /** The height of the virtual screen at the focus distance (in meters). */
  private final double objPlaneHeight;

  public static final class Builder {
    private double focalLength = DEFAULT_FOCAL_LENGTH;
    private double aperture = DEFAULT_APERTURE;
    private double focusDistance = DEFAULT_FOCUS_DISTANCE;
    private double fov = DEFAULT_FIELD_OF_VIEW;
    private double aspect = DEFAULT_ASPECT_RATIO;

    private Builder() {}

    /**
     * Sets the focal length.
     * @param focalLength The focal length (in meters).
     * @return This <code>Builder</code>.
     */
    public Builder setFocalLength(double focalLength) {
      this.focalLength = focalLength;
      return this;
    }

    /**
     * Sets the aperture size.
     * @param aperture The aperture (f-number).
     * @return This <code>Builder</code>.
     */
    public Builder setAperture(double aperture) {
      this.aperture = aperture;
      return this;
    }

    /**
     * Sets the distance to the plane in focus.
     * @param focusDistance The focus distance (in meters).
     * @return This <code>Builder</code>.
     */
    public Builder setFocusDistance(double focusDistance) {
      this.focusDistance = focusDistance;
      return this;
    }

    /**
     * Sets the field of view.
     * @param fov The field of view (in radians).
     * @return This <code>Builder</code>.
     */
    public Builder setFieldOfView(double fov) {
      this.fov = fov;
      return this;
    }

    /**
     * Sets the aspect ratio.
     * @param aspect The aspect ratio.
     * @return This <code>Builder</code>.
     */
    public Builder setAspectRatio(double aspect) {
      this.aspect = aspect;
      return this;
    }

    /**
     * Builds the <code>ThinLens</code>.
     * @return The new <code>ThinLens</code>.
     */
    public ThinLens build() {
      return new ThinLens(focalLength, aperture, focusDistance, fov, aspect);
    }
  }

  /**
   * Returns a new builder for creating a <code>ThinLens</code>.
   * @return The new <code>Builder</code>.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Creates a new <code>ThinLens</code>.
   */
  public ThinLens() {
    this.focalLength = DEFAULT_FOCAL_LENGTH;
    this.aperture = DEFAULT_APERTURE;
    this.focusDistance = DEFAULT_FOCUS_DISTANCE;
    this.fov = DEFAULT_FIELD_OF_VIEW;
    this.aspect = DEFAULT_ASPECT_RATIO;
    this.apertureRadius = 0.5 * focalLength / aperture;
    this.apertureArea = Math.PI * apertureRadius * apertureRadius;
    this.objPlaneWidth = 2.0 * focusDistance * Math.tan(fov / 2.0);
    this.objPlaneHeight = objPlaneWidth / aspect;
  }

  /**
   * Creates a new <code>ThinLens</code>.
   * @param focalLength The focal length (in meters).
   * @param aperture The aperture (f-number).
   * @param focusDistance The distance to the plane in focus (in meters).
   * @param fov The field of view (in radians).
   * @param aspect The aspect ratio.
   */
  public ThinLens(double focalLength, double aperture, double focusDistance, double fov, double aspect) {
    this.focalLength = focalLength;
    this.aperture = aperture;
    this.focusDistance = focusDistance;
    this.fov = fov;
    this.aspect = aspect;
    this.apertureRadius = 0.5 * focalLength / aperture;
    this.apertureArea = Math.PI * apertureRadius * apertureRadius;
    this.objPlaneWidth = 2.0 * focusDistance * Math.tan(fov / 2.0);
    this.objPlaneHeight = objPlaneWidth / aspect;
  }

  @Override
  public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv,
      double rj) {
    return new Node(p, pathInfo, ru, rv, rj);
  }

  /**
   * An <code>EyeNode</code> generated by a <code>ThinLens</code>.
   */
  private final class Node extends EyeTerminalNode {

    /** Projected point on the image plane. */
    private final Point2 pointOnImagePlane;

    private final Ray3 ray;

    public Node(Point2 p, PathInfo pathInfo, double ru, double rv, double rj) {
      super(pathInfo, ru, rv, rj);
      this.pointOnImagePlane = p;

      Vector2 uv = RandomUtil.uniformOnDisc(apertureRadius, ru, rv).toCartesian();
      Point3 origin = new Point3(uv.x(), uv.y(), 0.0);
      Point3 focus = new Point3(
          (pointOnImagePlane.x() - 0.5) * objPlaneWidth,
          (0.5 - pointOnImagePlane.y()) * objPlaneHeight,
          -focusDistance);

      Vector3 direction = origin.unitVectorTo(focus);

      this.ray = new Ray3(origin, direction);
    }

    @Override
    public Point2 project(HPoint3 p) {
      Ray3 pray = new Ray3(ray.origin(), p);
      Vector3 dir = pray.direction();
      if (-dir.z() < MathUtil.EPSILON) {
        return null;
      }

      double ratio = -focusDistance / dir.z();
      double x = ray.origin().x() + ratio * dir.x();
      double y = ray.origin().y() + ratio * dir.y();

      final double u = 0.5 + x / objPlaneWidth;
      if (!MathUtil.inRangeCC(u, 0.0, 1.0)) {
        return null;
      }

      final double v = 0.5 - y / objPlaneHeight;
      if (!MathUtil.inRangeCC(v, 0.0, 1.0)) {
        return null;
      }

      return new Point2(u, v);
    }

    @Override
    public double getPDF() {
      return 1.0 / apertureArea;
    }

    @Override
    public boolean isSpecular() {
      return false;
    }

    @Override
    public HPoint3 getPosition() {
      return ray.origin();
    }

    @Override
    public ScatteredRay sample(double ru, double rv, double rj) {
      Vector3 v = ray.direction();
      Color color = getWhite();
      double pdf = (focusDistance * focusDistance)
          / (v.z() * v.z() * v.z() * v.z() * objPlaneWidth * objPlaneHeight);
      return ScatteredRay.diffuse(ray, color, pdf);
    }

    @Override
    public Color scatter(Vector3 v) {
      return getGray(getPDF(v));
    }

    @Override
    public double getCosine(Vector3 v) {
      return -v.z() / v.length();
    }

    @Override
    public double getPDF(Vector3 v) {
      v = v.unit();
      return (focusDistance * focusDistance)
          / (v.z() * v.z() * v.z() * v.z() * objPlaneWidth * objPlaneHeight);
    }

  }
}
