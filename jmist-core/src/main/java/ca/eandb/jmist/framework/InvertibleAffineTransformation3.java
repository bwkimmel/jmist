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
 * A class for classes implementing <code>AffineTransformable3</code> that
 * require the inverse of the transformation matrix.
 *
 * @author Brad Kimmel
 */
public class InvertibleAffineTransformation3 extends AffineTransformation3
    implements Serializable {

  @Override
  public void rotate(Vector3 axis, double angle) {
    super.rotate(axis, angle);
    this.applyInverseTransformation(LinearMatrix3
        .rotateMatrix(axis, -angle));
  }

  @Override
  public void rotateX(double angle) {
    super.rotateX(angle);
    this.applyInverseTransformation(LinearMatrix3.rotateXMatrix(-angle));
  }

  @Override
  public void rotateY(double angle) {
    super.rotateY(angle);
    this.applyInverseTransformation(LinearMatrix3.rotateYMatrix(-angle));
  }

  @Override
  public void rotateZ(double angle) {
    super.rotateZ(angle);
    this.applyInverseTransformation(LinearMatrix3.rotateZMatrix(-angle));
  }

  @Override
  public void scale(double c) {
    super.scale(c);
    this.applyInverseTransformation(LinearMatrix3.scaleMatrix(1.0 / c));
  }

  @Override
  public void stretch(double cx, double cy, double cz) {
    super.stretch(cx, cy, cz);
    this.applyInverseTransformation(LinearMatrix3.stretchMatrix(1.0 / cx,
        1.0 / cy, 1.0 / cz));
  }

  @Override
  public void stretch(Vector3 axis, double c) {
    super.stretch(axis, c);
    this.applyInverseTransformation(LinearMatrix3.stretchMatrix(axis,
        1.0 / c));
  }

  @Override
  public void stretchX(double cx) {
    super.stretchX(cx);
    this.applyInverseTransformation(LinearMatrix3.stretchXMatrix(1.0 / cx));
  }

  @Override
  public void stretchY(double cy) {
    super.stretchY(cy);
    this.applyInverseTransformation(LinearMatrix3.stretchYMatrix(1.0 / cy));
  }

  @Override
  public void stretchZ(double cz) {
    super.stretchZ(cz);
    this.applyInverseTransformation(LinearMatrix3.stretchZMatrix(1.0 / cz));
  }

  @Override
  public void transform(AffineMatrix3 T) {
    super.transform(T);
    this.applyInverseTransformation(T.inverse());
  }

  @Override
  public void transform(LinearMatrix3 T) {
    super.transform(T);
    this.applyInverseTransformation(T.inverse());
  }

  @Override
  public void translate(Vector3 v) {
    super.translate(v);
    this.applyInverseTransformation(AffineMatrix3.translateMatrix(v
        .opposite()));
  }

  /**
   * Applies the specified inverse transformation matrix to the current
   * inverse transformation.
   *
   * @param Tinv
   *            The inverse of the <code>AffineMatrix3</code> that is being
   *            applied.
   */
  private void applyInverseTransformation(AffineMatrix3 Tinv) {
    if (this.inverse == null) {
      this.inverse = Tinv;
    } else {
      this.inverse = this.inverse.times(Tinv);
    }
  }

  /**
   * Applies the specified inverse transformation matrix to the current
   * inverse transformation.
   *
   * @param Tinv
   *            The inverse of the <code>LinearMatrix3</code> that is being
   *            applied.
   */
  private void applyInverseTransformation(LinearMatrix3 Tinv) {
    this.applyInverseTransformation(new AffineMatrix3(Tinv));
  }

  /**
   * Gets the inverse transformation matrix.
   *
   * @return The <code>AffineMatrix3</code> representing the inverse of this
   *         transformation.
   */
  protected AffineMatrix3 getInverseTransformationMatrix() {
    return this.inverse != null ? this.inverse : AffineMatrix3.IDENTITY;
  }

  /**
   * Applies this transformation to the specified
   * <code>InvertibleAffineTransformation3</code>.
   *
   * @param trans
   *            The <code>InvertibleAffineTransformation3</code> to apply
   *            this transformation to.
   */
  public void apply(InvertibleAffineTransformation3 trans) {
    if (this.isDirty()) {
      trans.applyTransformation(super.getTransformationMatrix());
      trans.applyInverseTransformation(this.inverse);
    }
  }

  @Override
  public void apply(AffineTransformable3 to) {
    if (to instanceof InvertibleAffineTransformation3) {
      this.apply((InvertibleAffineTransformation3) to);
    } else {
      super.apply(to);
    }
  }

  /**
   * Applies the inverse of this transformation to the specified
   * <code>InvertibleAffineTransformation3</code>.
   *
   * @param trans
   *            The <code>InvertibleAffineTransformation3</code> to apply
   *            the inverse of this transformation to.
   */
  public void applyInverse(InvertibleAffineTransformation3 trans) {
    if (this.isDirty()) {
      trans.applyTransformation(this.inverse);
      trans.applyInverseTransformation(super.getTransformationMatrix());
    }
  }

  /**
   * Applies the inverse of this <code>AffineTransformation3</code> to
   * another object that is affine transformable.
   *
   * @param to
   *            The <code>AffineTransformable3</code> object to apply the
   *            inverse of this transformation to.
   */
  public void applyInverse(AffineTransformable3 to) {
    if (to instanceof InvertibleAffineTransformation3) {
      this.applyInverse((InvertibleAffineTransformation3) to);
    } else if (this.inverse != null) {
      to.transform(this.inverse);
    }
  }

  /**
   * Applies the inverse of this <code>AffineTransformation3</code> to a
   * <code>AffineMatrix3</code>.
   *
   * @param matrix
   *            The <code>AffineMatrix3</code> object to apply the inverse
   *            of this transformation to.
   * @return The transformed <code>AffineMatrix3</code>.
   */
  public AffineMatrix3 applyInverse(AffineMatrix3 matrix) {
    return this.inverse != null ? this.inverse.times(matrix) : matrix;
  }

  /**
   * Applies the inverse of this <code>AffineTransformation3</code> to a
   * <code>HPoint3</code>.
   *
   * @param p
   *            The <code>HPoint3</code> object to apply the inverse of this
   *            transformation to.
   * @return The transformed <code>HPoint3</code>.
   */
  public HPoint3 applyInverse(HPoint3 p) {
    return this.inverse != null ? this.inverse.times(p) : p;
  }

  /**
   * Applies the inverse of this <code>AffineTransformation3</code> to a
   * <code>Point3</code>.
   *
   * @param p
   *            The <code>Point3</code> object to apply the inverse of this
   *            transformation to.
   * @return The transformed <code>Point3</code>.
   */
  public Point3 applyInverse(Point3 p) {
    return this.inverse != null ? this.inverse.times(p) : p;
  }

  /**
   * Applies the inverse of this <code>AffineTransformation3</code> to a
   * <code>Vector3</code>.
   *
   * @param v
   *            The <code>Vector3</code> object to apply the inverse of this
   *            transformation to.
   * @return The transformed <code>Vector3</code>.
   */
  public Vector3 applyInverse(Vector3 v) {
    return this.inverse != null ? this.inverse.times(v) : v;
  }

  /**
   * Applies the inverse of this <code>AffineTransformation3</code> to a
   * <code>Ray3</code>.
   *
   * @param ray
   *            The <code>Ray3</code> object to apply the inverse of this
   *            transformation to.
   * @return The transformed <code>Ray3</code>.
   */
  public Ray3 applyInverse(Ray3 ray) {
    return this.inverse != null ? ray.transform(this.inverse) : ray;
  }

  @Override
  public void reset() {
    super.reset();
    this.inverse = null;
  }

  /** The inverse transformation matrix. */
  private AffineMatrix3 inverse = null;

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -5759913323363262892L;

}
