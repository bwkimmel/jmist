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
 * An axis-aligned two dimensional box.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Box2 implements Serializable {

  /**
   * Initializes the extents of the box along the x and y axes.
   * @param spanX The extent of the box along the x-axis.
   * @param spanY The extent of the box along the y-axis.
   */
  public Box2(Interval spanX, Interval spanY) {
    if (spanX.isEmpty() || spanY.isEmpty()) {
      minimumX = minimumY = maximumX = maximumY = Double.NaN;
    } else {
      minimumX = spanX.minimum();
      maximumX = spanX.maximum();
      minimumY = spanY.minimum();
      maximumY = spanY.maximum();
    }
  }

  /**
   * Initializes the box between two points.
   * @param p One corner of the box.
   * @param q The corner of the box opposite p.
   */
  public Box2(Point2 p, Point2 q) {
    minimumX = Math.min(p.x(), q.x());
    maximumX = Math.max(p.x(), q.x());
    minimumY = Math.min(p.y(), q.y());
    maximumY = Math.max(p.y(), q.y());
  }

  /**
   * Initializes the box from its extents.
   * @param minimumX The lower bound of the box along the x-axis.
   * @param minimumY The lower bound of the box along the y-axis.
   * @param maximumX The upper bound of the box along the x-axis.
   * @param maximumY The upper bound of the box along the y-axis.
   */
  public Box2(double minimumX, double minimumY, double maximumX, double maximumY) {
    assert(minimumX <= maximumX && minimumY <= maximumY);

    this.minimumX = minimumX;
    this.minimumY = minimumY;
    this.maximumX = maximumX;
    this.maximumY = maximumY;
  }

  /**
   * Gets the lower bound of this box along the x-axis.
   * @return The lower bound of this box along the x-axis.
   */
  public double minimumX() {
    return minimumX;
  }

  /**
   * Gets the lower bound of this box along the y-axis.
   * @return The lower bound of this box along the y-axis.
   */
  public double minimumY() {
    return minimumY;
  }

  /**
   * Gets the lower bound along the specified axis.
   * @param axis The axis along which to get the lower bound (0 - x-axis, or
   *     1 - y-axis).
   * @return The lower bound along the specified axis.
   * @throws IllegalArgumentException if <code>axis</code> is negative or
   *     greater than 1.
   */
  public double minimum(int axis) {
    switch (axis) {
    case 0:
      return minimumX;
    case 1:
      return minimumY;
    default:
      throw new IllegalArgumentException();
    }
  }

  /**
   * Gets the upper bound of this box along the x-axis.
   * @return The upper bound of this box along the x-axis.
   */
  public double maximumX() {
    return maximumX;
  }

  /**
   * Gets the upper bound of this box along the y-axis.
   * @return The upper bound of this box along the y-axis.
   */
  public double maximumY() {
    return maximumY;
  }

  /**
   * Gets the upper bound along the specified axis.
   * @param axis The axis along which to get the upper bound (0 - x-axis, or
   *     1 - y-axis).
   * @return The upper bound along the specified axis.
   * @throws IllegalArgumentException if <code>axis</code> is negative or
   *     greater than 1.
   */
  public double maximum(int axis) {
    switch (axis) {
    case 0:
      return maximumX;
    case 1:
      return maximumY;
    default:
      throw new IllegalArgumentException();
    }
  }

  /**
   * Gets the extent of this box along the x-axis.
   * @return An interval representing the extent of this box along the x-axis.
   */
  public Interval spanX() {
    return isEmpty() ? Interval.EMPTY : new Interval(minimumX, maximumX);
  }

  /**
   * Gets the extent of this box along the y-axis.
   * @return An interval representing the extent of this box along the y-axis.
   */
  public Interval spanY() {
    return isEmpty() ? Interval.EMPTY : new Interval(minimumY, maximumY);
  }

  /**
   * Gets the length of the box along the x-axis.
   * @return The length of the box along the x-axis.
   */
  public double lengthX() {
    return isEmpty() ? Double.NaN : maximumX - minimumX;
  }

  /**
   * Gets the length of the box along the y-axis.
   * @return The length of the box along the y-axis.
   */
  public double lengthY() {
    return isEmpty() ? Double.NaN : maximumY - minimumY;
  }

  /**
   * Determines if this box is empty.
   * @return A value indicating wither this box is empty.
   */
  public boolean isEmpty() {
    return Double.isNaN(minimumX);
  }
  
  /** 
   * Determines if this box is infinite.
   * @return A value indicating if this interval is infinite.
   */
  public boolean isInfinite() {
    return !isEmpty()
        && (Double.isInfinite(minimumX) || Double.isInfinite(maximumX)
            || Double.isInfinite(minimumY) || Double
            .isInfinite(maximumY));
  }

  /**
   * Gets the point at the center of this box.
   * @return The point at the center of this box.
   */
  public Point2 center() {
    return new Point2((minimumX + maximumX) / 2.0, (minimumY + maximumY) / 2.0);
  }

  /**
   * Computes the length of the diagonal of this box.
   * @return The length of the diagonal of this box.
   */
  public double diagonal() {
    return Math.sqrt(
        (maximumX - minimumX) * (maximumX - minimumX) +
        (maximumY - minimumY) * (maximumY - minimumY)
        );
  }

  /**
   * Determines if this box contains the specified point.
   * @param p The point to check for containment of.
   * @return A value indicating whether p is inside this box.
   */
  public boolean contains(Point2 p) {
    return (minimumX <= p.x() && p.x() <= maximumX) && (minimumY <= p.y() && p.y() <= maximumY);
  }

  /**
   * Determines if this box contains another box.
   * @param other The <code>Box2</code> to check for containment of.
   * @return A value indicating if this box contains <code>other</code>.
   */
  public boolean contains(Box2 other) {
    return (minimumX <= other.minimumX && other.maximumX <= maximumX)
        && (minimumY <= other.minimumY && other.maximumY <= maximumY);
  }

  /**
   * Gets the area of the box.
   * @return The area of the box.
   */
  public double area() {
    return (maximumX - minimumX) * (maximumY - minimumY);
  }

  /**
   * Gets the length of the perimeter of the box.
   * @return The perimeter of the box.
   */
  public double perimeter() {
    return 2.0 * ((maximumX - minimumX) + (maximumY - minimumY));
  }

  /**
   * Computes the intersection of this box with another.
   * @param other The box to intersect with this box.
   * @return The intersection of this box with the other box.
   */
  public Box2 intersect(Box2 other) {
    return getInstance(
        Math.max(minimumX, other.minimumX),
        Math.max(minimumY, other.minimumY),
        Math.min(maximumX, other.maximumX),
        Math.min(maximumY, other.maximumY)
    );
  }

  /**
   * Extends the box to contain the specified point.
   * Guarantees that {@code this.contains(p)} after this method is called.
   * @param p The point to extend the box to.
   * @return The extended box.
   * @see #contains(Point2)
   */
  public Box2 extendTo(Point2 p) {
    if (isEmpty()) {
      return new Box2(p.x(), p.y(), p.x(), p.y());
    } else {
      return new Box2(
          Math.min(minimumX, p.x()),
          Math.min(minimumY, p.y()),
          Math.max(maximumX, p.x()),
          Math.max(maximumY, p.y())
      );
    }
  }

  /**
   * Expands the box by the specified amount in all directions.
   * @param amount The amount to expand the box by.
   * @return The expanded box.
   */
  public Box2 expand(double amount) {
    return getInstance(minimumX - amount, minimumY - amount, maximumX + amount, maximumY + amount);
  }

  /**
   * Determines if this box intersects with another.
   * @param other The <code>Box2</code> to check for an intersection with.
   * @return A value indicating if this box intersects <code>other</code>.
   */
  public boolean intersects(Box2 other) {
    return Math.max(minimumX, other.minimumX) <= Math.min(maximumX, other.maximumX)
        && Math.max(minimumY, other.minimumY) <= Math.min(maximumY, other.maximumY);
  }

  /**
   * Determines if the specified ray intersects with this box.
   * Equivalent to {@code !this.intersect(ray).isEmpty()}.
   * @param ray The ray to test for an intersection with this
   *     box.
   * @return A value indicating if the specified ray intersects
   *     with this box.
   * @see #intersect(Ray2)
   * @see Interval#isEmpty()
   */
  public boolean intersects(Ray2 ray) {
    return this.intersects(ray, Interval.POSITIVE);
  }

  /**
   * Determines if the specified ray intersects with this box.
   * Equivalent to {@code !this.intersect(ray).intersects(I)}.
   * @param ray The ray to test for an intersection with this
   *     box.
   * @param I The <code>Interval</code> along the ray to check for an
   *     intersection.
   * @return A value indicating if the specified ray intersects
   *     with this box.
   * @see #intersect(Ray2)
   * @see Interval#intersects(Interval)
   */
  public boolean intersects(Ray2 ray, Interval I) {

    // Check for an empty box.
    if (isEmpty()) {
      return false;
    }

    // Check if the ray starts from within the box.
    if (this.contains(ray.pointAt(I.minimum()))) {
      return true;
    }

    assert(ray.direction().x() != 0.0 || ray.direction().y() != 0.0);

    double  t;
    Point2  p;

    // Check for intersection with each of the six sides of the box.
    if (ray.direction().x() != 0.0) {
      t = (minimumX - ray.origin().x()) / ray.direction().x();
      if (I.contains(t)) {
        p = ray.pointAt(t);
        if (minimumY < p.y() && p.y() < maximumY) {
          return true;
        }
      }

      t = (maximumX - ray.origin().x()) / ray.direction().x();
      if (I.contains(t)) {
        p = ray.pointAt(t);
        if (minimumY < p.y() && p.y() < maximumY) {
          return true;
        }
      }
    }

    if (ray.direction().y() != 0.0) {
      t = (minimumY - ray.origin().y()) / ray.direction().y();
      if (I.contains(t)) {
        p = ray.pointAt(t);
        if (minimumX < p.x() && p.x() < maximumX) {
          return true;
        }
      }

      t = (maximumY - ray.origin().y()) / ray.direction().y();
      if (I.contains(t)) {
        p = ray.pointAt(t);
        if (minimumX < p.x() && p.x() < maximumX) {
          return true;
        }
      }
    }

    // If we didn't find any intersection points, then the
    // ray does not intersect the box.
    return false;

  }

  /**
   * Computes the intersection of this box with the given
   * ray.
   * @param ray The ray to compute the intersection of this
   *     box with.
   * @return The interval in which the ray passes through
   *     the box (i.e., this.contains(ray.pointAt(x)) if and
   *     only if this.intersect(ray).contains(x)).
   * @see #contains(Point2)
   * @see Ray2#pointAt(double)
   * @see Interval#contains(double)
   */
  public Interval intersect(Ray2 ray) {

    // Check for an empty box.
    if (isEmpty()) {
      return Interval.EMPTY;
    }

    assert(ray.direction().x() != 0.0 || ray.direction().y() != 0.0);

    double[]  t = new double[2];
    int      n = 0;
    Point2    p;

    // Check for intersection with each of the six sides of the box.
    if (ray.direction().x() != 0.0) {
      t[n] = (minimumX - ray.origin().x()) / ray.direction().x();
      if (t[n] > 0.0) {
        p = ray.pointAt(t[n]);
        if (minimumY < p.y() && p.y() < maximumY) {
          if (++n == 2) return Interval.between(t[0], t[1]);
        }
      }

      t[n] = (maximumX - ray.origin().x()) / ray.direction().x();
      if (t[n] > 0.0) {
        p = ray.pointAt(t[n]);
        if (minimumY < p.y() && p.y() < maximumY) {
          if (++n == 2) return Interval.between(t[0], t[1]);
        }
      }
    }

    if (ray.direction().y() != 0.0) {
      t[n] = (minimumY - ray.origin().y()) / ray.direction().y();
      if (t[n] > 0.0) {
        p = ray.pointAt(t[n]);
        if (minimumX < p.x() && p.x() < maximumX) {
          if (++n == 2) return Interval.between(t[0], t[1]);
        }
      }

      t[n] = (maximumY - ray.origin().y()) / ray.direction().y();
      if (t[n] > 0.0) {
        p = ray.pointAt(t[n]);
        if (minimumX < p.x() && p.x() < maximumX) {
          if (++n == 2) return Interval.between(t[0], t[1]);
        }
      }
    }

    // If we didn't find two intersection points, then the
    return Interval.EMPTY;

  }

  /**
   * Determines whether the specified point is near the
   * boundary of the box.
   * @param p The point to consider.
   * @return A value indicating whether the specified point
   *     is near the boundary of the box.
   */
  public boolean nearBoundary(Point2 p) {
    return ((MathUtil.equal(p.x(), minimumX) || MathUtil.equal(p.x(), maximumX)) && (minimumY <= p.y() && p.y() <= maximumY)) ||
      ((MathUtil.equal(p.y(), minimumY) || MathUtil.equal(p.y(), maximumY)) && (minimumX <= p.x() && p.x() <= maximumX));
  }

  /**
   * Determines whether the specified point is near the
   * boundary of the box, within a specified tolerance.
   * @param p The point to consider.
   * @param epsilon The tolerance.
   * @return A value indicating whether the specified point
   *     is near the boundary of the box.
   */
  public boolean nearBoundary(Point2 p, double epsilon) {
    return ((MathUtil.equal(p.x(), minimumX, epsilon) || MathUtil.equal(p.x(), maximumX, epsilon)) && (minimumY <= p.y() && p.y() <= maximumY)) ||
      ((MathUtil.equal(p.y(), minimumY, epsilon) || MathUtil.equal(p.y(), maximumY, epsilon)) && (minimumX <= p.x() && p.x() <= maximumX));
  }

  /**
   * Computes the normal at the specified point, assuming p
   * is on the boundary of the box.  This method is guaranteed
   * to return a unit vector.
   * @param p The point at which to compute the normal.
   * @return The normal at the specified point.
   */
  public Vector2 normalAt(Point2 p) {
    double  cx = (minimumX + maximumX) / 2.0;
    double  cy = (minimumY + maximumY) / 2.0;

    double  rx = this.lengthX() / 2.0;
    double  ry = this.lengthY() / 2.0;

    double  dx = (p.x() - cx) / rx;
    double  dy = (p.y() - cy) / ry;

    if (Math.abs(dx) > Math.abs(dy)) {
      return new Vector2(Math.signum(dx), 0.0);
    } else { // Math.abs(dy) > Math.abs(dx)
      return new Vector2(0.0, Math.signum(dy));
    }
  }

  /**
   * Interpolates between the bounds of this box along the x-axis.
   * Equivalent to {@code this.getSpanX().interpolate(t)}.
   * @param t The point at which to interpolate.
   * @return The interpolated value.
   * @see #spanX()
   * @see Interval#interpolate(double)
   */
  public double interpolateX(double t) {
    return this.minimumX + t * (this.maximumX - this.minimumX);
  }

  /**
   * Interpolates between the bounds of this box along the y-axis.
   * Equivalent to {@code this.getSpanY().interpolate(t)}.
   * @param t The point at which to interpolate.
   * @return The interpolated value.
   * @see #spanY()
   * @see Interval#interpolate(double)
   */
  public double interpolateY(double t) {
    return this.minimumY + t * (this.maximumY - this.minimumY);
  }

  /**
   * Interpolates within the bounds of this box.  If (tx, ty) is
   * in [0, 1]^2, then the interpolated point will fall inside the
   * box, otherwise the interpolated point will fall outside the
   * box.  Equivalent to {@code this.interpolate(new Point2(tx, ty))}.
   * @param tx The point at which to interpolate along the x-axis.
   * @param ty The point at which to interpolate along the y-axis.
   * @return The interpolated point.
   * @see #interpolate(Point2)
   */
  public Point2 interpolate(double tx, double ty) {
    return new Point2(this.interpolateX(tx), this.interpolateY(ty));
  }

  /**
   * Interpolates within the bounds of this box.  If {@code p} is
   * in [0, 1]^2, then the interpolated point will fall inside the
   * box, otherwise the interpolated point will fall outside the
   * box.  Equivalent to {@code this.interpolate(p.x(), p.y())}.
   * @param p The point at which to interpolate.
   * @return The interpolated point.
   * @see #interpolate(double, double)
   */
  public Point2 interpolate(Point2 p) {
    return this.interpolate(p.x(), p.y());
  }

  /**
   * The <code>Point2</code> at one of the four corners of this
   * <code>Box2</code>.
   * @param index The index of the corner to return (must be non-negative and
   *     less than four).
   * @return The <code>Point2</code> at one of the four corners of this
   *     <code>Box2</code>.
   */
  public Point2 corner(int index) {
    assert(0 <= index && index < 4);
    return new Point2(
        (index & 0x1) == 0 ? this.minimumX : this.maximumX,
        (index & 0x2) == 0 ? this.minimumY : this.maximumY
    );
  }
  
  /**
   * Computes the closest point within the box to a given point.
   * @param p The <code>Point2</code> to project.
   * @return The closest point to <code>p</code> within this box.
   */
  public final Point2 closestPointTo(Point2 p) {
    return new Point2(
        MathUtil.clamp(p.x(), minimumX, maximumX),
        MathUtil.clamp(p.y(), minimumY, maximumY));
  }
  
  /**
   * Returns the affine transformation that maps the unit box to this one.
   * @return The <code>AffineMatrix2</code> representing the transformation
   *     that maps points in {@link #UNIT} to this box.
   * @see #UNIT
   */
  public final AffineMatrix2 toMatrix() {
    return new AffineMatrix2(
        lengthX(), 0.0      , minimumX,
        0.0      , lengthY(), minimumY);
  }

  /**
   * Computes the smallest <code>Box2</code> that contains all of the given
   * points.
   * @param points The collection of <code>Point2</code>s for which to find the
   *     smallest containing <code>Box2</code>.
   * @return The smallest <code>Box2</code> that contains all of the given
   *     points.
   */
  public static Box2 smallestContainingPoints(Iterable<Point2> points) {

    double minimumX = Double.POSITIVE_INFINITY;
    double minimumY = Double.POSITIVE_INFINITY;
    double maximumX = Double.NEGATIVE_INFINITY;
    double maximumY = Double.NEGATIVE_INFINITY;

    for (Point2 p : points) {
      if (p.x() < minimumX) {
        minimumX = p.x();
      }
      if (p.y() < minimumY) {
        minimumY = p.y();
      }
      if (p.x() > maximumX) {
        maximumX = p.x();
      }
      if (p.y() > maximumY) {
        maximumY = p.y();
      }
    }

    return Box2.getInstance(minimumX, minimumY, maximumX, maximumY);

  }

  /**
   * Computes the smallest <code>Box2</code> containing the specified box.
   * @param a The first <code>Box2</code>.
   * @param b The second <code>Box2</code>.
   * @return The smallest <code>Box2</code> containing <code>a</code> and
   *     <code>b</code>.
   */
  public static Box2 smallestContaining(Box2 a, Box2 b) {

    if (a.isEmpty()) {
      return b;
    }
    if (b.isEmpty()) {
      return a;
    }

    return Box2.getInstance(
        Math.min(a.minimumX, b.minimumX),
        Math.min(a.minimumY, b.minimumY),
        Math.max(a.maximumX, b.maximumX),
        Math.max(a.maximumY, b.maximumY)
    );

  }

  /**
   * Computes the smallest <code>Box2</code> that contains all of the given
   * boxes.
   * @param boxes The collection of <code>Box2</code>s for which to find the
   *     smallest containing <code>Box2</code>.
   * @return The smallest <code>Box2</code> that contains all of the given
   *     boxes.
   */
  public static Box2 smallestContaining(Iterable<Box2> boxes) {

    double minimumX = Double.POSITIVE_INFINITY;
    double minimumY = Double.POSITIVE_INFINITY;
    double maximumX = Double.NEGATIVE_INFINITY;
    double maximumY = Double.NEGATIVE_INFINITY;

    for (Box2 box : boxes) {
      if (!box.isEmpty()) {
        if (box.minimumX < minimumX) {
          minimumX = box.minimumX;
        }
        if (box.minimumY < minimumY) {
          minimumY = box.minimumY;
        }
        if (box.maximumX > maximumX) {
          maximumX = box.maximumX;
        }
        if (box.maximumY > maximumY) {
          maximumY = box.maximumY;
        }
      }
    }

    return Box2.getInstance(minimumX, minimumY, maximumX, maximumY);

  }

  /**
   * Computes the intersection of several boxes.
   * @param boxes The collection of boxes to compute the intersection of.
   * @return The intersection of the given boxes.
   */
  public static Box2 intersection(Iterable<Box2> boxes) {

    double minimumX = Double.NEGATIVE_INFINITY;
    double minimumY = Double.NEGATIVE_INFINITY;
    double maximumX = Double.POSITIVE_INFINITY;
    double maximumY = Double.POSITIVE_INFINITY;

    for (Box2 box : boxes) {
      if (!box.isEmpty()) {
        if (box.minimumX > minimumX) {
          minimumX = box.minimumX;
        }
        if (box.minimumY > minimumY) {
          minimumY = box.minimumY;
        }
        if (box.maximumX < maximumX) {
          maximumX = box.maximumX;
        }
        if (box.maximumY < maximumY) {
          maximumY = box.maximumY;
        }
      }
    }

    return Box2.getInstance(minimumX, minimumY, maximumX, maximumY);

  }

  /**
   * Default constructor.
   */
  private Box2() {
    minimumX = maximumX = minimumY = maximumY = Double.NaN;
  }

  /**
   * Gets an instance of a two dimensional box.
   * @param minimumX The minimum extent of the box along the x-axis.
   * @param minimumY The minimum extent of the box along the y-axis.
   * @param maximumX The maximum extent of the box along the x-axis.
   * @param maximumY The maximum extent of the box along the y-axis.
   * @return A new box if minimumX <= maximumX and minimumY <= maximumY,
   *         Box2.EMPTY otherwise.
   */
  private static final Box2 getInstance(double minimumX, double minimumY, double maximumX, double maximumY) {
    if (minimumX > maximumY || minimumY > maximumY) {
      return Box2.EMPTY;
    } else {
      return new Box2(minimumX, minimumY, maximumX, maximumY);
    }
  }

  /**
   * The box containing all of two dimensional space.
   * {@code Box2.UNIVERSE.contains(p)} will be true for all p.
   */
  public static final Box2 UNIVERSE = new Box2(Interval.UNIVERSE, Interval.UNIVERSE);

  /**
   * The unit box: [0, 1]^2.
   */
  public static final Box2 UNIT = new Box2(0.0, 0.0, 1.0, 1.0);

  /**
   * The empty box.
   * {@code Box2.EMPTY.contains(p)} will be false for all p.
   */
  public static final Box2 EMPTY = new Box2();

  /** The lower bound along the x-axis. */
  private double minimumX;

  /** The upper bound along the x-axis. */
  private double maximumX;

  /** The lower bound along the y-axis. */
  private double minimumY;

  /** The upper bound along the y-axis. */
  private double maximumY;

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 2385108773960788026L;

}
