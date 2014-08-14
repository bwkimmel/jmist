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
 * A ray (half-line) in two dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Ray2 implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 8111023479108425628L;

  /**
   * Creates a <code>Ray2</code>.
   * @param origin The origin of the ray.
   * @param direction The direction of the ray.
   * @param limit The maximum units along the ray.
   */
  public Ray2(Point2 origin, Vector2 direction, double limit) {
    this.origin = origin;
    this.direction = direction;
    this.limit = limit;
  }

  /**
   * Creates a <code>Ray2</code> with no limit.
   * @param origin The origin of the ray.
   * @param direction The direction of the ray.
   */
  public Ray2(Point2 origin, Vector2 direction) {
    this(origin, direction, Double.POSITIVE_INFINITY);
  }

  /**
   * Creates a <code>Ray2</code> spanning from one point to another.
   * @param p The <code>Point2</code> at the origin of the ray.
   * @param q The <code>Point2</code> at the end of the ray.
   */
  public Ray2(Point2 p, Point2 q) {
    this.limit = p.distanceTo(q);
    this.origin = p;
    this.direction = p.vectorTo(q).divide(limit);
  }

  /**
   * Creates a <code>Ray2</code> from one point along a direction or to
   * another point.
   * @param p The <code>Point2</code> at the origin of the ray.
   * @param q The <code>HPoint2</code> indicating the direction and limit of
   *     the ray.  If <code>q</code> is a <code>Point2</code>, this creates
   *     a finite ray from <code>p</code> to <code>q</code>.  If
   *     <code>q</code> is a <code>Vector2</code>, this creates an infinite
   *     ray from <code>p</code> in the direction indicated by
   *     <code>q</code>.
   */
  public Ray2(Point2 p, HPoint2 q) {
    this.origin = p;
    if (q.isPoint()) {
      this.limit = p.distanceTo((Point2) q);
      this.direction = p.vectorTo((Point2) q).divide(limit);
    } else {
      this.direction = (Vector2) q;
      this.limit = Double.POSITIVE_INFINITY;
    }
  }

  /**
   * Gets the origin of this ray.
   * @return The origin of this ray.
   */
  public Point2 origin() {
    return origin;
  }

  /**
   * Gets the direction of this ray.
   * @return The direction of this ray.
   */
  public Vector2 direction() {
    return direction;
  }

  /**
   * Gets the maximum number of units along the ray.
   * @return The maximum number of units along this ray.
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
   * Gets a parallel <code>Ray2</code> with the origin <code>t</code> units
   * along this <code>Ray2</code>.
   * @param t The number of units along the ray to advance.
   * @return The new <code>Ray2</code>.
   */
  public Ray2 advance(double t) {
    return new Ray2(pointAt(t), direction);
  }

  /**
   * Gets the point that is t units along the ray.
   * @param t The number of units along the ray to find the point of.
   * @return The point that is t units along the ray.
   */
  public Point2 pointAt(double t) {
    return origin.plus(direction.times(t));
  }

  /**
   * Gets the end point of this ray.
   * Equivalent to <code>this.pointAt(this.limit())</code>.
   * @return The end point of this ray.
   * @see #pointAt(double)
   * @see #limit()
   */
  public Point2 pointAtLimit() {
    return pointAt(limit);
  }

  /**
   * Transforms this <code>Ray2</code> according to the specified
   * transformation matrix.
   * @param T The <code>AffineMatrix2</code> representing the transformation
   *     to apply.
   * @return The transformed <code>Ray2</code>.
   */
  public Ray2 transform(AffineMatrix2 T) {
    return new Ray2(
        T.times(origin),
        T.times(direction),
        limit
    );
  }

  /** The origin of the ray. */
  private final Point2 origin;

  /** The direction of the ray. */
  private final Vector2 direction;

  /** The maximum units along the ray. */
  private final double limit;

}
