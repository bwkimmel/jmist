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

import ca.eandb.util.UnimplementedException;

/**
 * A 3x3 matrix of the form
 * <table>
 * 		<tr><td>a</td><td>b</td><td>0</td></tr>
 * 		<tr><td>c</td><td>d</td><td>0</td></tr>
 * 		<tr><td>0</td><td>0</td><td>1</td></tr>
 * </table>
 * for applying two dimensional linear transformations.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class LinearMatrix2 implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = 3218722249297624032L;

	/**
	 * The identity matrix ({@code this * IDENTITY == this}).
	 */
	public static final LinearMatrix2 IDENTITY = new LinearMatrix2(
			1.0, 0.0,
			0.0, 1.0);

	/**
	 * The zero matrix ({@code this + IDENTITY == this}).
	 */
	public static final LinearMatrix2 ZERO = new LinearMatrix2(
			0.0, 0.0,
			0.0, 0.0);

	/* Matrix elements */
	private final double _00, _01,
						 _10, _11;

	/**
	 * Initializes the matrix from its elements.
	 * @param _00
	 * @param _01
	 * @param _10
	 * @param _11
	 */
	public LinearMatrix2(
			double _00, double _01,
			double _10, double _11
			) {
		this._00 = _00; this._01 = _01;
		this._10 = _10; this._11 = _11;
	}

	/**
	 * Computes the product of this matrix with another.
	 * @param other The matrix by which to multiply this matrix.
	 * @return The product of this matrix and other.
	 */
	public LinearMatrix2 times(LinearMatrix2 other) {
		return new LinearMatrix2(
				_00 * other._00 + _01 * other._10,
				_00 * other._01 + _01 * other._11,
				_10 * other._00 + _11 * other._10,
				_10 * other._01 + _11 * other._11
		);
	}

	/**
	 * Multiplies this matrix by the inverse of the specified matrix.
	 * @param other The matrix by which to divide this matrix.
	 * @return The product of this matrix and the inverse of other.
	 * @see #inverse()
	 */
	public LinearMatrix2 divide(LinearMatrix2 other) {
		return this.times(other.inverse());
	}

	/**
	 * Computes the inverse of this matrix.
	 * The product of {@code this} and {@code this.inverse()} will be
	 * the identity matrix.
	 * @return The inverse of this matrix.
	 */
	public LinearMatrix2 inverse() {
		double det = determinant();

		return new LinearMatrix2(
				 _11 / det, -_01 / det,
				-_10 / det,  _00 / det
		);
	}

	/**
	 * Computes the transpose of this matrix.
	 * @return The transpose of this matrix.
	 */
	public LinearMatrix2 transposed() {
		return new LinearMatrix2(
				_00, _10,
				_01, _11
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
		return _00 + _11;
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
		double c1 = -trace();
		double c2 = 1.0;
	
		return new Polynomial(c0, c1, c2);		
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
	 * array of two (2) elements.
	 * @return An array containing the <code>Complex</code> eigenvalues of
	 * 		this matrix.
	 * @see ca.eandb.jmist.math.Complex
	 */
	public Complex[] complexEigenvalues() {
		return characteristic().complexRoots();
	}

	/**
	 * Gets an element of the matrix.
	 * @param row The row containing the element to get (0 <= row < 2).
	 * @param col The column containing the element to get (0 <= col < 2).
	 * @return The value of the element at the specified position.
	 */
	public double at(int row, int col) {
		switch (row) {
		case 0:
			switch (col) {
			case 0: return _00;
			case 1: return _01;
			}
		case 1:
			switch (col) {
			case 0: return _10;
			case 1: return _11;
			}
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Transforms the specified <code>HPoint2</code> according to the
	 * transformation represented by this <code>LinearMatrix2</code>.
	 * @param p The <code>HPoint2</code> to transform.
	 * @return The transformed <code>HPoint2</code>.
	 */
	public HPoint2 times(HPoint2 p) {
		return p.isPoint() ? times(p.toVector2()) : times(p.toPoint2());
	}

	/**
	 * Transforms the specified <code>Vector2</code> according to the
	 * transformation representing by this <code>LinearMatrix2</code>.
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
	 * transformation representing by this <code>LinearMatrix2</code>.
	 * @param p The <code>Point2</code> to transform.
	 * @return The transformed <code>Point2</code>.
	 */
	public Point2 times(Point2 p) {
		return new Point2(
				_00 * p.x() + _01 * p.y(),
				_10 * p.x() + _11 * p.y()
		);
	}

	/**
	 * Returns a scaling matrix.
	 * @param c The factor by which to scale.
	 * @return A scaling matrix.
	 */
	public static LinearMatrix2 scaleMatrix(double c) {
		return new LinearMatrix2(
				c, 0.0,
				0.0, c
		);
	}

	/**
	 * Returns a matrix for scaling by independent factors along
	 * each axis.
	 * @param cx The factor by which to scale along the x-axis.
	 * @param cy The factor by which to scale along the y-axis.
	 * @return The stretch matrix.
	 */
	public static LinearMatrix2 stretchMatrix(double cx, double cy) {
		return new LinearMatrix2(
				cx, 0.0,
				0.0, cy
		);
	}

	/**
	 * Returns a matrix for stretching along the x-axis.
	 * @param cx The factor by which to scale along the x-axis.
	 * @return The stretch matrix.
	 */
	public static LinearMatrix2 stretchXMatrix(double cx) {
		return new LinearMatrix2(
				cx , 0.0,
				0.0, 1.0
		);
	}

	/**
	 * Returns a matrix for stretching along the y-axis.
	 * @param cy The factor by which to scale along the y-axis.
	 * @return The stretch matrix.
	 */
	public static LinearMatrix2 stretchYMatrix(double cy) {
		return new LinearMatrix2(
				1.0, 0.0,
				0.0, cy 
		);
	}

	/**
	 * Returns a matrix for stretching along an arbitrary axis.
	 * @param axis The axis to stretch along.
	 * @param c The factor to stretch by.
	 * @return The stretch matrix.
	 */
	public static LinearMatrix2 stretchMatrix(Vector2 axis, double c) {
		// TODO implement this method.
		throw new UnimplementedException();
	}

	/**
	 * Returns a matrix for rotating.
	 * @param theta The angle to rotate by.
	 * @return The rotation matrix.
	 */
	public static LinearMatrix2 rotateMatrix(double theta) {
		return new LinearMatrix2(
				Math.cos(theta), -Math.sin(theta),
				Math.sin(theta), Math.cos(theta)
		);
	}

}
