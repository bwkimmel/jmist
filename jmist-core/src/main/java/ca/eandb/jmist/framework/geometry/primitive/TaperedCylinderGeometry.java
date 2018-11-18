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
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Polynomial;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

public final class TaperedCylinderGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = -6353821613593755723L;

  /** The radius of the second end of the tapered cylinder */
  private final double radius1;

  /** The radius of the first end of the tapered cylinder */
  private final double radius2;

  /** The height of the first end of the tapered cylinder */
  private final double height1;

  /** The height of the second end of the tapered cylinder */
  private final double height2;

  /** A value indicating whether the ends of the cylinder are rendered. */
  private final boolean capped;

  /** The surface ID for the body of the tapered cylinder. */
  private static final int TAPERED_CYLINDER_SURFACE_BODY = 0;

  /** The surface ID for the first end of the tapered cylinder. */
  private static final int TAPERED_CYLINDER_SURFACE_END_1 = 1;

  /** The surface ID for the second end of the tapered cylinder. */
  private static final int TAPERED_CYLINDER_SURFACE_END_2 = 2;

  public TaperedCylinderGeometry(double height1, double radius1, double height2, double radius2, boolean capped) {
    this.height1 = height1;
    this.radius1 = radius1;
    this.height2 = height2;
    this.radius2 = radius2;
    this.capped = capped;
  }

  @Override
  public Sphere boundingSphere() {
    Point3 center = new Point3(0.0, 0.5 * (height1 + height2), 0.0);
    double height = Math.abs(height1 - height2);
    double maxR = Math.max(radius1, radius2);
    double radius = Math.sqrt(maxR * maxR + 0.25 * height * height);
    return new Sphere(center, radius);
  }

  @Override
  public Box3 boundingBox() {
    double maxR = Math.max(radius1, radius2);
    double minH = Math.min(height1, height2);
    double maxH = Math.max(height1, height2);

    return new Box3(-maxR, minH, -maxR, maxR, maxH, maxR);
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    double dr = radius2 - radius1;
    double dh = height2 - height1;
    double det = radius1 * height2 - radius2 * height1;

//    if (capped) {
//      double t = Plane3.XZ.intersect(ray);
//
//      if (recorder.interval().contains(t)) {
//        Point3 p = ray.pointAt(t);
//        if (p.distanceToOrigin() <= radius) {
//          Intersection x = super.newIntersection(ray, t, ray.origin().y() < 0.0, TAPERED_CYLINDER_SURFACE_BASE)
//            .setLocation(p);
//          recorder.record(x);
//        }
//      }
//    }

    double x0 = ray.origin().x() * dh;
    double y0 = ray.origin().y() * dr + det;
    double z0 = ray.origin().z() * dh;
    double x1 = ray.direction().x() * dh;
    double y1 = ray.direction().y() * dr;
    double z1 = ray.direction().z() * dh;

    Polynomial f = new Polynomial(
        x0 * x0 - y0 * y0 + z0 * z0,
        2.0 * (x0 * x1 - y0 * y1 + z0 * z1),
        x1 * x1 - y1 * y1 + z1 * z1);

    double[] t = f.roots();

    if (t.length == 2) {
      for (int i = 0; i < 2; i++) {
        if (recorder.interval().contains(t[i])) {
          Point3 p = ray.pointAt(t[i]);
          if (MathUtil.inRangeCC(p.y(),
              Math.min(height1, height2),
              Math.max(height1, height2))) {
            Intersection x = super.newIntersection(ray, t[i], i == 0, TAPERED_CYLINDER_SURFACE_BODY)
              .setLocation(p);
            recorder.record(x);
          }
        }
      }
    }
  }

  @Override
  public double getSurfaceArea() {
    throw new UnsupportedOperationException();
//    double s = Math.hypot(radius, height);
//    return capped ? Math.PI * radius * radius + Math.PI * s * radius
//        : Math.PI * s * radius;
  }

  @Override
  protected Basis3 getBasis(GeometryIntersection x) {
    switch (x.getTag()) {
    case TAPERED_CYLINDER_SURFACE_BODY:
      return Basis3.fromWV(
          getNormal(x),
          x.getPosition().vectorTo(new Point3(0, height2, 0)));

    case TAPERED_CYLINDER_SURFACE_END_1:
    case TAPERED_CYLINDER_SURFACE_END_2:
      return Basis3.fromW(getNormal(x));

    default:
      throw new IllegalArgumentException("Invalid surface ID");
    }
  }

  @Override
  protected Vector3 getNormal(GeometryIntersection x) {
    switch (x.getTag()) {
    case TAPERED_CYLINDER_SURFACE_BODY:
      double dr = radius2 - radius1;
      double dh = height2 - height1;
      double dh2 = dh * dh;
      double det = radius1 * height2 - radius2 * height1;
      Point3 p = x.getPosition();

      return Vector3.unit(
          p.x() * dh2,
          -dr * (dr * p.y() + det),
          p.z() * dh2);
//
//      double dh = height2 - height1;
//      double dr = radius2 - radius1;
//      double side = Math.hypot(dr, dh);
//      double c = dr / side;
//      double s = dh / side;
//      double hyp = Math.hypot(p.x(), p.z());
//
//      return new Vector3(
//          s * p.x() / hyp,
//          c,
//          s * p.z() / hyp);

    case TAPERED_CYLINDER_SURFACE_END_1:
      return Vector3.NEGATIVE_J;

    case TAPERED_CYLINDER_SURFACE_END_2:
      return Vector3.J;

    default:
      throw new IllegalArgumentException("Invalid surface ID");
    }
  }

  @Override
  protected Point2 getTextureCoordinates(GeometryIntersection x) {
    Point3 p = x.getPosition();
    switch (x.getTag()) {
    case TAPERED_CYLINDER_SURFACE_BODY:
      double angle = Math.PI + Math.atan2(p.z(), p.x());
      return new Point2(angle / ((capped ? 4.0 : 2.0) * Math.PI),
          (p.y() - height1) / (height2 - height1));

    case TAPERED_CYLINDER_SURFACE_END_1:
      return new Point2(0.5 + (p.x() + radius1) / (4.0 * radius1),
          (p.z() + radius1) / (4.0 * radius1));

    case TAPERED_CYLINDER_SURFACE_END_2:
      return new Point2(0.5 + (p.x() + radius2) / (4.0 * radius2),
          (p.z() + radius2) / (4.0 * radius2));

    default:
      throw new IllegalArgumentException("Invalid surface ID");
    }
  }

}
