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
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * An axis aligned box <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class BoxGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = 2733253411334817090L;

  /**
   * Creates a new <code>BoxGeometry</code>.
   * @param box The axis aligned <code>Box3</code> to be rendered.
   */
  public BoxGeometry(Box3 box) {
    this.box = box;
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {

    // Check for an empty box.
    if (!this.box.isEmpty()) {

      assert(ray.direction().x() != 0.0 || ray.direction().y() != 0.0 || ray.direction().z() != 0.0);

      double    t;
      int      n = 0;
      Point3    p;

      // Check for intersection with each of the six sides of the box.
      if (ray.direction().x() != 0.0) {
        t = (box.minimumX() - ray.origin().x()) / ray.direction().x();
        if (t > 0.0) {
          p = ray.pointAt(t);
          if (box.minimumY() < p.y() && p.y() < box.maximumY() && box.minimumZ() < p.z() && p.z() < box.maximumZ()) {
            Intersection x = super.newIntersection(ray, t, ray.direction().x() > 0.0, BOX_SURFACE_MIN_X)
              .setLocation(p);
            recorder.record(x);
            if (++n == 2) return;
          }
        }

        t = (box.maximumX() - ray.origin().x()) / ray.direction().x();
        if (t > 0.0) {
          p = ray.pointAt(t);
          if (box.minimumY() < p.y() && p.y() < box.maximumY() && box.minimumZ() < p.z() && p.z() < box.maximumZ()) {
            Intersection x = super.newIntersection(ray, t, ray.direction().x() < 0.0, BOX_SURFACE_MAX_X)
              .setLocation(p);
            recorder.record(x);
            if (++n == 2) return;
          }
        }
      }

      if (ray.direction().y() != 0.0) {
        t = (box.minimumY() - ray.origin().y()) / ray.direction().y();
        if (t > 0.0) {
          p = ray.pointAt(t);
          if (box.minimumX() < p.x() && p.x() < box.maximumX() && box.minimumZ() < p.z() && p.z() < box.maximumZ()) {
            Intersection x = super.newIntersection(ray, t, ray.direction().y() > 0.0, BOX_SURFACE_MIN_Y)
              .setLocation(p);
            recorder.record(x);
            if (++n == 2) return;
          }
        }

        t = (box.maximumY() - ray.origin().y()) / ray.direction().y();
        if (t > 0.0) {
          p = ray.pointAt(t);
          if (box.minimumX() < p.x() && p.x() < box.maximumX() && box.minimumZ() < p.z() && p.z() < box.maximumZ()) {
            Intersection x = super.newIntersection(ray, t, ray.direction().y() < 0.0, BOX_SURFACE_MAX_Y)
              .setLocation(p);
            recorder.record(x);
            if (++n == 2) return;
          }
        }
      }

      if (ray.direction().z() != 0.0) {
        t = (box.minimumZ() - ray.origin().z()) / ray.direction().z();
        if (t > 0.0) {
          p = ray.pointAt(t);
          if (box.minimumX() < p.x() && p.x() < box.maximumX() && box.minimumY() < p.y() && p.y() < box.maximumY()) {
            Intersection x = super.newIntersection(ray, t, ray.direction().z() > 0.0, BOX_SURFACE_MIN_Z)
              .setLocation(p);
            recorder.record(x);
            if (++n == 2) return;
          }
        }

        t = (box.maximumZ() - ray.origin().z()) / ray.direction().z();
        if (t > 0.0) {
          p = ray.pointAt(t);
          if (box.minimumX() < p.x() && p.x() < box.maximumX() && box.minimumY() < p.y() && p.y() < box.maximumY()) {
            Intersection x = super.newIntersection(ray, t, ray.direction().z() < 0.0, BOX_SURFACE_MAX_Z)
              .setLocation(p);
            recorder.record(x);
            if (++n == 2) return;
          }
        }
      }

    }

  }

  @Override
  protected Basis3 getBasis(GeometryIntersection x) {
    return Basis3.fromW(x.getNormal(), Basis3.Orientation.RIGHT_HANDED);
  }

  @Override
  protected Vector3 getNormal(GeometryIntersection x) {
    switch (x.getTag())
    {
    case BOX_SURFACE_MAX_X:  return Vector3.I;
    case BOX_SURFACE_MIN_X:  return Vector3.NEGATIVE_I;
    case BOX_SURFACE_MAX_Y:  return Vector3.J;
    case BOX_SURFACE_MIN_Y:  return Vector3.NEGATIVE_J;
    case BOX_SURFACE_MAX_Z:  return Vector3.K;
    case BOX_SURFACE_MIN_Z:  return Vector3.NEGATIVE_K;
    default:        assert(false); return null;
    }
  }

  @Override
  protected Point2 getTextureCoordinates(GeometryIntersection x) {

    Point2  facePoint;
    Point3  p = x.getPosition();

    switch (x.getTag())
    {
    case BOX_SURFACE_MAX_X:
      facePoint = new Point2(
          (box.maximumZ() - p.z()) / box.lengthX(),
          (box.maximumY() - p.y()) / box.lengthY()
      );
      break;

    case BOX_SURFACE_MIN_X:
      facePoint = new Point2(
          (p.z() - box.minimumZ()) / box.lengthZ(),
          (box.maximumY() - p.y()) / box.lengthY()
      );
      break;

    case BOX_SURFACE_MAX_Y:
      facePoint = new Point2(
          (p.x() - box.minimumX()) / box.lengthX(),
          (p.z() - box.minimumZ()) / box.lengthZ()
      );
      break;

    case BOX_SURFACE_MIN_Y:
      facePoint = new Point2(
          (p.x() - box.minimumX()) / box.lengthX(),
          (box.maximumZ() - p.z()) / box.lengthZ()
      );
      break;

    case BOX_SURFACE_MAX_Z:
      facePoint = new Point2(
          (p.x() - box.minimumX()) / box.lengthX(),
          (box.maximumY() - p.y()) / box.lengthY()
      );
      break;

    case BOX_SURFACE_MIN_Z:
      facePoint = new Point2(
          (p.x() - box.minimumX()) / box.lengthX(),
          (p.y() - box.minimumY()) / box.lengthY()
      );
      break;

    default:
      throw new IllegalArgumentException("invalid surface id");

    }

    return new Point2(
      FACE_DOMAIN[x.getTag()].minimumX() + facePoint.x() * FACE_DOMAIN[x.getTag()].lengthX(),
      FACE_DOMAIN[x.getTag()].minimumY() + facePoint.y() * FACE_DOMAIN[x.getTag()].lengthY()
    );

  }

  @Override
  public Box3 boundingBox() {
    return this.box;
  }

  @Override
  public Sphere boundingSphere() {
    return new Sphere(this.box.center(), this.box.diagonal() / 2.0);
  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
    double xyArea = box.lengthX() * box.lengthY();
    double xzArea = box.lengthX() * box.lengthZ();
    double yzArea = box.lengthY() * box.lengthZ();

    SeedReference ref = new SeedReference(rj);
    double total = xyArea + xzArea + yzArea;
    boolean dir = RandomUtil.coin(ref);
    int id;
    Point3 p;

    ref.seed *= total;

    if (ref.seed < xyArea) {
      id = dir ? BOX_SURFACE_MAX_Z : BOX_SURFACE_MIN_Z;
      p = new Point3(
          RandomUtil.uniform(box.spanX(), ru),
          RandomUtil.uniform(box.spanY(), rv),
          dir ? box.maximumZ() : box.minimumZ());
    } else if (ref.seed < xyArea + xzArea) {
      id = dir ? BOX_SURFACE_MAX_Y : BOX_SURFACE_MIN_Y;
      p = new Point3(RandomUtil.uniform(box.spanX(), ru),
          dir ? box.maximumY() : box.minimumY(),
          RandomUtil.uniform(box.spanZ(), rv));

    } else {
      id = dir ? BOX_SURFACE_MAX_X : BOX_SURFACE_MIN_X;
      p = new Point3(dir ? box.maximumX() : box.minimumX(),
          RandomUtil.uniform(box.spanY(), ru),
          RandomUtil.uniform(box.spanZ(), rv));
    }

    Intersection x = newSurfacePoint(p, id);
    x.prepareShadingContext(context);
  }

  @Override
  public double getSurfaceArea() {
    return box.surfaceArea();
  }

  /** The surface id for the side facing toward the positive x-axis. */
  private static final int BOX_SURFACE_MAX_X = 0;

  /** The surface id for the side facing toward the negative x-axis. */
  private static final int BOX_SURFACE_MIN_X = 1;

  /** The surface id for the side facing toward the positive y-axis. */
  private static final int BOX_SURFACE_MAX_Y = 2;

  /** The surface id for the side facing toward the negative y-axis. */
  private static final int BOX_SURFACE_MIN_Y = 3;

  /** The surface id for the side facing toward the positive z-axis. */
  private static final int BOX_SURFACE_MAX_Z = 4;

  /** The surface id for the side facing toward the negative z-axis. */
  private static final int BOX_SURFACE_MIN_Z = 5;

  /**
   * The <code>Box2</code>s that each face on the cube map to in texture
   * coordinate space.
   */
  private static final Box2 FACE_DOMAIN[] = {
      new Box2(2.0 / 3.0, 1.0 / 4.0, 1.0, 1.0 / 2.0),
      new Box2(0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0),
      new Box2(1.0 / 3.0, 0.0, 2.0 / 3.0, 1.0 / 4.0),
      new Box2(1.0 / 3.0, 1.0 / 2.0, 2.0 / 3.0, 3.0 / 4.0),
      new Box2(1.0 / 3.0, 1.0 / 4.0, 2.0 / 3.0, 1.0 / 2.0),
      new Box2(1.0 / 3.0, 3.0 / 4.0, 2.0 / 3.0, 1.0)
  };

  /** The <code>Box3</code> that represents this <code>SceneElement</code>. */
  private final Box3 box;

}
