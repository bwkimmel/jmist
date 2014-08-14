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

import java.util.Arrays;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Polynomial;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * A torus primitive <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class TorusGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = 8573316243171501395L;

  /**
   * Creates a new <code>TorusGeometry</code>.
   * @param major The major radius of the torus (i.e., the distance from the
   *     center of the torus to a point in the center of the tube.
   * @param minor The minor radius of the torus (i.e., the radius of the
   *     tube).
   */
  public TorusGeometry(double major, double minor) {
    this.major = major;
    this.minor = minor;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {

    Vector3    orig      = ray.origin().vectorFromOrigin();
    Vector3    dir        = ray.direction().unit();
    double    sqRadius1    = major * major;
    double    sqRadius2    = minor * minor;
    double    s2NormOfDir    = dir.squaredLength();
    double    s2NormOfOrig  = orig.squaredLength();
    double    dirDotOrig    = dir.dot(orig);
    double    K        = s2NormOfOrig - (sqRadius1 + sqRadius2);

    Polynomial  f = new Polynomial(
              K * K - 4.0 * sqRadius1 * (sqRadius2 - orig.y() * orig.y()),
              4.0 * dirDotOrig * (s2NormOfOrig - (sqRadius1 + sqRadius2)) + 8.0 * sqRadius1 * dir.y() * orig.y(),
              2.0 * s2NormOfDir * (s2NormOfOrig - (sqRadius1 + sqRadius2)) + 4.0 * ((dirDotOrig * dirDotOrig) + sqRadius1 * dir.y() * dir.y()),
              4.0 * dirDotOrig * s2NormOfDir,
              s2NormOfDir * s2NormOfDir
          );

    double[]  x = f.roots();

    if (x.length > 1)
    {
      Arrays.sort(x);
      for (int i = 0; i < x.length; i++)
        recorder.record(super.newIntersection(ray, x[i], i % 2 == 0));
    }

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AbstractGeometry#getNormal(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
   */
  @Override
  protected Vector3 getNormal(GeometryIntersection x) {

    Point3  p = x.getPosition();
    Vector3  rel = new Vector3(p.x(), 0.0, p.z());

    double  length = rel.length();

    if (length > 0.0)
    {
      rel = rel.times(major / length);
      return p.vectorFrom(Point3.ORIGIN.plus(rel));
    }
    else
      return Vector3.K;

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AbstractGeometry#getBasis(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
   */
  @Override
  protected Basis3 getBasis(GeometryIntersection x) {

    Point3  p  = x.getPosition();
    Vector3  u  = new Vector3(-p.z(), 0.0, p.x()).unit();

    return Basis3.fromWU(x.getNormal(), u, Basis3.Orientation.RIGHT_HANDED);

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AbstractGeometry#getTextureCoordinates(ca.eandb.jmist.framework.AbstractGeometry.GeometryIntersection)
   */
  @Override
  protected Point2 getTextureCoordinates(GeometryIntersection x) {

    Vector3  cp  = x.getPosition().vectorFromOrigin();
    Vector3  R  = Vector3.unit(cp.x(), 0.0, cp.z());
    Vector3  r  = cp.minus(R.times(major)).unit();

    return new Point2(
      (Math.PI + Math.atan2(-cp.z(), cp.x())) / (2.0 * Math.PI),
      (Math.PI + Math.atan2(cp.y(), R.dot(r))) / (2.0 * Math.PI)
    );

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
   */
  public Box3 boundingBox() {
    return new Box3(
        -(major + minor), -minor, -(major + minor),
          major + minor ,  minor,   major + minor
    );
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
   */
  public Sphere boundingSphere() {
    return new Sphere(Point3.ORIGIN, major + minor);
  }

  /**
   * The major radius of the torus (i.e., the distance from the center of the
   * torus to a point in the center of the tube.
   */
  private final double major;

  /**
   * The minor radius of the torus (i.e., the radius of the tube).
   */
  private final double minor;

}
