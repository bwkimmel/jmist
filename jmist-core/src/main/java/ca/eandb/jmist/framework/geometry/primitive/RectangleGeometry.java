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

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * A plane rectangle <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class RectangleGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = -7544786348196730015L;

  /**
   * The surface id for the side of the rectangle toward which the normal
   * points.
   */
  private static final int RECTANGLE_SURFACE_TOP = 0;

  /**
   * The surface id for the side of the rectangle away from which the normal
   * points.
   */
  private static final int RECTANGLE_SURFACE_BOTTOM = 1;

  /** The <code>Plane3</code> on which this rectangle lies. */
  private final Plane3 plane;

  /** The <code>Point3</code> at the center of this rectangle. */
  private final Point3 center;

  /**
   * The orthonormal <code>Basis3</code> describing the orientation of this
   * rectangle.
   */
  private final Basis3 basis;

  /**
   * Half the length of this rectangle along the side parallel to the first
   * tangent vector (<code>this.basis.u()</code>).
   */
  private final double ru;

  /**
   * Half the length of this rectangle along the side parallel to the second
   * tangent vector (<code>this.basis.v()</code>).
   */
  private final double rv;

  /** A value indicating whether this rectangle is two sided. */
  private final boolean twoSided;

  /**
   * Creates a new <code>RectangleGeometry</code>.
   * @param center The <code>Point3</code> at the center of the rectangle.
   * @param basis The <code>Basis3</code> describing the orientation of the
   *     rectangle.
   * @param su The length of the side of the rectangle along the axis
   *     parallel to <code>basis.u()</code>.
   * @param sv The length of the side of the rectangle along the axis
   *     parallel to <code>basis.v()</code>.
   * @param twoSided A value indicating whether the rectangle is two sided.
   * @see Basis3#u()
   * @see Basis3#v()
   */
  public RectangleGeometry(Point3 center, Basis3 basis, double su, double sv, boolean twoSided) {
    this.plane = Plane3.throughPoint(center, basis);
    this.center = center;
    this.basis = basis;
    this.ru = su / 2.0;
    this.rv = sv / 2.0;
    this.twoSided = twoSided;
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    boolean  fromTop = ray.direction().dot(plane.normal()) < 0.0;
    if (!twoSided && !fromTop)
      return;

    double t = this.plane.intersect(ray);
    if (recorder.interval().contains(t)) {
      Point3 p = ray.pointAt(t);
      Vector3 dp = p.vectorFrom(this.center);

      double u = 0.5 + 0.5 * dp.dot(basis.u()) / ru;
      double v = 0.5 + 0.5 * dp.dot(basis.v()) / rv;
      if (MathUtil.inRangeCC(u, 0.0, 1.0) && MathUtil.inRangeCC(v, 0.0, 1.0)) {
        // If the rectangle is two sided, adjust the 2D inversion to include
        // the information about which side was intersected (i.e., each side
        // of the rectangle will use one half of the texture map).
        if (twoSided) {
          u /= 2.0;
          if (!fromTop)
            u += 0.5;
        }

        Intersection x = super.newIntersection(ray, t, true, fromTop ? RECTANGLE_SURFACE_TOP : RECTANGLE_SURFACE_BOTTOM)
            .setLocation(p)
            .setUV(new Point2(u, v));
        recorder.record(x);
      }
    }
  }

  @Override
  protected Basis3 getBasis(GeometryIntersection x) {
    switch (x.getTag()) {
    case RECTANGLE_SURFACE_TOP: return this.basis;
    case RECTANGLE_SURFACE_BOTTOM: return this.basis.opposite();
    default: throw new IllegalArgumentException("invalid surface id");
    }
  }

  @Override
  protected Vector3 getNormal(GeometryIntersection x) {
    switch (x.getTag()) {
    case RECTANGLE_SURFACE_TOP: return this.plane.normal();
    case RECTANGLE_SURFACE_BOTTOM: return this.plane.normal().opposite();
    default: throw new IllegalArgumentException("invalid surface id");
    }
  }

  @Override
  public Box3 boundingBox() {
    BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
    builder.add(center.plus(basis.u().times( ru)).plus(basis.v().times( rv)));
    builder.add(center.plus(basis.u().times( ru)).plus(basis.v().times(-rv)));
    builder.add(center.plus(basis.u().times(-ru)).plus(basis.v().times( rv)));
    builder.add(center.plus(basis.u().times(-ru)).plus(basis.v().times(-rv)));
    return builder.getBoundingBox();
  }

  @Override
  public Sphere boundingSphere() {
    return new Sphere(this.center, Math.sqrt(ru * ru * rv * rv));
  }

//  /* (non-Javadoc)
//   * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.VisibilityFunction3, ca.eandb.jmist.framework.Illuminable)
//   */
//  public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
//      Illuminable target) {
//    /* Pick a point at random on the surface of the rectangle. */
//    double u = 2.0 * random.next() - 1.0;
//    double v = 2.0 * random.next() - 1.0;
//
//    Point3 p = center.plus(basis.u().times(u * ru)).plus(basis.v().times(v * rv));
//
//    /* Check for visibility between the point being illuminated and the
//     * point on the rectangle.
//     */
//    if (vf.visibility(p, x.location())) {
//      // FIXME Select from appropriate side when two-sided.
//      Intersection sp = super.newIntersection(null, 0.0, true, RECTANGLE_SURFACE_TOP)
//        .setLocation(p)
//        .setTextureCoordinates(new Point2(u, v)); // FIXME correct texture coordinates.
//
//      /* Compute the attenuation according to distance. */
//      Vector3 from = x.location().vectorTo(p);
//      double r = from.length();
//      double attenuation = (ru * rv) / (Math.PI * r * r);
//      from = from.divide(r);
//
//      /* Sample the material radiance. */
//      Color radiance = sp.material().emission(sp, from.opposite());
//
//      /* Illuminate the point. */
//      target.illuminate(from, radiance.times(attenuation));
//    }
//  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double xu, double xv, double xj) {
    Point3 p = center
        .plus(basis.u().times(RandomUtil.uniform(-ru, ru, xu)))
        .plus(basis.v().times(RandomUtil.uniform(-rv, rv, xv)));

    int id = (twoSided && RandomUtil.coin(xj))
        ? RECTANGLE_SURFACE_BOTTOM
        : RECTANGLE_SURFACE_TOP;

    Intersection x = newSurfacePoint(p, id);
    x.prepareShadingContext(context);
  }

  @Override
  public double getSurfaceArea() {
    return (twoSided ? 8.0 : 4.0) * ru * rv;
  }

}
