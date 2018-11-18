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
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A spherical <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class SphereGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3863465919049151682L;

  /**
   * Creates a new <code>SphereGeometry</code>.
   * @param sphere The <code>Sphere</code> describing to be rendered.
   */
  public SphereGeometry(Sphere sphere) {
    this.sphere = sphere;
  }

  /**
   * Creates a new <code>SphereGeometry</code>.
   * @param center The <code>Point3</code> at the center of the sphere.
   * @param radius The radius of the sphere.
   */
  public SphereGeometry(Point3 center, double radius) {
    this.sphere = new Sphere(center, radius);
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    Interval I = this.sphere.intersect(ray);
    if (!I.isEmpty()) {
      recorder.record(super.newIntersection(ray, I.minimum(), true));
      recorder.record(super.newIntersection(ray, I.maximum(), false));
    }
  }

  @Override
  protected Basis3 getBasis(GeometryIntersection x) {
    return Basis3.fromWV(x.getNormal(), Vector3.NEGATIVE_J, Basis3.Orientation.LEFT_HANDED);
  }

  @Override
  protected Vector3 getNormal(GeometryIntersection x) {
    return this.sphere.normalAt(x.getPosition());
  }

  @Override
  protected Point2 getTextureCoordinates(GeometryIntersection x) {
    Vector3 n = x.getNormal();
    SphericalCoordinates sc = SphericalCoordinates.fromCartesian(new Vector3(n.x(), -n.z(), n.y()));
    return new Point2(
        (Math.PI + sc.azimuthal()) / (2.0 * Math.PI),
        sc.polar() / Math.PI
    );
  }

  @Override
  public Box3 boundingBox() {
    return this.sphere.boundingBox();
  }

  @Override
  public Sphere boundingSphere() {
    return this.sphere;
  }

  @Override
  public boolean intersects(Box3 box) {
    boolean foundCornerInside = false;
    boolean foundCornerOutside = false;
    for (int i = 0; i < 8; i++) {
      if (this.sphere.contains(box.corner(i))) {
        foundCornerInside = true;
      } else {
        foundCornerOutside = true;
      }
      if (foundCornerInside && foundCornerOutside) {
        return true;
      }
    }

    if (box.contains(this.sphere.center().plus(Vector3.I.times(this.sphere.radius())))) {
      return true;
    }

    if (box.contains(this.sphere.center().plus(Vector3.J.times(this.sphere.radius())))) {
      return true;
    }

    if (box.contains(this.sphere.center().plus(Vector3.K.times(this.sphere.radius())))) {
      return true;
    }

    if (box.contains(this.sphere.center().plus(Vector3.NEGATIVE_I.times(this.sphere.radius())))) {
      return true;
    }

    if (box.contains(this.sphere.center().plus(Vector3.NEGATIVE_J.times(this.sphere.radius())))) {
      return true;
    }

    if (box.contains(this.sphere.center().plus(Vector3.NEGATIVE_K.times(this.sphere.radius())))) {
      return true;
    }

    return false;
  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
    Point3 p = sphere.center().plus(RandomUtil.uniformOnSphere(sphere.radius(), ru, rv).toCartesian());
    Intersection x = newSurfacePoint(p);
    x.prepareShadingContext(context);
  }

  @Override
  public double getSurfaceArea() {
    return sphere.surfaceArea();
  }

  /** The <code>Sphere</code> describing this <code>SceneElement</code>. */
  private final Sphere sphere;

}
