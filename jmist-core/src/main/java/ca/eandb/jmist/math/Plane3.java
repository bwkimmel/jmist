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
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A plane in three dimensional space.
 * @author Brad Kimmel
 */
public final class Plane3 implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5388824832351428994L;

  /** The x-y plane with the normal oriented in the +z direction. */
  public static final Plane3 XY = new Plane3(Vector3.K, 0.0);

  /** The x-z plane with the normal oriented in the +y direction. */
  public static final Plane3 ZX = new Plane3(Vector3.J, 0.0);

  /** The y-z plane with the normal oriented in the +x direction. */
  public static final Plane3 YZ = new Plane3(Vector3.I, 0.0);

  /** The x-y plane with the normal oriented in the -z direction. */
  public static final Plane3 YX = new Plane3(Vector3.NEGATIVE_K, 0.0);

  /** The x-z plane with the normal oriented in the -y direction. */
  public static final Plane3 XZ = new Plane3(Vector3.NEGATIVE_J, 0.0);

  /** The y-z plane with the normal oriented in the -x direction. */
  public static final Plane3 ZY = new Plane3(Vector3.NEGATIVE_I, 0.0);

  /**
   * Creates a <code>Plane3</code>.
   * @param normal The <code>Vector3</code> that is normal to the plane.
   * @param d The altitude of the origin.
   */
  private Plane3(Vector3 normal, double d) {
    this.normal = normal;
    this.d = d;
  }

  /**
   * Creates a <code>Plane3</code> that passes through a <code>Point3</code>
   * and has a given normal.
   * @param p A <code>Point3</code> on the plane.
   * @param normal A <code>Vector3</code> that is normal to the plane.
   * @return The new <code>Plane3</code>.
   */
  public static Plane3 throughPoint(Point3 p, Vector3 normal) {
    return new Plane3(normal, -normal.unit().dot(p.vectorFromOrigin()));
  }

  /**
   * Creates a <code>Plane3</code> that passes through a <code>Point3</code>
   * and has a given basis.
   * @param p A <code>Point3</code> on the plane.
   * @param basis A <code>Basis3</code> for the plane.  The normal is given
   *     by <code>basis.w()</code>.
   * @return The new <code>Plane3</code>.
   * @see Basis3#w()
   */
  public static Plane3 throughPoint(Point3 p, Basis3 basis) {
    return new Plane3(basis.w(), -basis.w().dot(p.vectorFromOrigin()));
  }

  /**
   * Gets the <code>Plane3</code> that passes through three
   * <code>Point3</code>s.
   * @param p0 The first <code>Point3</code>.
   * @param p1 The second <code>Point3</code>.
   * @param p2 The third <code>Point3</code>.
   * @return The <code>Plane3</code> that passes through three
   * <code>Point3</code>s.
   */
  public static Plane3 throughPoints(Point3 p0, Point3 p1, Point3 p2) {
    Vector3 u = p2.vectorTo(p0);
    Vector3 v = p0.vectorTo(p1);
    return throughPoint(p0, u.cross(v).unit());
  }

  /**
   * Creates a <code>Plane3</code> through the origin with a given normal.
   * @param normal The <code>Vector3</code> indicating the normal.
   * @return The new <code>Plane3</code>.
   */
  public static Plane3 throughOrigin(Vector3 normal) {
    return new Plane3(normal.unit(), 0.0);
  }

  /**
   * Creates a <code>Plane3</code> through the origin with a given normal.
   * @param basis A <code>Basis3</code> for the plane.  The normal is given
   *     by <code>basis.w()</code>.
   * @return The new <code>Plane3</code>.
   * @see Basis3#w()
   */
  public static Plane3 throughOrigin(Basis3 basis) {
    return new Plane3(basis.w(), 0.0);
  }

  /**
   * Gets the <code>Plane3</code> that is coincident with this
   * <code>Plane3</code> but has the opposite normal.
   * @return The opposite <code>Plane3</code>.
   */
  public Plane3 opposite() {
    return new Plane3(normal.opposite(), -d);
  }

  /**
   * Gets the <code>Vector3</code> that is normal to the plane.
   * @return The <code>Vector3</code> that is normal to the plane.
   */
  public Vector3 normal() {
    return this.normal;
  }

  /**
   * Computes the intersection of a <code>Ray3</code> with this plane.
   * @param ray The <code>Ray3</code> to intersect with this plane.
   * @return The ray parameter at which the ray intersects the plane (i.e.,
   *     <code>ray.pointAt(this.intersect(ray))</code> is a point on this
   *     plane.
   * @see Ray3#pointAt(double)
   */
  public double intersect(Ray3 ray) {
    return this.intersect(ray.origin(), ray.direction());
  }

  /**
   * Finds the <code>Point3</code> on this <code>Plane3</code> that is
   * nearest to the given <code>Point3</code>.
   * @param p The <code>Point3</code> to project onto this
   *     <code>Plane3</code>.
   * @return The <code>Point3</code> on this <code>Plane3</code> that is
   *     nearest to <code>p</code>.
   */
  public Point3 project(Point3 p) {
    return p.minus(this.normal.times(this.altitude(p)));
  }

  /**
   * Computes the distance from the given point to this <code>Plane3</code>.
   * Equivalent to <code>Math.abs(this.altitude(p))</code>.
   * @param p The <code>Point3</code> to get the distance to.
   * @return The distance from this <code>Plane3</code> to <code>p</code>.
   * @see #altitude(Point3)
   */
  public double distanceTo(Point3 p) {
    return Math.abs(this.altitude(p));
  }

  /**
   * Computes the multiple of the normal that needs to be subtracted from the
   * given <code>Point3</code> to obtain a <code>Point3</code> on this
   * <code>Plane3</code>.
   * @param p The <code>Point3</code> to compute the altitude of.
   * @return The multiple of the normal that needs to be subtracted from
   *     <code>p</code> to obtain a <code>Point3</code> on this
   *     <code>Plane3</code>.  This is the distance to <code>p</code> if
   *     <code>p</code> if on the side of this <code>Plane3</code> toward
   *     which {@link #normal()} points, or the negative of the distance to
   *     <code>p</code> if <code>p</code> is on the opposite side of this
   *     <code>Plane3</code> toward which {@link #normal()} points.
   * @see #distanceTo(Point3)
   * @see #normal()
   */
  public double altitude(Point3 p) {
    return d + p.vectorFromOrigin().dot(normal);
  }

  /**
   * Computes the intersection of three planes.
   * @param p1 The first <code>Plane3</code>.
   * @param p2 The second <code>Plane3</code>.
   * @param p3 The third <code>Plane3</code>.
   * @return A <code>Point3</code> that is on each of the planes.
   */
  public static Point3 intersection(Plane3 p1, Plane3 p2, Plane3 p3) {
    Vector3 n1 = p1.normal;
    Vector3 n2 = p2.normal;
    Vector3 n3 = p3.normal;
    AffineMatrix3 T = new AffineMatrix3(
        n1.x(), n1.y(), n1.z(), p1.d,
        n2.x(), n2.y(), n2.z(), p2.d,
        n3.x(), n3.y(), n3.z(), p3.d);
    T = T.inverse();
    return T.times(Point3.ORIGIN);
  }

  /**
   * Computes the value, <code>t</code>, where (p + tv)
   * (<code>p.plus(v.times(t))</code>) is on this <code>Plane3</code>.
   * @param p The <code>Point3</code>.
   * @param v The <code>Vector3</code>.
   * @return The value, <code>t</code>, where (p + tv)
   *     (<code>p.plus(v.times(t))</code>) is on this <code>Plane3</code>.
   * @see Point3#plus(Vector3)
   * @see Vector3#times(double)
   */
  private double intersect(Point3 p, Vector3 v) {
    return -(normal.dot(p.vectorFromOrigin()) + d)
        / normal.dot(v);
  }

  /** A <code>Vector3</code> that is normal to the plane. */
  private final Vector3 normal;

  /**
   * The altitude of {@link Point3#ORIGIN}.
   * @see Point3#ORIGIN
   */
  private final double d;

}
