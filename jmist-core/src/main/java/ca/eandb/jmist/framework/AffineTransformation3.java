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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * An abstract base class for classes implementing
 * <code>AffineTransformable3</code>.
 * @author Brad Kimmel
 */
public class AffineTransformation3 implements AffineTransformable3, Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = -4028593857879980021L;

  /** The cumulative transformation matrix. */
  private AffineMatrix3 matrix = null;

  @Override
  public void transform(AffineMatrix3 T) {
    this.applyTransformation(T);
  }

  @Override
  public void transform(LinearMatrix3 T) {
    this.applyTransformation(T);
  }

  @Override
  public void rotate(Vector3 axis, double angle) {
    this.applyTransformation(LinearMatrix3.rotateMatrix(axis, angle));
  }

  @Override
  public void rotateX(double angle) {
    this.applyTransformation(LinearMatrix3.rotateXMatrix(angle));
  }

  @Override
  public void rotateY(double angle) {
    this.applyTransformation(LinearMatrix3.rotateYMatrix(angle));
  }

  @Override
  public void rotateZ(double angle) {
    this.applyTransformation(LinearMatrix3.rotateZMatrix(angle));
  }

  @Override
  public void translate(Vector3 v) {
    this.applyTransformation(AffineMatrix3.translateMatrix(v));
  }

  @Override
  public void scale(double c) {
    this.applyTransformation(LinearMatrix3.scaleMatrix(c));
  }

  @Override
  public void stretch(Vector3 axis, double c) {
    this.applyTransformation(LinearMatrix3.stretchMatrix(axis, c));
  }

  @Override
  public void stretch(double cx, double cy, double cz) {
    this.applyTransformation(LinearMatrix3.stretchMatrix(cx, cy, cz));
  }

  @Override
  public void stretchX(double cx) {
    this.applyTransformation(LinearMatrix3.stretchXMatrix(cx));
  }

  @Override
  public void stretchY(double cy) {
    this.applyTransformation(LinearMatrix3.stretchYMatrix(cy));
  }

  @Override
  public void stretchZ(double cz) {
    this.applyTransformation(LinearMatrix3.stretchZMatrix(cz));
  }

  /**
   * Applies this <code>AffineTransformation3</code> to another object that
   * is affine transformable.
   * @param to The <code>AffineTransformable3</code> object to apply this
   *     transformation to.
   */
  public void apply(AffineTransformable3 to) {
    if (this.matrix != null) {
      to.transform(this.matrix);
    }
  }

  /**
   * Applies this <code>AffineTransformation3</code> to a
   * <code>AffineMatrix3</code>.
   * @param matrix The <code>AffineMatrix3</code> object to apply this
   *     transformation to.
   * @return The transformed <code>AffineMatrix3</code>.
   */
  public AffineMatrix3 apply(AffineMatrix3 matrix) {
    return this.matrix != null ? this.matrix.times(matrix) : matrix;
  }

  /**
   * Applies this <code>AffineTransformation3</code> to a
   * <code>Point3</code>.
   * @param p The <code>Point3</code> object to apply this transformation to.
   * @return The transformed <code>Point3</code>.
   */
  public Point3 apply(Point3 p) {
    return this.matrix != null ? this.matrix.times(p) : p;
  }

  /**
   * Applies this <code>AffineTransformation3</code> to a
   * <code>Vector3</code>.
   * @param v The <code>Vector3</code> object to apply this transformation
   *     to.
   * @return The transformed <code>Vector3</code>.
   */
  public Vector3 apply(Vector3 v) {
    return this.matrix != null ? this.matrix.times(v) : v;
  }

  /**
   * Applies this <code>AffineTransformation3</code> to a
   * <code>HPoint3</code>.
   * @param p The <code>HPoint3</code> to apply this transformation to.
   * @return The transformed <code>HPoint3</code>.
   */
  public HPoint3 apply(HPoint3 p) {
    return this.matrix != null ? this.matrix.times(p) : p;
  }

  /**
   * Applies this <code>AffineTransformation3</code> to a <code>Ray3</code>.
   * @param ray The <code>Ray3</code> object to apply this transformation to.
   * @return The transformed <code>Ray3</code>.
   */
  public Ray3 apply(Ray3 ray) {
    return this.matrix != null ? ray.transform(this.matrix) : ray;
  }

  /**
   * Applies the transformation represented by the specified
   * <code>AffineMatrix3</code> to this <code>AffineTransformation3</code>.
   * @param T The <code>AffineMatrix3</code> representing the transformation
   *     to be applied.
   */
  protected final void applyTransformation(AffineMatrix3 T) {
    if (this.matrix == null) {
      this.matrix = T;
    } else {
      this.matrix = T.times(this.matrix);
    }
  }

  /**
   * Applies the transformation represented by the specified
   * <code>LinearMatrix3</code> to this <code>AffineTransformation3</code>.
   * @param T The <code>LinearMatrix3</code> representing the transformation
   *     to be applied.
   */
  protected final void applyTransformation(LinearMatrix3 T) {
    this.applyTransformation(new AffineMatrix3(T));
  }

  /**
   * Gets the cumulative transformation matrix.
   * @return The cumulative transformation matrix.
   */
  protected final AffineMatrix3 getTransformationMatrix() {
    return matrix != null ? matrix : AffineMatrix3.IDENTITY;
  }

  /**
   * Gets a value indicating whether a transformation has been applied.
   * @return A value indicating whether a transformation has been applied.
   */
  public final boolean isDirty() {
    return matrix != null;
  }

  /**
   * Resets the transformation to the identity transformation.
   */
  public void reset() {
    this.matrix = null;
  }

}
