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
 * A 4x4 matrix.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Matrix4 implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5396312497182111362L;

  /**
   * Initializes the matrix from its elements.
   * @param _00 The element in row 0, column 0.
   * @param _01 The element in row 0, column 1.
   * @param _02 The element in row 0, column 2.
   * @param _03 The element in row 0, column 3.
   * @param _10 The element in row 1, column 0.
   * @param _11 The element in row 1, column 1.
   * @param _12 The element in row 1, column 2.
   * @param _13 The element in row 1, column 3.
   * @param _20 The element in row 2, column 0.
   * @param _21 The element in row 2, column 1.
   * @param _22 The element in row 2, column 2.
   * @param _23 The element in row 2, column 3.
   * @param _30 The element in row 3, column 0.
   * @param _31 The element in row 3, column 1.
   * @param _32 The element in row 3, column 2.
   * @param _33 The element in row 3, column 3.
   */
  public Matrix4(
      double _00, double _01, double _02, double _03,
      double _10, double _11, double _12, double _13,
      double _20, double _21, double _22, double _23,
      double _30, double _31, double _32, double _33
      ) {
    this._00 = _00; this._01 = _01; this._02 = _02; this._03 = _03;
    this._10 = _10; this._11 = _11; this._12 = _12; this._13 = _13;
    this._20 = _20; this._21 = _21; this._22 = _22; this._23 = _23;
    this._30 = _30; this._31 = _31; this._32 = _32; this._33 = _33;
  }

  /**
   * Computes the product of this matrix with another.
   * @param other The matrix by which to multiply this matrix.
   * @return The product of this matrix and other.
   */
  public Matrix4 times(Matrix4 other) {
    return new Matrix4(
        _00 * other._00 + _01 * other._10 + _02 * other._20 + _03 * other._30,
        _00 * other._01 + _01 * other._11 + _02 * other._21 + _03 * other._31,
        _00 * other._02 + _01 * other._12 + _02 * other._22 + _03 * other._32,
        _00 * other._03 + _01 * other._13 + _02 * other._23 + _03 * other._33,
        _10 * other._00 + _11 * other._10 + _12 * other._20 + _13 * other._30,
        _10 * other._01 + _11 * other._11 + _12 * other._21 + _13 * other._31,
        _10 * other._02 + _11 * other._12 + _12 * other._22 + _13 * other._32,
        _10 * other._03 + _11 * other._13 + _12 * other._23 + _13 * other._33,
        _20 * other._00 + _21 * other._10 + _22 * other._20 + _23 * other._30,
        _20 * other._01 + _21 * other._11 + _22 * other._21 + _23 * other._31,
        _20 * other._02 + _21 * other._12 + _22 * other._22 + _23 * other._32,
        _20 * other._03 + _21 * other._13 + _22 * other._23 + _23 * other._33,
        _30 * other._00 + _31 * other._10 + _32 * other._20 + _33 * other._30,
        _30 * other._01 + _31 * other._11 + _32 * other._21 + _33 * other._31,
        _30 * other._02 + _31 * other._12 + _32 * other._22 + _33 * other._32,
        _30 * other._03 + _31 * other._13 + _32 * other._23 + _33 * other._33
    );
  }

  /**
   * Multiplies this matrix by the inverse of the specified matrix.
   * @param other The matrix by which to divide this matrix.
   * @return The product of this matrix and the inverse of other.
   * @see #inverse()
   */
  public Matrix4 divide(Matrix4 other) {
    return this.times(other.inverse());
  }

  /**
   * Computes the inverse of this matrix.
   * The product of {@code this} and {@code this.inverse()} will be
   * the identity matrix.
   * @return The inverse of this matrix.
   */
  public Matrix4 inverse() {
    double det = determinant();

    return new Matrix4(
        (_11 * _22 * _33 + _12 * _23 * _31 + _13 * _21 * _32 - _11 * _23 * _32 - _12 * _21 * _33 - _13 * _22 * _31) / det,
        (_01 * _23 * _32 + _02 * _21 * _33 + _03 * _22 * _31 - _01 * _22 * _33 - _02 * _23 * _31 - _03 * _21 * _32) / det,
        (_01 * _12 * _33 + _02 * _13 * _31 + _03 * _11 * _32 - _01 * _13 * _32 - _02 * _11 * _33 - _03 * _12 * _31) / det,
        (_01 * _13 * _22 + _02 * _11 * _23 + _03 * _12 * _21 - _01 * _12 * _23 - _02 * _13 * _21 - _03 * _11 * _22) / det,

        (_10 * _23 * _32 + _12 * _20 * _33 + _13 * _22 * _30 - _10 * _22 * _33 - _12 * _23 * _30 - _13 * _20 * _32) / det,
        (_00 * _22 * _33 + _02 * _23 * _30 + _03 * _20 * _32 - _00 * _23 * _32 - _02 * _20 * _33 - _03 * _22 * _30) / det,
        (_00 * _13 * _32 + _02 * _10 * _33 + _03 * _12 * _30 - _00 * _12 * _33 - _02 * _13 * _30 - _03 * _10 * _32) / det,
        (_00 * _12 * _23 + _02 * _13 * _20 + _03 * _10 * _22 - _00 * _13 * _22 - _02 * _10 * _23 - _03 * _12 * _20) / det,

        (_10 * _21 * _33 + _11 * _23 * _30 + _13 * _20 * _31 - _10 * _23 * _31 - _11 * _20 * _33 - _13 * _21 * _30) / det,
        (_00 * _23 * _31 + _01 * _20 * _33 + _03 * _21 * _30 - _00 * _21 * _33 - _01 * _23 * _30 - _03 * _20 * _31) / det,
        (_00 * _11 * _33 + _01 * _13 * _30 + _03 * _10 * _31 - _00 * _13 * _31 - _01 * _10 * _33 - _03 * _11 * _30) / det,
        (_00 * _13 * _21 + _01 * _10 * _23 + _03 * _11 * _20 - _00 * _11 * _23 - _01 * _13 * _20 - _03 * _10 * _21) / det,

        (_10 * _22 * _31 + _11 * _20 * _32 + _12 * _21 * _30 - _10 * _21 * _32 - _11 * _22 * _30 - _12 * _20 * _31) / det,
        (_00 * _21 * _32 + _01 * _22 * _30 + _02 * _20 * _31 - _00 * _22 * _31 - _01 * _20 * _32 - _02 * _21 * _30) / det,
        (_00 * _12 * _31 + _01 * _10 * _32 + _02 * _11 * _30 - _00 * _11 * _32 - _01 * _12 * _30 - _02 * _10 * _31) / det,
        (_00 * _11 * _22 + _01 * _12 * _20 + _02 * _10 * _21 - _00 * _12 * _21 - _01 * _10 * _22 - _02 * _11 * _20) / det
        );
  }

  /**
   * Computes the transpose of this matrix.
   * @return The transpose of this matrix.
   */
  public Matrix4 transposed() {
    return new Matrix4(
        _00, _10, _20, _30,
        _01, _11, _21, _31,
        _02, _12, _22, _32,
        _03, _13, _23, _33
    );
  }

  /**
   * Finds the determinant of this matrix (i.e., the volume of the
   * unit cube after the transformation represented by this matrix
   * is applied).
   * @return The determinant of this matrix.
   */
  public double determinant() {
    return _00 * (_11 * (_22 * _33 - _23 * _32) +
              _12 * (_23 * _31 - _21 * _33) +
              _13 * (_21 * _32 - _22 * _31)) -
         _01 * (_12 * (_23 * _30 - _20 * _33) +
            _13 * (_20 * _32 - _22 * _30) +
            _10 * (_22 * _33 - _23 * _32)) +
         _02 * (_13 * (_20 * _31 - _21 * _30) +
            _10 * (_21 * _33 - _23 * _31) +
            _11 * (_23 * _30 - _20 * _33)) -
         _03 * (_10 * (_21 * _32 - _22 * _31) +
            _11 * (_22 * _30 - _20 * _32) +
            _12 * (_20 * _31 - _21 * _30));
  }

  /**
   * Gets the trace of this matrix (i.e., the sum of the diagonal elements).
   * @return The trace of this matrix.
   */
  public double trace() {
    return _00 + _11 + _22 + _33;
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
    double c1 = -_00*_11*_22-_00*_11*_33-_00*_22*_33+_00*_23*_32+_00*_21*_12+_00*_31*_13-_11*_22*_33+_11*_23*_32+_21*_12*_33-_21*_13*_32-_31*_12*_23+_31*_13*_22+_10*_01*_22+_10*_01*_33-_10*_21*_02-_10*_31*_03-_20*_01*_12+_20*_11*_02+_20*_02*_33-_20*_03*_32-_30*_01*_13+_30*_11*_03-_30*_02*_23+_30*_03*_22;
    double c2 = _00*_11-_20*_02-_10*_01-_30*_03+_11*_22+_11*_33+_22*_33-_23*_32-_21*_12-_31*_13+_00*_22+_00*_33;
    double c3 = -trace();
    double c4 = 1.0;

    return new Polynomial(c0, c1, c2, c3, c4);
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
   * array of four (4) elements.
   * @return An array containing the <code>Complex</code> eigenvalues of
   *     this matrix.
   * @see ca.eandb.jmist.math.Complex
   */
  public Complex[] complexEigenvalues() {
    return characteristic().complexRoots();
  }

  /**
   * Gets an element of the matrix.
   * @param row The row containing the element to get (0 &lt;= row &lt; 4).
   * @param col The column containing the element to get (0 &lt;= col &lt; 4).
   * @return The value of the element at the specified position.
   */
  public double at(int row, int col) {
    switch (row) {
    case 0:
      switch (col) {
      case 0: return _00;
      case 1: return _01;
      case 2: return _02;
      case 3: return _03;
      }
    case 1:
      switch (col) {
      case 0: return _10;
      case 1: return _11;
      case 2: return _12;
      case 3: return _13;
      }
    case 2:
      switch (col) {
      case 0: return _20;
      case 1: return _21;
      case 2: return _22;
      case 3: return _23;
      }
    case 3:
      switch (col) {
      case 0: return _30;
      case 1: return _31;
      case 2: return _32;
      case 3: return _33;
      }
    }
    throw new IndexOutOfBoundsException();
  }

  /**
   * Transforms the specified <code>Vector4</code> according to the
   * transformation representing by this <code>Matrix4</code>.
   * @param v The <code>Vector4</code> to transform.
   * @return The transformed <code>Vector4</code>.
   */
  public Vector4 times(Vector4 v) {
    return new Vector4(
        _00 * v.x() + _01 * v.y() + _02 * v.z() + _03 * v.w(),
        _10 * v.x() + _11 * v.y() + _12 * v.z() + _13 * v.w(),
        _20 * v.x() + _21 * v.y() + _22 * v.z() + _23 * v.w(),
        _30 * v.x() + _31 * v.y() + _32 * v.z() + _33 * v.w()
    );
  }

  /**
   * Computes the sum of this matrix with another.
   * @param m The <code>Matrix4</code> to add to this matrix.
   * @return The sum of this matrix and <code>m</code>.
   */
  public Matrix4 plus(Matrix4 m) {
    return new Matrix4(
        _00 + m._00, _01 + m._01, _02 + m._02, _03 + m._03,
        _10 + m._10, _11 + m._11, _12 + m._12, _13 + m._13,
        _20 + m._20, _21 + m._21, _22 + m._22, _23 + m._23,
        _30 + m._30, _31 + m._31, _32 + m._32, _33 + m._33);
  }

  /**
   * Computes the difference of this matrix with another.
   * @param m The <code>Matrix4</code> to subtract from this matrix.
   * @return The difference of this matrix and <code>m</code>.
   */
  public Matrix4 minus(Matrix4 m) {
    return new Matrix4(
        _00 - m._00, _01 - m._01, _02 - m._02, _03 - m._03,
        _10 - m._10, _11 - m._11, _12 - m._12, _13 - m._13,
        _20 - m._20, _21 - m._21, _22 - m._22, _23 - m._23,
        _30 - m._30, _31 - m._31, _32 - m._32, _33 - m._33);
  }

  /**
   * Gets the Hermitian part of this <code>Matrix4</code> (i.e.,
   * <code>0.5 * (A + A<sup>t</sup>)</code>, where <code>A</code> is this
   * <code>Matrix4</code>.
   * @return The Hermitian part of this <code>Matrix4</code>.
   */
  public Matrix4 hermitian() {
    double h01 = 0.5 * (_01 + _10);
    double h02 = 0.5 * (_02 + _20);
    double h03 = 0.5 * (_03 + _30);
    double h12 = 0.5 * (_12 + _21);
    double h13 = 0.5 * (_13 + _31);
    double h23 = 0.5 * (_23 + _32);

    return new Matrix4(
        _00, h01, h02, h03,
        h01, _11, h12, h13,
        h02, h12, _22, h23,
        h03, h13, h23, _33);
  }

  /**
   * Gets the Antihermitian part of this <code>Matrix4</code> (i.e.,
   * <code>0.5 * (A - A<sup>t</sup>)</code>, where <code>A</code> is this
   * <code>Matrix4</code>.
   * @return The Antihermitian part of this <code>Matrix4</code>.
   */
  public Matrix4 antihermitian() {
    double h01 = 0.5 * (_01 - _10);
    double h02 = 0.5 * (_02 - _20);
    double h03 = 0.5 * (_03 - _30);
    double h12 = 0.5 * (_12 - _21);
    double h13 = 0.5 * (_13 - _31);
    double h23 = 0.5 * (_23 - _32);

    return new Matrix4(
         0.0,  h01,  h02,  h03,
        -h01,  0.0,  h12,  h13,
        -h02, -h12,  0.0,  h23,
        -h03, -h13, -h23,  0.0);
  }

  /**
   * Computes the general dot product of <code>u</code> and <code>v</code>
   * defined by this <code>Matrix4</code> (i.e, <code>u<sup>t</sup>Qv</code>,
   * where <code>Q</code> is this <code>Matrix4</code>).
   * @param u The first <code>Vector4</code>.
   * @param v The second <code>Vector4</code>.
   * @return The general dot product of <code>u</code> and <code>v</code>
   *     defined by this <code>Matrix4</code>.
   */
  public double dot(Vector4 u, Vector4 v) {
    return
      u.x() * (_00 * v.x() + _01 * v.y() + _02 * v.z() + _03 * v.w()) +
      u.y() * (_10 * v.x() + _11 * v.y() + _12 * v.z() + _13 * v.w()) +
      u.z() * (_20 * v.x() + _21 * v.y() + _22 * v.z() + _23 * v.w()) +
      u.w() * (_30 * v.x() + _31 * v.y() + _32 * v.z() + _33 * v.w());
  }

  /**
   * The identity matrix ({@code this * IDENTITY == this}).
   */
  public static final Matrix4 IDENTITY = new Matrix4(
      1.0, 0.0, 0.0, 0.0,
      0.0, 1.0, 0.0, 0.0,
      0.0, 0.0, 1.0, 0.0,
      0.0, 0.0, 0.0, 1.0);

  /**
   * The zero matrix ({@code this + IDENTITY == this}).
   */
  public static final Matrix4 ZERO = new Matrix4(
      0.0, 0.0, 0.0, 0.0,
      0.0, 0.0, 0.0, 0.0,
      0.0, 0.0, 0.0, 0.0,
      0.0, 0.0, 0.0, 0.0);

  /* Matrix elements */
  private final double _00, _01, _02, _03,
             _10, _11, _12, _13,
             _20, _21, _22, _23,
             _30, _31, _32, _33;

}
