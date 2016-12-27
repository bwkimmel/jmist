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

/**
 * A homogenized point in 3D space.  This will always be either a
 * <code>Point3</code> or a <code>Vector3</code>.
 * @author Brad Kimmel
 */
public abstract class HPoint3 extends Tuple3 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 5667239684184414779L;

  /**
   * Initializes the coordinates of this <code>HPoint3</code>.
   * NOTE: This constructor has package scope to prevent classes other than
   * <code>Point3</code> and <code>Vector3</code> from extending this class.
   * @param x The value of the x-coordinate.
   * @param y The value of the y-coordinate.
   * @param z The value of the z-coordinate.
   */
  /* package */ HPoint3(double x, double y, double z) {
    super(x, y, z);
  }

  /**
   * Creates a homogenized point.
   * @param x The value of the x-coordinate.
   * @param y The value of the y-coordinate.
   * @param z The value of the z-coordinate.
   * @param w The value of the w-coordinate (must be one or zero).
   * @return An <code>HPoint3</code> corresponding to the given coordinates.
   *     If <code>w == 0</code>, a <code>Vector3</code> is returned.  If
   *     <code>w == 1</code>, a <code>Point3</code> is returned.
   * @throws IllegalArgumentException if <code>w</code> is not within
   *     <code>MathUtil.EPSILON</code> of <code>0.0</code> or
   *     <code>1.0</code>.
   * @see MathUtil#EPSILON
   * @see Point3
   * @see Vector3
   */
  public static HPoint3 create(double x, double y, double z, double w) {
    if (MathUtil.isZero(w)) {
      return new Vector3(x, y, z);
    } else if (MathUtil.equal(w, 1.0)) {
      return new Point3(x, y, z);
    } else {
      throw new IllegalArgumentException("w must be 0 or 1");
    }
  }

  /**
   * Creates a homogenized point.
   * @param v The <code>Vector4</code> indicating the coordinate values.
   * @return An <code>HPoint3</code> corresponding to the given coordinates.
   *     If <code>v.w() == 0</code>, a <code>Vector3</code> is returned.  If
   *     <code>v.w() == 1</code>, a <code>Point3</code> is returned.
   * @throws IllegalArgumentException if <code>v.w()</code> is not within
   *     <code>MathUtil.EPSILON</code> of <code>0.0</code> or
   *     <code>1.0</code>.
   * @see MathUtil#EPSILON
   * @see Point3
   * @see Vector3
   * @see Vector4
   * @see Vector4#w()
   */
  public static HPoint3 create(Vector4 v) {
    return create(v.x(), v.y(), v.z(), v.w());
  }

  /**
   * Gets a value indicating if this <code>HPoint3</code> is a
   * <code>Point3</code>.
   * @return A value indicating if this <code>HPoint3</code> is a
   *     <code>Point3</code>.
   */
  public final boolean isPoint() {
    return this instanceof Point3;
  }

  /**
   * Gets a value indicating if this <code>HPoint3</code> is a
   * <code>Vector3</code>.
   * @return A value indicating if this <code>HPoint3</code> is a
   *     <code>Vector3</code>.
   */
  public final boolean isVector() {
    return this instanceof Vector3;
  }

  /**
   * Converts this <code>HPoint3</code> to a <code>Point3</code>.
   * @return If this <code>HPoint3</code> is a <code>Point3</code>, this
   *     object is returned, otherwise <code>null</code> is returned.
   */
  public final Point3 toPoint3() {
    return (Point3) this;
  }

  /**
   * Converts this <code>HPoint3</code> to a <code>Vector3</code>.
   * @return If this <code>HPoint3</code> is a <code>Vector3</code>, this
   *     object is returned, otherwise <code>null</code> is returned.
   */
  public final Vector3 toVector3() {
    return (Vector3) this;
  }

  /**
   * Gets a representation of this <code>HPoint3</code> as a 4 dimensional
   * vector.
   * @return A <code>Vector4</code> representation of this
   *     <code>HPoint3</code>.
   */
  public final Vector4 toVector4() {
    return new Vector4(x, y, z, w());
  }

  /**
   * Gets the distance from the origin along the x-axis.
   * @return The distance from the origin along the x-axis.
   */
  public final double x() {
    return x;
  }

  /**
   * Gets the distance from the origin along the y-axis.
   * @return The distance from the origin along the y-axis.
   */
  public final double y() {
    return y;
  }

  /**
   * Gets the distance from the origin along the z-axis.
   * @return The distance from the origin along the z-axis.
   */
  public final double z() {
    return z;
  }

  /**
   * Gets the distance from the origin along the w-axis.
   * @return The distance from the origin along the w-axis.
   */
  public abstract double w();

  /**
   * Adds a <code>Vector3</code> to this <code>HPoint3</code>.
   * @param v The <code>Vector3</code> to add.
   * @return The sum of this <code>HPoint3</code> and <code>v</code>.
   */
  public abstract HPoint3 plus(Vector3 v);

  /**
   * Subtracts a <code>Vector3</code> from this <code>HPoint3</code>.
   * @param v The <code>Vector3</code> to subtract.
   * @return The difference of this <code>HPoint3</code> and <code>v</code>.
   */
  public abstract HPoint3 minus(Vector3 v);

}
