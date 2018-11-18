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

/**
 * A location in three dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Point3 extends HPoint3 {

  /**
   * Initializes the components for the point.
   * @param x The distance from the origin along the x axis.
   * @param y The distance from the origin along the y axis.
   * @param z The distance from the origin along the z axis.
   */
  public Point3(double x, double y, double z) {
    super(x, y, z);
  }

  public double w() {
    return 1.0;
  }

  /**
   * Computes the square of the distance from this point to the
   * specified point.
   * @param p The point to compute the square of the distance to.
   * @return The square of the distance between this point and
   * the specified point.
   */
  public double squaredDistanceTo(Point3 p) {
    return ((x - p.x) * (x - p.x)) + ((y - p.y) * (y - p.y)) + ((z - p.z) * (z - p.z));
  }

  /**
   * Computes the square of the distance between this point and the origin.
   * @return The square of the distance between this point and the origin.
   */
  public double squaredDistanceToOrigin() {
    return x * x + y * y + z * z;
  }

  /**
   * Computes the distance between this point and the specified point.
   * @param p The point to compute the distance to.
   * @return The distance between this point and p.
   */
  public double distanceTo(Point3 p) {
    return Math.sqrt(squaredDistanceTo(p));
  }

  /**
   * Computes the distance between this point and the origin.
   * @return The distance between this point and the origin.
   */
  public double distanceToOrigin() {
    return Math.sqrt(squaredDistanceToOrigin());
  }

  /**
   * Computes the unit vector from this point to the specified point.
   * @param p The point at the end of the vector.
   * @return The unit vector from this point to p.
   */
  public Vector3 unitVectorTo(Point3 p) {
    double d = distanceTo(p);
    return new Vector3((p.x - x) / d, (p.y - y) / d, (p.z - z) / d);
  }

  /**
   * Computes the unit vector from this point to the origin.
   * @return The unit vector from this point to the origin.
   */
  public Vector3 unitVectorToOrigin() {
    double d = distanceToOrigin();
    return new Vector3(-x / d, -y / d, -z / d);
  }

  /**
   * Computes the vector from this point to the specified point.
   * @param p The point at the end of the vector.
   * @return The vector from this point to p.
   */
  public Vector3 vectorTo(Point3 p) {
    return new Vector3(p.x - x, p.y - y, p.z - z);
  }

  /**
   * Computes the vector to {@link #ORIGIN}.
   * @return The vector to {@link #ORIGIN} from this point.
   * @see #ORIGIN
   */
  public Vector3 vectorToOrigin() {
    return new Vector3(-x, -y, -z);
  }

  /**
   * Computes the unit vector from the specified point to this point.
   * @param p The point at the start of the vector.
   * @return The unit vector from p to this point.
   */
  public Vector3 unitVectorFrom(Point3 p) {
    double d = distanceTo(p);
    return new Vector3((x - p.x) / d, (y - p.y) / d, (z - p.z) / d);
  }

  /**
   * Computes the unit vector from the origin to this point.
   * @return The unit vector from the origin to this point.
   */
  public Vector3 unitVectorFromOrigin() {
    double d = distanceToOrigin();
    return new Vector3(x / d, y / d, z / d);
  }

  /**
   * Computes the vector from the specified point to this point.
   * @param p The point at the start of the vector.
   * @return The vector from p to this point.
   */
  public Vector3 vectorFrom(Point3 p) {
    return new Vector3(x - p.x, y - p.y, z - p.z);
  }

  /**
   * Computes the vector from {@link #ORIGIN}.
   * @return The vector from {@link #ORIGIN} to this point.
   * @see #ORIGIN
   */
  public Vector3 vectorFromOrigin() {
    return new Vector3(x, y, z);
  }

  /**
   * Returns this point translated according to the specified vector.
   * @param v The vector to translate this point by.
   * @return The value of this point translated by v.
   */
  public Point3 plus(Vector3 v) {
    return new Point3(x + v.x(), y + v.y(), z + v.z());
  }

  /**
   * Returns this point translated in the opposite direction of the
   * specified vector.
   * @param v The opposite of the vector to translate by.
   * @return The value of this point translated by -v.
   */
  public Point3 minus(Vector3 v) {
    return new Point3(x - v.x(), y - v.y(), z - v.z());
  }

  public Vector3 minus(Point3 p) {
    return new Vector3(x - p.x(), y - p.y(), z - p.z());
  }

  public HPoint3 minus(HPoint3 p) {
    return isPoint() ? minus(p.toPoint3()) : minus(p.toVector3());
  }

  /**
   * Interpolates between two <code>Point3</code>s.
   * @param p The <code>Point3</code> at <code>t = 0</code>.
   * @param q The <code>Point3</code> at <code>t = 1</code>.
   * @param t The value at which to interpolate.
   * @return The <code>Point3</code> that is the fraction <code>t</code> of
   *     the way from <code>p</code> to <code>q</code>:
   *     <code>p + t(q - p)</code>.
   */
  public static Point3 interpolate(Point3 p, Point3 q, double t) {
    return new Point3(
        MathUtil.interpolate(p.x, q.x, t),
        MathUtil.interpolate(p.y, q.y, t),
        MathUtil.interpolate(p.z, q.z, t)
    );
  }

  /**
   * Performs a bilinear interpolation between four <code>Point3</code>s.
   * @param _00 The <code>Point3</code> at <code>(t, u) = (0, 0)</code>.
   * @param _10 The <code>Point3</code> at <code>(t, u) = (1, 0)</code>.
   * @param _01 The <code>Point3</code> at <code>(t, u) = (0, 1)</code>.
   * @param _11 The <code>Point3</code> at <code>(t, u) = (1, 1)</code>.
   * @param t The first value at which to interpolate.
   * @param u The second value at which to interpolate.
   * @return The interpolated <code>Point3</code> at <code>(t, u)</code>.
   */
  public static Point3 bilinearInterpolate(Point3 _00, Point3 _10,
      Point3 _01, Point3 _11, double t, double u) {
    return new Point3(
        MathUtil.bilinearInterpolate(_00.x, _10.x, _01.x, _11.x, t, u),
        MathUtil.bilinearInterpolate(_00.y, _10.y, _01.y, _11.y, t, u),
        MathUtil.bilinearInterpolate(_00.z, _10.z, _01.z, _11.z, t, u)
    );
  }

  /**
   * Performs a trilinear interpolation between eight <code>Point3</code>s.
   * @param _000 The <code>Point3</code> at <code>(t, u, v) = (0, 0, 0)</code>.
   * @param _100 The <code>Point3</code> at <code>(t, u, v) = (1, 0, 0)</code>.
   * @param _010 The <code>Point3</code> at <code>(t, u, v) = (0, 1, 0)</code>.
   * @param _110 The <code>Point3</code> at <code>(t, u, v) = (1, 1, 0)</code>.
   * @param _001 The <code>Point3</code> at <code>(t, u, v) = (0, 0, 1)</code>.
   * @param _101 The <code>Point3</code> at <code>(t, u, v) = (1, 0, 1)</code>.
   * @param _011 The <code>Point3</code> at <code>(t, u, v) = (0, 1, 1)</code>.
   * @param _111 The <code>Point3</code> at <code>(t, u, v) = (1, 1, 1)</code>.
   * @param t The first value at which to interpolate.
   * @param u The second value at which to interpolate.
   * @param v The third value at which to interpolate.
   * @return The interpolated <code>Point3</code> at <code>(t, u, v)</code>.
   */
  public static Point3 trilinearInterpolate(Point3 _000, Point3 _100,
      Point3 _010, Point3 _110, Point3 _001, Point3 _101, Point3 _011,
      Point3 _111, double t, double u, double v) {
    return new Point3(
        MathUtil.trilinearInterpolate(_000.x, _100.x, _010.x, _110.x, _001.x, _101.x, _011.x, _111.x, t, u, v),
        MathUtil.trilinearInterpolate(_000.y, _100.y, _010.y, _110.y, _001.y, _101.y, _011.y, _111.y, t, u, v),
        MathUtil.trilinearInterpolate(_000.z, _100.z, _010.z, _110.z, _001.z, _101.z, _011.z, _111.z, t, u, v)
    );
  }

  /**
   * Finds the midpoint of the line segment joining two <code>Point3</code>s.
   * Equivalent to <code>Point3.interpolate(p, q, 0.5)</code>.
   * @param p The first <code>Point3</code>.
   * @param q The second <code>Point3</code>.
   * @return The <code>Point3</code> half-way between <code>p</code> and
   *     <code>q</code>.
   * @see #interpolate(Point3, Point3, double)
   */
  public static Point3 midpoint(Point3 p, Point3 q) {
    return Point3.interpolate(p, q, 0.5);
  }

  /**
   * Computes the centroid of a triangle defined by three <code>Point3</code>s.
   * @param a The <code>Point3</code> at the first corner of the triangle.
   * @param b The <code>Point3</code> at the second corner of the triangle.
   * @param c The <code>Point3</code> at the third corner of the triangle.
   * @return The <code>Point3</code> at the centroid of the triangle.
   */
  public static Point3 centroid(Point3 a, Point3 b, Point3 c) {
    return new Point3(
        (a.x + b.x + c.x) / 3.0,
        (a.y + b.y + c.y) / 3.0,
        (a.z + b.z + c.z) / 3.0
    );
  }

  /**
   * Computes the centroid of several points.
   * @param points The collection of <code>Point3</code>s for which to
   *     computes the centroid.
   * @return The centroid of the given collection of points.
   */
  public static Point3 centroid(Iterable<Point3> points) {
    double x = 0.0;
    double y = 0.0;
    double z = 0.0;
    int n = 0;

    for (Point3 p : points) {
      x += p.x;
      y += p.y;
      z += p.z;
      n++;
    }

    return new Point3(x / (double) n, y / (double) n, z / (double) n);
  }

  /** The origin of three dimensional space. */
  public static final Point3 ORIGIN = new Point3(0.0, 0.0, 0.0);

  /** Serialization version ID. */
  private static final long serialVersionUID = 15329079129798899L;

}
