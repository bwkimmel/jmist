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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.math.AffineMatrix2;
import ca.eandb.jmist.math.HPoint2;
import ca.eandb.jmist.math.LinearMatrix2;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray2;
import ca.eandb.jmist.math.Vector2;

/**
 * An abstract base class for classes implementing
 * <code>AffineTransformable2</code>.
 * @author Brad Kimmel
 */
public class AffineTransformation2 implements AffineTransformable2, Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 1271328080514598930L;

  /** The cumulative transformation matrix. */
  private AffineMatrix2 matrix = null;

  @Override
  public void transform(AffineMatrix2 T) {
    this.applyTransformation(T);
  }

  @Override
  public void transform(LinearMatrix2 T) {
    this.applyTransformation(T);
  }

  @Override
  public void rotate(double angle) {
    this.applyTransformation(LinearMatrix2.rotateMatrix(angle));
  }

  @Override
  public void translate(Vector2 v) {
    this.applyTransformation(AffineMatrix2.translateMatrix(v));
  }

  @Override
  public void scale(double c) {
    this.applyTransformation(LinearMatrix2.scaleMatrix(c));
  }

  @Override
  public void stretch(Vector2 axis, double c) {
    this.applyTransformation(LinearMatrix2.stretchMatrix(axis, c));
  }

  @Override
  public void stretch(double cx, double cy) {
    this.applyTransformation(LinearMatrix2.stretchMatrix(cx, cy));
  }

  @Override
  public void stretchX(double cx) {
    this.applyTransformation(LinearMatrix2.stretchXMatrix(cx));
  }

  @Override
  public void stretchY(double cy) {
    this.applyTransformation(LinearMatrix2.stretchYMatrix(cy));
  }

  /**
   * Applies this <code>AffineTransformation2</code> to another object that
   * is affine transformable.
   * @param to The <code>AffineTransformable2</code> object to apply this
   *     transformation to.
   */
  public void apply(AffineTransformable2 to) {
    if (this.matrix != null) {
      to.transform(this.matrix);
    }
  }

  /**
   * Applies this <code>AffineTransformation2</code> to a
   * <code>AffineMatrix2</code>.
   * @param matrix The <code>AffineMatrix2</code> object to apply this
   *     transformation to.
   * @return The transformed <code>AffineMatrix2</code>.
   */
  public AffineMatrix2 apply(AffineMatrix2 matrix) {
    return this.matrix != null ? this.matrix.times(matrix) : matrix;
  }

  /**
   * Applies this <code>AffineTransformation2</code> to a
   * <code>Point2</code>.
   * @param p The <code>Point2</code> object to apply this transformation to.
   * @return The transformed <code>Point2</code>.
   */
  public Point2 apply(Point2 p) {
    return this.matrix != null ? this.matrix.times(p) : p;
  }

  /**
   * Applies this <code>AffineTransformation2</code> to a
   * <code>Vector2</code>.
   * @param v The <code>Vector2</code> object to apply this transformation
   *     to.
   * @return The transformed <code>Vector2</code>.
   */
  public Vector2 apply(Vector2 v) {
    return this.matrix != null ? this.matrix.times(v) : v;
  }

  /**
   * Applies this <code>AffineTransformation2</code> to a
   * <code>HPoint2</code>.
   * @param p The <code>HPoint2</code> to apply this transformation to.
   * @return The transformed <code>HPoint2</code>.
   */
  public HPoint2 apply(HPoint2 p) {
    return this.matrix != null ? this.matrix.times(p) : p;
  }

  /**
   * Applies this <code>AffineTransformation2</code> to a <code>Ray2</code>.
   * @param ray The <code>Ray2</code> object to apply this transformation to.
   * @return The transformed <code>Ray2</code>.
   */
  public Ray2 apply(Ray2 ray) {
    return this.matrix != null ? ray.transform(this.matrix) : ray;
  }

  /**
   * Applies the transformation represented by the specified
   * <code>AffineMatrix2</code> to this <code>AffineTransformation2</code>.
   * @param T The <code>AffineMatrix2</code> representing the transformation
   *     to be applied.
   */
  protected final void applyTransformation(AffineMatrix2 T) {
    if (this.matrix == null) {
      this.matrix = T;
    } else {
      this.matrix = T.times(this.matrix);
    }
  }

  /**
   * Applies the transformation represented by the specified
   * <code>LinearMatrix2</code> to this <code>AffineTransformation2</code>.
   * @param T The <code>LinearMatrix2</code> representing the transformation
   *     to be applied.
   */
  protected final void applyTransformation(LinearMatrix2 T) {
    this.applyTransformation(new AffineMatrix2(T));
  }

  /**
   * Gets the cumulative transformation matrix.
   * @return The cumulative transformation matrix.
   */
  protected final AffineMatrix2 getTransformationMatrix() {
    return matrix != null ? matrix : AffineMatrix2.IDENTITY;
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
