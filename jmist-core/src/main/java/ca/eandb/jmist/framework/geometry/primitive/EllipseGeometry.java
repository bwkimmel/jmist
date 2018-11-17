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
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector2;
import ca.eandb.jmist.math.Vector3;

/**
 * An elliptical plane <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class EllipseGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = 7682212284130537132L;

  /**
   * Creates a new <code>EllipseGeometry</code>.
   * @param center The <code>Point3</code> at the center of the ellipse.
   * @param basis The <code>Basis3</code> indicating the orientation of the
   *     ellipse.
   * @param ru The radius of the ellipse (in meters) along the direction of the
   *     u basis vector.
   * @param rv The radius of the ellipse (in meters) along the direction of the
   *     v basis vector.
   * @param twoSided A value indicating whether the ellipse is two sided.
   */
  public EllipseGeometry(Point3 center, Basis3 basis, double ru, double rv, boolean twoSided) {
    this.plane = Plane3.throughPoint(center, basis.w());
    this.center = center;
    this.basis = basis;
    this.ru = ru;
    this.rv = rv;
    this.twoSided = twoSided;
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    boolean fromTop = this.plane.altitude(ray.origin()) > 0.0;

    if (!twoSided && !fromTop)
      return;

    double t = this.plane.intersect(ray);

    if (recorder.interval().contains(t)) {
      Point3 p = ray.pointAt(t);
      Vector3 r = center.vectorTo(p);

      double rdotu = r.dot(basis.u()) / ru;
      double rdotv = r.dot(basis.v()) / rv;

      if (Math.hypot(rdotu, rdotv) < 1.0) {
        GeometryIntersection x = super.newIntersection(ray, t, true, fromTop ? ELLIPSE_SURFACE_TOP : ELLIPSE_SURFACE_BOTTOM)
                                      .setLocation(p)
                                      .setUV(new Point2(0.5 * (1.0 + rdotu), 0.5 * (1.0 + rdotv)));

        recorder.record(x);
      }
    }
  }

  @Override
  protected Basis3 getBasis(GeometryIntersection x) {
    switch (x.getTag()) {
    case ELLIPSE_SURFACE_TOP: return Basis3.fromW(this.plane.normal(), Basis3.Orientation.RIGHT_HANDED);
    case ELLIPSE_SURFACE_BOTTOM: return Basis3.fromW(this.plane.normal().opposite(), Basis3.Orientation.RIGHT_HANDED);
    default: assert(false); return null;
    }
  }

  @Override
  protected Vector3 getNormal(GeometryIntersection x) {
    switch (x.getTag()) {
    case ELLIPSE_SURFACE_TOP: return this.plane.normal();
    case ELLIPSE_SURFACE_BOTTOM: return this.plane.normal().opposite();
    default: assert(false); return null;
    }
  }

  @Override
  protected Point2 getTextureCoordinates(GeometryIntersection x) {
    Basis3 basis = x.getBasis();
    Vector3 r = x.getPosition().vectorFrom(center);

    return new Point2(
        0.5 * (1.0 + r.dot(basis.u()) / ru),
        0.5 * (1.0 + r.dot(basis.v()) / rv)
    );
  }

  @Override
  public Box3 boundingBox() {
    Vector3 u = basis.u();
    Vector3 v = basis.v();

    double ri = Math.hypot(ru * u.x(), rv * v.x());
    double rj = Math.hypot(ru * u.y(), rv * v.y());
    double rk = Math.hypot(ru * u.z(), rv * v.z());

    return new Box3(
        center.x() - ri,
        center.y() - rj,
        center.z() - rk,
        center.x() + ri,
        center.y() + rj,
        center.z() + rk
    );
  }

  @Override
  public Sphere boundingSphere() {
    return new Sphere(center, Math.max(ru, rv));
  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double xu, double xv, double xj) {
    Vector2 uv = RandomUtil.uniformOnDisc(1.0, xu, xv).toCartesian();

    Point3 p = center.plus(basis.u().times(ru * uv.x()))
                     .plus(basis.v().times(rv * uv.y()));

    int id = (twoSided && RandomUtil.coin(xj))
        ? ELLIPSE_SURFACE_BOTTOM
        : ELLIPSE_SURFACE_TOP;

    Intersection x = newSurfacePoint(p, id);
    x.prepareShadingContext(context);
  }

  @Override
  public double getSurfaceArea() {
    return (twoSided ? 2.0 : 1.0) * Math.PI * ru * rv;
  }

  /**
   * The surface ID for the top of the disc (the side toward which the normal
   * points.
   */
  private static final int ELLIPSE_SURFACE_TOP = 0;

  /**
   * The surface ID for the bottom of the disc (the side away from which the
   * normal points.
   */
  private static final int ELLIPSE_SURFACE_BOTTOM = 1;

  /**
   * The <code>Plane3</code> in which this <code>EllipseGeometry</code> lies.
   */
  private final Plane3 plane;

  /** The center of the ellipse. */
  private final Point3 center;

  /** The orientation of the ellipse. */
  private final Basis3 basis;

  /** The radius of the ellipse in the direction of the u basis vector. */
  private final double ru;

  /** The radius of the ellipse in the direction of the v basis vector. */
  private final double rv;

  /** A value indicating whether this disc is two sided. */
  private final boolean twoSided;

}
