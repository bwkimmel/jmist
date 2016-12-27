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
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Polynomial;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * A cone aligned along the y-axis.
 *
 * @author Brad Kimmel
 */
public final class ConeGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = -9049728620213203671L;

  /** The point at the base of the cone */
  private final Point3 base;

  /** The radius of the base of the cone */
  private final double radius;

  /** The height of the cone */
  private final double height;

  /** A value indicating whether the base of the cone is rendered. */
  private final boolean capped;

  /** The surface ID for the body of the cone. */
  private static final int CONE_SURFACE_BODY = 0;

  /** The surface ID for the base of the cone. */
  private static final int CONE_SURFACE_BASE = 1;

  /**
   * Creates a new <code>ConeGeometry</code>.
   * @param base The <code>Point3</code> at the base of the cone.
   * @param radius The radius of the base of the cone.
   * @param height The height of the cone.
   * @param capped A value indicating whether the base of the cone is
   *     rendered.
   */
  public ConeGeometry(Point3 base, double radius, double height, boolean capped) {
    this.base = base;
    this.radius = radius;
    this.height = height;
    this.capped = capped;
  }

  /**
   * Creates a new <code>ConeGeometry</code>.
   * @param base The <code>Point3</code> at the base of the cone.
   * @param radius The radius of the base of the cone.
   * @param height The height of the cone.
   */
  public ConeGeometry(Point3 base, double radius, double height) {
    this(base, radius, height, true);
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {

    if (capped) {
      double t = Plane3.XZ.intersect(ray);

      if (recorder.interval().contains(t)) {
        Point3 p = ray.pointAt(t);
        if (p.distanceToOrigin() <= radius) {
          Intersection x = super.newIntersection(ray, t, ray.origin().y() < 0.0, CONE_SURFACE_BASE)
            .setLocation(p);
          recorder.record(x);
        }
      }
    }

    Point3 org = ray.origin();
    Vector3 dir = ray.direction();

    double x0 = org.x() / radius;
    double y0 = org.y() / height - 1.0;
    double z0 = org.z() / radius;
    double x1 = dir.x() / radius;
    double y1 = dir.y() / height;
    double z1 = dir.z() / radius;

    Polynomial f = new Polynomial(
        x0 * x0 - y0 * y0 + z0 * z0,
        2.0 * (x0 * x1 - y0 * y1 + z0 * z1),
        x1 * x1 - y1 * y1 + z1 * z1);

    double[] t = f.roots();

    if (t.length == 2) {
      for (int i = 0; i < 2; i++) {
        if (recorder.interval().contains(t[i])) {
          Point3 p = ray.pointAt(t[i]);
          if (0.0 <= p.y() && p.y() <= height) {
            Intersection x = super.newIntersection(ray, t[i], i == 0, CONE_SURFACE_BODY)
              .setLocation(p);
            recorder.record(x);
          }
        }
      }
    }

  }

  @Override
  public double getSurfaceArea() {
    double s = Math.hypot(radius, height);
    return capped ? Math.PI * radius * radius + Math.PI * s * radius
        : Math.PI * s * radius;
  }

  @Override
  protected Basis3 getBasis(GeometryIntersection x) {
    switch (x.getTag()) {
    case CONE_SURFACE_BODY:
      return Basis3.fromWV(
          getNormal(x),
          x.getPosition().vectorTo(new Point3(0, height, 0)));

    case CONE_SURFACE_BASE:
      return Basis3.fromW(getNormal(x));

    default:
      throw new IllegalArgumentException("Invalid surface ID");
    }
  }

  @Override
  protected Vector3 getNormal(GeometryIntersection x) {
    switch (x.getTag()) {
    case CONE_SURFACE_BODY:
      Point3 p = x.getPosition();
      double side = Math.hypot(radius, height);
      double c = radius / side;
      double s = height / side;
      double hyp = Math.hypot(p.x(), p.z());

      return new Vector3(
          s * p.x() / hyp,
          c,
          s * p.z() / hyp);

    case CONE_SURFACE_BASE:
      return Vector3.NEGATIVE_J;

    default:
      throw new IllegalArgumentException("Invalid surface ID");
    }
  }

  @Override
  protected Point2 getTextureCoordinates(GeometryIntersection x) {
    Point3 p = x.getPosition();
    switch (x.getTag()) {
    case CONE_SURFACE_BODY:
      double angle = Math.PI + Math.atan2(p.z(), p.x());
      return new Point2(angle / ((capped ? 4.0 : 2.0) * Math.PI),
          p.y() / height);

    case CONE_SURFACE_BASE:
      return new Point2(0.5 + (p.x() + radius) / (4.0 * radius),
          (p.z() + radius) / (4.0 * radius));

    default:
      throw new IllegalArgumentException("Invalid surface ID");
    }
  }

  @Override
  public Box3 boundingBox() {
    return new Box3(
        base.x() - radius,
        base.y(),
        base.z() - radius,
        base.x() + radius,
        base.y() + height,
        base.z() + radius);
  }

  @Override
  public Sphere boundingSphere() {
    double yc = (height * height - radius * radius) / (2.0 * height);
    return yc > 0.0 ? new Sphere(new Point3(0, yc, 0), height - yc)
        : new Sphere(Point3.ORIGIN, radius);
  }

}
