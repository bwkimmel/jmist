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
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A circle in two dimensional space (the set of points at most a
 * constant distance from a fixed point).
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Circle implements Serializable {

  /**
   * Initializes the center and radius of the circle.
   * @param center The center of the circle.
   * @param radius The radius of the circle (must be non-negative).
   */
  public Circle(Point2 center, double radius) {
    assert(radius >= 0.0);
    this.center = center;
    this.radius = radius;
  }

  /**
   * Gets the center of the circle.
   * @return The center of the circle.
   */
  public Point2 center() {
    return center;
  }

  /**
   * Gets the radius of the circle.
   * @return The radius of the circle.
   */
  public double radius() {
    return radius;
  }

  /**
   * Determines if this circle is empty.
   * @return A value indicating if this circle is empty.
   */
  public boolean isEmpty() {
    return Double.isNaN(radius);
  }

  /**
   * Determines if this circle contains the specified point.
   * @param p The point to check for containment of.
   * @return A value indicating if p is within this circle.
   */
  public boolean contains(Point2 p) {
    return center.distanceTo(p) <= radius;
  }

  /**
   * Computes the area of the circle.
   * @return The area of the circle.
   */
  public double area() {
    return Math.PI * radius * radius;
  }

  /**
   * Computes the diameter of the circle.
   * @return The diameter of the circle.
   */
  public double diameter() {
    return 2.0 * radius;
  }

  /**
   * Computes the circumference of the circle.
   * @return The circumference of the circle.
   */
  public double circumference() {
    return 2.0 * Math.PI * radius;
  }

  /**
   * Expands this circle outwards by the specified amount.
   * @param amount The amount to expand the circle by.
   * @return The expanded circle.
   */
  public Circle expand(double amount) {
    return getInstance(center, radius + amount);
  }

  /**
   * Expands this circle outwards to encompass the specified point.
   * Guarantees that {@code this.contains(p)} after this method is called.
   * @param p The point to include in this circle.
   * @return The expanded circle.
   */
  public Circle expandTo(Point2 p) {
    if (isEmpty()) {
      return new Circle(p, 0.0);
    } else {
      double newRadius = center.distanceTo(p);

      if (newRadius < radius) {
        return this;
      } else {
        return new Circle(center, newRadius);
      }
    }
  }

  /**
   * Determines whether the specified ray intersects with
   * this circle.  Equivalent to {@code !this.intersect(ray).isEmpty()}.
   * @param ray The ray to test for an intersection with.
   * @return A value indicating whether the ray intersects
   *     this circle.
   * @see #intersect(Ray2)
   * @see Interval#isEmpty()
   */
  public boolean intersects(Ray2 ray) {
    //
    // Algorithm from:
    //
    // A.S. Glassner, Ed.,
    // "An Introduction to Ray Tracing",
    // Morgan Kaufmann Publishers, Inc., San Francisco, CA, 2002
    // Section 2.2
    //

    if (isEmpty()) {
      return false;
    }

    double r2 = radius * radius;
    Vector2 oc = ray.origin().vectorTo(center);
    double L2oc = oc.dot(oc);

    if (L2oc < r2) return true;

    double tca = oc.dot(ray.direction());

    if (tca < 0.0) return false;

    return ray.pointAt(tca).squaredDistanceTo(center) < r2;
  }

  /**
   * Computes the intersection of this circle with the given
   * ray.
   * @param ray The ray to compute the intersection of this
   *     circle with.
   * @return The interval in which the ray passes through
   *     the circle (i.e., this.contains(ray.pointAt(x)) if and
   *     only if this.intersect(ray).contains(x)).
   * @see #contains(Point2)
   * @see Ray2#pointAt(double)
   * @see Interval#contains(double)
   */
  public Interval intersect(Ray2 ray) {
    //
    // Algorithm from:
    //
    // A.S. Glassner, Ed.,
    // "An Introduction to Ray Tracing",
    // Morgan Kaufmann Publishers, Inc., San Francisco, CA, 2002
    // Section 2.2
    //

    // Check for an empty box.
    if (isEmpty()) {
      return Interval.EMPTY;
    }

    // Check if the ray starts from within the box.
    double r2 = radius * radius;
    Vector2 oc = ray.origin().vectorTo(center);
    double L2oc = oc.dot(oc);
    boolean startInside = (L2oc < r2);

    // distance along ray to point on ray closest to center of sphere (equation (A10)).
    double tca = oc.dot(ray.direction());

    // if the ray starts outside the sphere and points away from the center of the
    // sphwere, then the ray does not hit the sphere.
    if (!startInside && tca < 0.0) {
      return Interval.EMPTY;
    }

    // compute half chord distance squared (equation (A13)).
    double t2hc = r2 - L2oc + (tca * tca);

    if (t2hc < 0.0) {
      return Interval.EMPTY;
    }

    double thc = Math.sqrt(t2hc);

    // compute interval (equation (A14)).
    return new Interval(tca - thc, tca + thc);
  }

  /**
   * Determines whether the specified point is near the
   * boundary of the circle.
   * @param p The point to consider.
   * @return A value indicating whether the specified point
   *     is near the boundary of the circle.
   */
  public boolean nearBoundary(Point2 p) {
    return MathUtil.equal(center.squaredDistanceTo(p) / radius, radius);
  }

  /**
   * Determines whether the specified point is near the
   * boundary of the circle, within a specified tolerance.
   * @param p The point to consider.
   * @param epsilon The tolerance.
   * @return A value indicating whether the specified point
   *     is near the boundary of the circle.
   */
  public boolean nearBoundary(Point2 p, double epsilon) {
    return MathUtil.equal(center.squaredDistanceTo(p) / radius, radius, epsilon);
  }

  /**
   * Computes the normal at the specified point, assuming p
   * is on the boundary of the circle.  This method is guaranteed
   * to return a unit vector.
   * @param p The point at which to compute the normal.
   * @return The normal at the specified point.
   */
  public Vector2 normalAt(Point2 p) {
    return center.vectorTo(p).unit();
  }

  /**
   * Computes the smallest <code>Circle</code> containing each of the given
   * points.
   * @param points The collection of points for which to compute the smallest
   *     <code>Circle</code> containing those points.
   * @return The smallest <code>Circle</code> containing the given points.
   */
  public static Circle smallestContaining(Iterable<Point2> points) {
    Point2 center = Point2.centroid(points);
    double radius = 0.0;

    for (Point2 p : points) {
      double distance = center.distanceTo(p);
      if (distance > radius) {
        radius = distance;
      }
    }

    return new Circle(center, radius);
  }

  /**
   * Gets the smallest <code>Box2</code> containing this
   * <code>Circle</code>.
   * @return The smallest <code>Box2</code> containing this
   * <code>Circle</code>.
   */
  public Box2 boundingBox() {
    return new Box2(
        center.x() - radius,
        center.y() - radius,
        center.x() + radius,
        center.y() + radius
    );
  }

  /**
   * Default constructor.
   */
  private Circle() {
    center = Point2.ORIGIN;
    radius = Double.NaN;
  }

  /**
   * Gets an instance of a circle.
   * @param center The center of the circle.
   * @param radius The radius of the circle.
   * @return A new circle if 0 <= radius < infinity,
   *         Circle.UNIVERSE if radius == infinity,
   *         Circle.EMPTY if radius < 0.
   */
  private static final Circle getInstance(Point2 center, double radius) {
    if (radius < 0.0) {
      return Circle.EMPTY;
    } else if (Double.isInfinite(radius)) {
      return Circle.UNIVERSE;
    } else {
      return new Circle(center, radius);
    }
  }

  /** A circle containing all points. */
  public static final Circle UNIVERSE = new Circle(Point2.ORIGIN, Double.POSITIVE_INFINITY);

  /**
   * The unit circle (the circle centered at the origin with a radius of 1.0).
   */
  public static final Circle UNIT = new Circle(Point2.ORIGIN, 1.0);

  /** An empty circle. */
  public static final Circle EMPTY = new Circle();

  /** The center of the circle. */
  private final Point2 center;

  /** The radius of the circle. */
  private final double radius;

  /** Serialization version ID. */
  private static final long serialVersionUID = -2497313773493949063L;

}
