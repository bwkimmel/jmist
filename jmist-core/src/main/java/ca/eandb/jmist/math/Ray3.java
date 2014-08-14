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
 * A ray (half-line) in three dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Ray3 implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 599796092151860220L;

  /**
   * Creates a <code>Ray3</code>.
   * @param origin The origin of the ray.
   * @param direction The direction of the ray.
   * @param limit The maximum units along the ray.
   */
  public Ray3(Point3 origin, Vector3 direction, double limit) {
    this.origin = origin;
    this.direction = direction;
    this.limit = limit;
  }

  /**
   * Creates a <code>Ray3</code> with no limit.
   * @param origin The origin of the ray.
   * @param direction The direction of the ray.
   */
  public Ray3(Point3 origin, Vector3 direction) {
    this(origin, direction, Double.POSITIVE_INFINITY);
  }

  /**
   * Creates a <code>Ray3</code> spanning from one point to another.
   * @param p The <code>Point3</code> at the origin of the ray.
   * @param q The <code>Point3</code> at the end of the ray.
   */
  public Ray3(Point3 p, Point3 q) {
    this.limit = p.distanceTo(q);
    this.origin = p;
    this.direction = p.vectorTo(q).divide(limit);
  }

  /**
   * Creates a <code>Ray3</code> from one point along a direction or to
   * another point.
   * @param p The <code>Point3</code> at the origin of the ray.
   * @param q The <code>HPoint3</code> indicating the direction and limit of
   *     the ray.  If <code>q</code> is a <code>Point3</code>, this creates
   *     a finite ray from <code>p</code> to <code>q</code>.  If
   *     <code>q</code> is a <code>Vector3</code>, this creates an infinite
   *     ray from <code>p</code> in the direction indicated by
   *     <code>q</code>.
   */
  public Ray3(Point3 p, HPoint3 q) {
    this.origin = p;
    if (q.isPoint()) {
      this.limit = p.distanceTo((Point3) q);
      this.direction = p.vectorTo((Point3) q).divide(limit);
    } else {
      this.direction = (Vector3) q;
      this.limit = Double.POSITIVE_INFINITY;
    }
  }

  /**
   * Creates a <code>Ray3</code> spanning two <code>HPoint3</code>s.  If both
   * are <code>Point3</code>s, this is equivalent to
   * <code>new Ray3((Point3) p, (Point3) q)</code>.  If one is a
   * <code>Point3</code> and one is a <code>Vector3</code>, this is
   * equivalent to
   * <code>new Ray3((Point3) thePoint, (Vector3) theVector)</code>.  If both
   * are <code>Vector3</code>s, the return value is <code>null</code>.
   * @param p The first <code>HPoint3</code>.
   * @param q The second <code>HPoint3</code>.
   * @return A <code>Ray3</code> spanning <code>p</code> and <code>q</code>,
   *     or <code>null</code> if <code>p</code> and <code>q</code> are both
   *     <code>Vector3</code>s.
   * @see Point3
   * @see Vector3
   * @see #Ray3(Point3, Point3)
   * @see #Ray3(Point3, Vector3)
   */
  public static Ray3 create(HPoint3 p, HPoint3 q) {
    if (p.isPoint()) {
      return new Ray3(p.toPoint3(), q);
    } else if (q.isPoint()) {
      return new Ray3(q.toPoint3(), p.toVector3());
    } else {
      return null;
    }
  }

  /**
   * Gets the origin of this ray.
   * @return The origin of this ray.
   */
  public Point3 origin() {
    return origin;
  }

  /**
   * Gets the direction of this ray.
   * @return The direction of this ray.
   */
  public Vector3 direction() {
    return direction;
  }

  /**
   * Gets the maximum units along the ray.
   * @return The maximum units along the ray.
   */
  public double limit() {
    return limit;
  }

  /**
   * Gets a value indicating if this ray is infinite.  Equivalent to
   * <code>Double.isInfinite(this.limit())</code>.
   * @return A value indicating if this ray is infinite.
   * @see #limit()
   */
  public boolean isInfinite() {
    return Double.isInfinite(limit);
  }

  /**
   * Gets a parallel <code>Ray3</code> with the origin <code>t</code> units
   * along this <code>Ray3</code>.
   * @param t The number of units along the ray to advance.
   * @return The new <code>Ray3</code>.
   */
  public Ray3 advance(double t) {
    return new Ray3(pointAt(t), direction, limit - t);
  }

  /**
   * Gets the point that is t units along the ray.
   * @param t The number of units along the ray to find the point of.
   * @return The point that is t units along the ray.
   */
  public Point3 pointAt(double t) {
    return new Point3(
        origin.x() + t * direction.x(),
        origin.y() + t * direction.y(),
        origin.z() + t * direction.z()
    );
  }

  /**
   * Gets the end point of this ray.
   * Equivalent to <code>this.pointAt(this.limit())</code>.
   * @return The end point of this ray.
   * @see #pointAt(double)
   * @see #limit()
   */
  public Point3 pointAtLimit() {
    return new Point3(
        origin.x() + limit * direction.x(),
        origin.y() + limit * direction.y(),
        origin.z() + limit * direction.z()
    );
  }

  /**
   * Transforms this <code>Ray3</code> according to the specified
   * transformation matrix.
   * @param T The <code>AffineMatrix3</code> representing the transformation
   *     to apply.
   * @return The transformed <code>Ray3</code>.
   */
  public Ray3 transform(AffineMatrix3 T) {
    return new Ray3(
        T.times(origin),
        T.times(direction),
        limit
    );
  }

  /** The origin of the ray. */
  private final Point3 origin;

  /** The direction of the ray. */
  private final Vector3 direction;

  /** The maximum units along the ray. */
  private final double limit;

}
