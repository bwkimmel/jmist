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
 * A 4x4 matrix of the form
 * \[\left(
 * \begin{matrix}
 *   a &amp; b &amp; c &amp; 0 \\
 *   d &amp; e &amp; f &amp; 0 \\
 *   g &amp; h &amp; i &amp; 0 \\
 *   0 &amp; 0 &amp; 0 &amp; 1
 * \end{matrix}
 * \right)\]
 * for applying three dimensional linear transformations.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class LinearMatrix3 implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = -2238797743118681949L;

  /** The identity matrix ({@code this * IDENTITY == this}). */
  public static final LinearMatrix3 IDENTITY = new LinearMatrix3(
      1.0, 0.0, 0.0,
      0.0, 1.0, 0.0,
      0.0, 0.0, 1.0);

  /** The zero matrix ({@code this + IDENTITY == this}). */
  public static final LinearMatrix3 ZERO = new LinearMatrix3(
      0.0, 0.0, 0.0,
      0.0, 0.0, 0.0,
      0.0, 0.0, 0.0);

  /* Matrix elements */
  private final double
      _00, _01, _02,
      _10, _11, _12,
      _20, _21, _22;

  /**
   * Initializes the matrix from its elements.
   * @param _00 The element in row 0, column 0.
   * @param _01 The element in row 0, column 1.
   * @param _02 The element in row 0, column 2.
   * @param _10 The element in row 1, column 0.
   * @param _11 The element in row 1, column 1.
   * @param _12 The element in row 1, column 2.
   * @param _20 The element in row 2, column 0.
   * @param _21 The element in row 2, column 1.
   * @param _22 The element in row 2, column 2.
   */
  public LinearMatrix3(
      double _00, double _01, double _02,
      double _10, double _11, double _12,
      double _20, double _21, double _22
      ) {
    this._00 = _00; this._01 = _01; this._02 = _02;
    this._10 = _10; this._11 = _11; this._12 = _12;
    this._20 = _20; this._21 = _21; this._22 = _22;
  }

  /**
   * Computes the product of this matrix with another.
   * @param other The matrix by which to multiply this matrix.
   * @return The product of this matrix and other.
   */
  public LinearMatrix3 times(LinearMatrix3 other) {
    return new LinearMatrix3(
        _00 * other._00 + _01 * other._10 + _02 * other._20,
        _00 * other._01 + _01 * other._11 + _02 * other._21,
        _00 * other._02 + _01 * other._12 + _02 * other._22,
        _10 * other._00 + _11 * other._10 + _12 * other._20,
        _10 * other._01 + _11 * other._11 + _12 * other._21,
        _10 * other._02 + _11 * other._12 + _12 * other._22,
        _20 * other._00 + _21 * other._10 + _22 * other._20,
        _20 * other._01 + _21 * other._11 + _22 * other._21,
        _20 * other._02 + _21 * other._12 + _22 * other._22
    );
  }

  /**
   * Multiplies this matrix by the inverse of the specified matrix.
   * @param other The matrix by which to divide this matrix.
   * @return The product of this matrix and the inverse of other.
   * @see #inverse()
   */
  public LinearMatrix3 divide(LinearMatrix3 other) {
    return this.times(other.inverse());
  }

  /**
   * Computes the inverse of this matrix.
   * The product of {@code this} and {@code this.inverse()} will be
   * the identity matrix.
   * @return The inverse of this matrix.
   */
  public LinearMatrix3 inverse() {
    double det = determinant();

    return new LinearMatrix3(
        (_11 * _22 - _12 * _21) / det, (_02 * _21 - _01 * _22) / det, (_01 * _12 - _02 * _11) / det,
        (_12 * _20 - _10 * _22) / det, (_00 * _22 - _02 * _20) / det, (_02 * _10 - _00 * _12) / det,
        (_10 * _21 - _11 * _20) / det, (_01 * _20 - _00 * _21) / det, (_00 * _11 - _01 * _10) / det
    );
  }

  /**
   * Computes the transpose of this matrix.
   * @return The transpose of this matrix.
   */
  public LinearMatrix3 transposed() {
    return new LinearMatrix3(
        _00, _10, _20,
        _01, _11, _21,
        _02, _12, _22
    );
  }

  /**
   * Finds the determinant of this matrix (i.e., the volume of the
   * unit cube after the transformation represented by this matrix
   * is applied).
   * @return The determinant of this matrix.
   */
  public double determinant() {
    return _00 * (_11 * _22 - _12 * _21) +
           _01 * (_12 * _20 - _10 * _22) +
           _02 * (_10 * _21 - _11 * _20);
  }

  /**
   * Gets the trace of this matrix (i.e., the sum of the diagonal elements).
   * @return The trace of this matrix.
   */
  public double trace() {
    return _00 + _11 + _22;
  }

  /**
   * Gets the characteristic polynomial of this matrix (i.e., the univariate
   * polynomial in <code>&lambda;</code> given by
   * <code>det(A - &lambda;I)</code>).
   * @return The characteristic <code>Polynomial</code> of this matrix.
   * @see ca.eandb.jmist.math.Polynomial
   */
  public Polynomial characteristic() {
    double c0 = determinant();
    double c1 = _10 * _01 + _20 * _02 + _12 * _21 - _00 * _11 - _00 * _22 - _11 * _22;
    double c2 = trace();
    double c3 = -1.0;

    return new Polynomial(c0, c1, c2, c3);
  }

  /**
   * Returns an array of the real eigenvalues of this matrix (i.e., the
   * values <code>&lambda;</code> for which there exists a non-trivial
   * vector <code>v</code> satisfying <code>Av = &lambda;v</code>).
   * @return An array containing the real eigenvalues of this matrix.
   */
  public double[] eigenvalues() {
    return characteristic().roots();
  }

  /**
   * Returns an array of the <code>Complex</code> eigenvalues of this matrix
   * (i.e., the values <code>&lambda;</code> for which there exists a
   * non-trivial vector <code>v</code> satisfying
   * <code>Av = &lambda;v</code>).  This method is guaranteed to return an
   * array of three (3) elements.
   * @return An array containing the <code>Complex</code> eigenvalues of
   *     this matrix.
   * @see ca.eandb.jmist.math.Complex
   */
  public Complex[] complexEigenvalues() {
    return characteristic().complexRoots();
  }

  /**
   * Gets an element of the matrix.
   * @param row The row containing the element to get (0 &lt;= row &lt; 3).
   * @param col The column containing the element to get (0 &lt;= col &lt; 3).
   * @return The value of the element at the specified position.
   */
  public double at(int row, int col) {
    switch (row) {
    case 0:
      switch (col) {
      case 0: return _00;
      case 1: return _01;
      case 2: return _02;
      }
    case 1:
      switch (col) {
      case 0: return _10;
      case 1: return _11;
      case 2: return _12;
      }
    case 2:
      switch (col) {
      case 0: return _20;
      case 1: return _21;
      case 2: return _22;
      }
    }
    throw new IndexOutOfBoundsException();
  }

  /**
   * Transforms the specified <code>HPoint3</code> according to the
   * transformation represented by this <code>LinearMatrix3</code>.
   * @param p The <code>HPoint3</code> to transform.
   * @return The transformed <code>HPoint3</code>.
   */
  public HPoint3 times(HPoint3 p) {
    return p.isPoint() ? times(p.toVector3()) : times(p.toPoint3());
  }

  /**
   * Transforms the specified <code>Vector3</code> according to the
   * transformation representing by this <code>LinearMatrix3</code>.
   * @param v The <code>Vector3</code> to transform.
   * @return The transformed <code>Vector3</code>.
   */
  public Vector3 times(Vector3 v) {
    return new Vector3(
        _00 * v.x() + _01 * v.y() + _02 * v.z(),
        _10 * v.x() + _11 * v.y() + _12 * v.z(),
        _20 * v.x() + _21 * v.y() + _22 * v.z()
    );
  }

  /**
   * Transforms the specified <code>Point3</code> according to the
   * transformation representing by this <code>LinearMatrix3</code>.
   * @param p The <code>Point3</code> to transform.
   * @return The transformed <code>Point3</code>.
   */
  public Point3 times(Point3 p) {
    return new Point3(
        _00 * p.x() + _01 * p.y() + _02 * p.z(),
        _10 * p.x() + _11 * p.y() + _12 * p.z(),
        _20 * p.x() + _21 * p.y() + _22 * p.z()
    );
  }

  /**
   * Returns a scaling matrix.
   * @param c The factor by which to scale.
   * @return A scaling matrix.
   */
  public static LinearMatrix3 scaleMatrix(double c) {
    return new LinearMatrix3(
        c, 0.0, 0.0,
        0.0, c, 0.0,
        0.0, 0.0, c
    );
  }

  /**
   * Returns a matrix for scaling by independent factors along
   * each axis.
   * @param cx The factor by which to scale along the x-axis.
   * @param cy The factor by which to scale along the y-axis.
   * @param cz The factor by which to scale along the z-axis.
   * @return The stretch matrix.
   */
  public static LinearMatrix3 stretchMatrix(double cx, double cy, double cz) {
    return new LinearMatrix3(
        cx, 0.0, 0.0,
        0.0, cy, 0.0,
        0.0, 0.0, cz
    );
  }

  /**
   * Returns a matrix for stretching along the x-axis.
   * @param cx The factor by which to scale along the x-axis.
   * @return The stretch matrix.
   */
  public static LinearMatrix3 stretchXMatrix(double cx) {
    return new LinearMatrix3(
        cx , 0.0, 0.0,
        0.0, 1.0, 0.0,
        0.0, 0.0, 1.0
    );
  }

  /**
   * Returns a matrix for stretching along the y-axis.
   * @param cy The factor by which to scale along the y-axis.
   * @return The stretch matrix.
   */
  public static LinearMatrix3 stretchYMatrix(double cy) {
    return new LinearMatrix3(
        1.0, 0.0, 0.0,
        0.0, cy , 0.0,
        0.0, 0.0, 1.0
    );
  }

  /**
   * Returns a matrix for stretching along the z-axis.
   * @param cz The factor by which to scale along the z-axis.
   * @return The stretch matrix.
   */
  public static LinearMatrix3 stretchZMatrix(double cz) {
    return new LinearMatrix3(
        1.0, 0.0, 0.0,
        0.0, 1.0, 0.0,
        0.0, 0.0, cz
    );
  }

  /**
   * Returns a matrix for stretching along an arbitrary axis.
   * @param axis The axis to stretch along.
   * @param c The factor to stretch by.
   * @return The stretch matrix.
   */
  public static LinearMatrix3 stretchMatrix(Vector3 axis, double c) {
    // TODO implement this method.
    throw new UnsupportedOperationException("not yet implemented");
  }

  /**
   * Returns a matrix for rotating about the x-axis.
   * @param theta The angle to rotate by.
   * @return The rotation matrix.
   */
  public static LinearMatrix3 rotateXMatrix(double theta) {
    return new LinearMatrix3(
        1.0, 0.0, 0.0,
        0.0, Math.cos(theta), -Math.sin(theta),
        0.0, Math.sin(theta), Math.cos(theta)
    );
  }

  /**
   * Returns a matrix for rotating about the y-axis.
   * @param theta The angle to rotate by.
   * @return The rotation matrix.
   */
  public static LinearMatrix3 rotateYMatrix(double theta) {
    return new LinearMatrix3(
        Math.cos(theta), 0.0, Math.sin(theta),
        0.0, 1.0, 0.0,
        -Math.sin(theta), 0.0, Math.cos(theta)
    );
  }

  /**
   * Returns a matrix for rotating about the z-axis.
   * @param theta The angle to rotate by.
   * @return The rotation matrix.
   */
  public static LinearMatrix3 rotateZMatrix(double theta) {
    return new LinearMatrix3(
        Math.cos(theta), -Math.sin(theta), 0.0,
        Math.sin(theta), Math.cos(theta), 0.0,
        0.0, 0.0, 1.0
    );
  }

  /**
   * Returns a matrix for rotating about an arbitrary axis.
   * @param axis The axis to rotate around.
   * @param theta The angle to rotate by.
   * @return The rotation matrix.
   */
  public static LinearMatrix3 rotateMatrix(Vector3 axis, double theta) {
    // TODO implement this method.
    throw new UnsupportedOperationException("not yet implemented");
  }

}
