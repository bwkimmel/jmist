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
 * A class for classes implementing <code>AffineTransformable2</code> that
 * require the inverse of the transformation matrix.
 *
 * @author Brad Kimmel
 */
public class InvertibleAffineTransformation2 extends AffineTransformation2
    implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 338292224394167644L;
  
  /** The inverse transformation matrix. */
  private AffineMatrix2 inverse = null;

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AffineTransformation2#rotate(double)
   */
  @Override
  public void rotate(double angle) {
    super.rotate(angle);
    this.applyInverseTransformation(LinearMatrix2
        .rotateMatrix(-angle));
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#scale(double)
   */
  @Override
  public void scale(double c) {
    super.scale(c);
    this.applyInverseTransformation(LinearMatrix2.scaleMatrix(1.0 / c));
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#stretch(double,
   *      double, double)
   */
  @Override
  public void stretch(double cx, double cy) {
    super.stretch(cx, cy);
    this.applyInverseTransformation(LinearMatrix2.stretchMatrix(1.0 / cx,
        1.0 / cy));
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#stretch(ca.eandb.jmist.toolkit.Vector2,
   *      double)
   */
  @Override
  public void stretch(Vector2 axis, double c) {
    super.stretch(axis, c);
    this.applyInverseTransformation(LinearMatrix2.stretchMatrix(axis,
        1.0 / c));
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#stretchX(double)
   */
  @Override
  public void stretchX(double cx) {
    super.stretchX(cx);
    this.applyInverseTransformation(LinearMatrix2.stretchXMatrix(1.0 / cx));
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#stretchY(double)
   */
  @Override
  public void stretchY(double cy) {
    super.stretchY(cy);
    this.applyInverseTransformation(LinearMatrix2.stretchYMatrix(1.0 / cy));
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#transform(ca.eandb.jmist.toolkit.AffineMatrix2)
   */
  @Override
  public void transform(AffineMatrix2 T) {
    super.transform(T);
    this.applyInverseTransformation(T.inverse());
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#transform(ca.eandb.jmist.toolkit.LinearMatrix2)
   */
  @Override
  public void transform(LinearMatrix2 T) {
    super.transform(T);
    this.applyInverseTransformation(T.inverse());
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#translate(ca.eandb.jmist.toolkit.Vector2)
   */
  @Override
  public void translate(Vector2 v) {
    super.translate(v);
    this.applyInverseTransformation(AffineMatrix2.translateMatrix(v
        .opposite()));
  }

  /**
   * Applies the specified inverse transformation matrix to the current
   * inverse transformation.
   *
   * @param Tinv
   *            The inverse of the <code>AffineMatrix2</code> that is being
   *            applied.
   */
  private void applyInverseTransformation(AffineMatrix2 Tinv) {
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
   *            The inverse of the <code>LinearMatrix2</code> that is being
   *            applied.
   */
  private void applyInverseTransformation(LinearMatrix2 Tinv) {
    this.applyInverseTransformation(new AffineMatrix2(Tinv));
  }

  /**
   * Gets the inverse transformation matrix.
   *
   * @return The <code>AffineMatrix2</code> representing the inverse of this
   *         transformation.
   */
  protected AffineMatrix2 getInverseTransformationMatrix() {
    return this.inverse != null ? this.inverse : AffineMatrix2.IDENTITY;
  }

  /**
   * Applies this transformation to the specified
   * <code>InvertibleAffineTransformation2</code>.
   *
   * @param trans
   *            The <code>InvertibleAffineTransformation2</code> to apply
   *            this transformation to.
   */
  public void apply(InvertibleAffineTransformation2 trans) {
    if (this.isDirty()) {
      trans.applyTransformation(super.getTransformationMatrix());
      trans.applyInverseTransformation(this.inverse);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see ca.eandb.jmist.framework.AffineTransformation2#apply(ca.eandb.jmist.framework.AffineTransformable2)
   */
  @Override
  public void apply(AffineTransformable2 to) {
    if (to instanceof InvertibleAffineTransformation2) {
      this.apply((InvertibleAffineTransformation2) to);
    } else {
      super.apply(to);
    }
  }

  /**
   * Applies the inverse of this transformation to the specified
   * <code>InvertibleAffineTransformation2</code>.
   *
   * @param trans
   *            The <code>InvertibleAffineTransformation2</code> to apply
   *            the inverse of this transformation to.
   */
  public void applyInverse(InvertibleAffineTransformation2 trans) {
    if (this.isDirty()) {
      trans.applyTransformation(this.inverse);
      trans.applyInverseTransformation(super.getTransformationMatrix());
    }
  }

  /**
   * Applies the inverse of this <code>AffineTransformation2</code> to
   * another object that is affine transformable.
   *
   * @param to
   *            The <code>AffineTransformable2</code> object to apply the
   *            inverse of this transformation to.
   */
  public void applyInverse(AffineTransformable2 to) {
    if (to instanceof InvertibleAffineTransformation2) {
      this.applyInverse((InvertibleAffineTransformation2) to);
    } else if (this.inverse != null) {
      to.transform(this.inverse);
    }
  }

  /**
   * Applies the inverse of this <code>AffineTransformation2</code> to a
   * <code>AffineMatrix2</code>.
   *
   * @param matrix
   *            The <code>AffineMatrix2</code> object to apply the inverse
   *            of this transformation to.
   * @return The transformed <code>AffineMatrix2</code>.
   */
  public AffineMatrix2 applyInverse(AffineMatrix2 matrix) {
    return this.inverse != null ? this.inverse.times(matrix) : matrix;
  }

  /**
   * Applies the inverse of this <code>AffineTransformation2</code> to a
   * <code>HPoint2</code>.
   *
   * @param p
   *            The <code>HPoint2</code> object to apply the inverse of this
   *            transformation to.
   * @return The transformed <code>HPoint2</code>.
   */
  public HPoint2 applyInverse(HPoint2 p) {
    return this.inverse != null ? this.inverse.times(p) : p;
  }

  /**
   * Applies the inverse of this <code>AffineTransformation2</code> to a
   * <code>Point2</code>.
   *
   * @param p
   *            The <code>Point2</code> object to apply the inverse of this
   *            transformation to.
   * @return The transformed <code>Point2</code>.
   */
  public Point2 applyInverse(Point2 p) {
    return this.inverse != null ? this.inverse.times(p) : p;
  }

  /**
   * Applies the inverse of this <code>AffineTransformation2</code> to a
   * <code>Vector2</code>.
   *
   * @param v
   *            The <code>Vector2</code> object to apply the inverse of this
   *            transformation to.
   * @return The transformed <code>Vector2</code>.
   */
  public Vector2 applyInverse(Vector2 v) {
    return this.inverse != null ? this.inverse.times(v) : v;
  }

  /**
   * Applies the inverse of this <code>AffineTransformation2</code> to a
   * <code>Ray2</code>.
   *
   * @param ray
   *            The <code>Ray2</code> object to apply the inverse of this
   *            transformation to.
   * @return The transformed <code>Ray2</code>.
   */
  public Ray2 applyInverse(Ray2 ray) {
    return this.inverse != null ? ray.transform(this.inverse) : ray;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AffineTransformation2#reset()
   */
  @Override
  public void reset() {
    super.reset();
    this.inverse = null;
  }

}
