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
 * The difference between two points in two dimensional space.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Vector2 extends HPoint2 {

  /**
   * Initializes the components for the vector.
   * @param x The length of the vector along the x axis.
   * @param y The length of the vector along the y axis.
   */
  public Vector2(double x, double y) {
    super(x, y);
  }

  @Override
  public double w() {
    return 0.0;
  }

  /**
   * Computes the magnitude of the vector.
   * @return The magnitude of the vector.
   */
  public double length() {
    return Math.hypot(x, y);
  }

  /**
   * Computes the square of the magnitude of this vector.
   * @return The square of the magnitude of this vector.
   */
  public double squaredLength() {
    return dot(this);
  }

  /**
   * Returns the opposite of this vector.
   * @return The opposite of this vector.
   */
  public Vector2 opposite() {
    return new Vector2(-x, -y);
  }

  /**
   * Computes the sum of two vectors.
   * @param v The vector to add to this vector.
   * @return The sum of this vector and v.
   */
  public Vector2 plus(Vector2 v) {
    return new Vector2(x + v.x, y + v.y);
  }

  /**
   * Computes the difference between two vectors.
   * @param v The vector to subtract from this vector.
   * @return The difference between this vector and v.
   */
  public Vector2 minus(Vector2 v) {
    return new Vector2(x - v.x, y - v.y);
  }

  /**
   * Computes this vector scaled by a constant factor.
   * @param c The factor to scale this vector by.
   * @return This vector scaled by c.
   */
  public Vector2 times(double c) {
    return new Vector2(c * x, c * y);
  }

  /**
   * Computes this vector scaled by the reciprocal of
   * a constant factor.
   * Equivalent to {@code this.times(1.0 / c).}
   * @param c The factor to divide this vector by.
   * @return The vector scaled by 1.0 / c.
   * @see #times(double)
   */
  public Vector2 divide(double c) {
    return new Vector2(x / c, y / c);
  }

  /**
   * Computes the dot product between two vectors.
   * @param v The vector to compute the dot product of with this vector.
   * @return The dot product of this vector and v.
   */
  public double dot(Vector2 v) {
    return (x * v.x) + (y * v.y);
  }

  /**
   * Computes the unit vector in the same direction as this vector.
   * @return The unit vector in the same direction as this vector.
   */
  public Vector2 unit() {
    return this.times(1.0 / length());
  }

  /**
   * Returns a new unit vector in the same direction as
   * the vector with the specified components.
   * Equivalent to {@code new Vector2(x, y).unit()}.
   * @param x The magnitude of the vector along the x-axis.
   * @param y The magnitude of the vector along the y-axis.
   * @return A unit vector in the same direction as the
   *     vector with the indicated components.
   * @see #unit()
   */
  public static Vector2 unit(double x, double y) {
    double r = Math.sqrt(x * x + y * y);
    return new Vector2(x / r, y / r);
  }

  /**
   * Returns a <code>Vector2</code> perpendicular to this one.
   * @return A <code>Vector2</code> perpendicular to this one.
   */
  public Vector2 perp() {
    return new Vector2(-y, x);
  }

  /**
   * The zero vector (represents the vector between two identical points).
   */
  public static final Vector2 ZERO = new Vector2(0.0, 0.0);

  /**
   * The unit vector along the x-axis.
   */
  public static final Vector2 I = new Vector2(1.0, 0.0);

  /**
   * The unit vector along the y-axis.
   */
  public static final Vector2 J = new Vector2(0.0, 1.0);

  /**
   * The unit vector along the negative x-axis.
   */
  public static final Vector2 NEGATIVE_I = new Vector2(-1.0, 0.0);

  /**
   * The unit vector along the negative y-axis.
   */
  public static final Vector2 NEGATIVE_J = new Vector2(0.0, -1.0);

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -5602669939886701571L;

}
