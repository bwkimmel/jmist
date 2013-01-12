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
 * A 3x3 matrix of the form
 * <table>
 * 		<tr><td>a</td><td>b</td><td>x</td></tr>
 * 		<tr><td>c</td><td>d</td><td>y</td></tr>
 * 		<tr><td>0</td><td>0</td><td>1</td></tr>
 * </table>
 * for applying two dimensional affine transformations.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class AffineMatrix2 implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = 2792329658585793940L;

	/**
	 * The identity matrix ({@code this * IDENTITY == this}).
	 */
	public static final AffineMatrix2 IDENTITY = new AffineMatrix2(
			1.0, 0.0, 0.0,
			0.0, 1.0, 0.0);

	/* Matrix elements */
	private final double _00, _01, _02,
						 _10, _11, _12;

	/**
	 * Initializes the matrix from its elements.
	 * @param _00
	 * @param _01
	 * @param _02
	 * @param _10
	 * @param _11
	 * @param _12
	 */
	public AffineMatrix2(
			double _00, double _01, double _02,
			double _10, double _11, double _12
			) {
		this._00 = _00; this._01 = _01; this._02 = _02;
		this._10 = _10; this._11 = _11; this._12 = _12;
	}

	/**
	 * Initializes an affine matrix with a linear transformation.
	 * @param T The matrix representing the linear transformation.
	 */
	public AffineMatrix2(LinearMatrix2 T) {
		_00 = T.at(0, 0); _01 = T.at(0, 1); _02 = 0.0;
		_10 = T.at(1, 0); _11 = T.at(1, 1); _12 = 0.0;
	}

	/**
	 * Computes the product of this matrix with another.
	 * @param other The matrix by which to multiply this matrix.
	 * @return The product of this matrix and other.
	 */
	public AffineMatrix2 times(AffineMatrix2 other) {
		return new AffineMatrix2(
				_00 * other._00 + _01 * other._10,
				_00 * other._01 + _01 * other._11,
				_00 * other._02 + _01 * other._12 + _02,
				_10 * other._00 + _11 * other._10,
				_10 * other._01 + _11 * other._11,
				_10 * other._02 + _11 * other._12 + _12
		);
	}

	public AffineMatrix2 times(LinearMatrix2 other) {
		return new AffineMatrix2(
				_00 * other.at(0, 0) + _01 * other.at(1, 0),
				_00 * other.at(0, 1) + _01 * other.at(1, 1),
				_02,
				_10 * other.at(0, 0) + _11 * other.at(1, 0),
				_10 * other.at(0, 1) + _11 * other.at(1, 1),
				_12
		);
	}

	/**
	 * Multiplies this matrix by the inverse of the specified matrix.
	 * @param other The matrix by which to divide this matrix.
	 * @return The product of this matrix and the inverse of other.
	 * @see inverse
	 */
	public AffineMatrix2 divide(AffineMatrix2 other) {
		return this.times(other.inverse());
	}

	/**
	 * Multiplies this matrix by the inverse of the specified matrix.
	 * @param other The matrix by which to divide this matrix.
	 * @return The product of this matrix and the inverse of other.
	 * @see inverse
	 */
	public AffineMatrix2 divide(LinearMatrix2 other) {
		return this.times(other.inverse());
	}

	/**
	 * Computes the inverse of this matrix.
	 * The product of {@code this} and {@code this.inverse()} will be
	 * the identity matrix.
	 * @return The inverse of this matrix.
	 */
	public AffineMatrix2 inverse() {
		double det = determinant();

		return new AffineMatrix2(
				 _11 / det, -_01 / det, (_01 * _12 - _02 * _11) / det,
				-_10 / det,  _00 / det, (_02 * _10 - _00 * _12) / det
		);
	}

	/**
	 * Finds the determinant of this matrix (i.e., the volume of the
	 * unit cube after the transformation represented by this matrix
	 * is applied).
	 * @return The determinant of this matrix.
	 */
	public double determinant() {
		return _00 * _11 - _01 * _10;
	}
	
	/**
	 * Gets the trace of this matrix (i.e., the sum of the diagonal elements).
	 * @return The trace of this matrix.
	 */
	public double trace() {
		return _00 + _11 + 1.0;
	}

	/**
	 * Gets an element of the matrix.
	 * @param row The row containing the element to get (0 <= row < 2).
	 * @param col The column containing the element to get (0 <= col < 3).
	 * @return The value of the element at the specified position.
	 * @see getElement
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
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Transforms the specified <code>HPoint2</code> according to the
	 * transformation represented by this <code>AffineMatrix2</code>.
	 * @param p The <code>HPoint2</code> to transform.
	 * @return The transformed <code>HPoint2</code>.
	 */
	public HPoint2 times(HPoint2 p) {
		return p.isPoint() ? times(p.toPoint2()) : times(p.toVector2());
	}

	/**
	 * Transforms the specified <code>Vector2</code> according to the
	 * transformation representing by this <code>AffineMatrix2</code>.
	 * @param v The <code>Vector2</code> to transform.
	 * @return The transformed <code>Vector2</code>.
	 */
	public Vector2 times(Vector2 v) {
		return new Vector2(
				_00 * v.x() + _01 * v.y(),
				_10 * v.x() + _11 * v.y()
		);
	}

	/**
	 * Transforms the specified <code>Point2</code> according to the
	 * transformation representing by this <code>AffineMatrix2</code>.
	 * @param p The <code>Point2</code> to transform.
	 * @return The transformed <code>Point2</code>.
	 */
	public Point2 times(Point2 p) {
		return new Point2(
				_00 * p.x() + _01 * p.y() + _02,
				_10 * p.x() + _11 * p.y() + _12
		);
	}

	/**
	 * Creates a new <code>AffineMatrix2</code> representing a translation.
	 * @param v The <code>Vector2</code> to translate along.
	 * @return A translation matrix.
	 */
	public static AffineMatrix2 translateMatrix(Vector2 v) {
		return new AffineMatrix2(
				1.0, 0.0, v.x(),
				0.0, 1.0, v.y()
		);
	}

	/**
	 * Creates an <code>AffineMatrix2</code> with the given columns.
	 * @param u The <code>Vector2</code> with the elements for the first
	 * 		column.
	 * @param v The <code>Vector2</code> with the elements for the second
	 * 		column.
	 * @return The required <code>AffineMatrix2</code>.
	 */
	public static AffineMatrix2 fromColumns(Vector2 u, Vector2 v) {
		return fromColumns(u, v, Point2.ORIGIN);
	}


	/**
	 * Creates an <code>AffineMatrix2</code> with the given columns.
	 * @param u The <code>Vector2</code> with the elements for the first
	 * 		column.
	 * @param v The <code>Vector2</code> with the elements for the second
	 * 		column.
	 * @param p The <code>Point2</code> with the elements for the third
	 * 		column.
	 * @return The required <code>AffineMatrix2</code>.
	 */
	public static AffineMatrix2 fromColumns(Vector2 u, Vector2 v, Point2 p) {
		return new AffineMatrix2(
				u.x(), v.x(), p.x(),
				u.y(), v.y(), p.y());
	}

}
