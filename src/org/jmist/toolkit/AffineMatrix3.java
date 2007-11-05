/**
 *
 */
package org.jmist.toolkit;

import java.io.Serializable;

/**
 * A 4x4 matrix of the form
 * <table>
 * 		<tr><td>a</td><td>b</td><td>c</td><td>x</td></tr>
 * 		<tr><td>d</td><td>e</td><td>f</td><td>y</td></tr>
 * 		<tr><td>g</td><td>h</td><td>i</td><td>z</td></tr>
 * 		<tr><td>0</td><td>0</td><td>0</td><td>1</td></tr>
 * </table>
 * for applying three dimensional affine transformations.
 * This class is immutable.
 * @author brad
 */
public final class AffineMatrix3 implements Serializable {

	/**
	 * Initializes the matrix from its elements.
	 * @param _00
	 * @param _01
	 * @param _02
	 * @param _03
	 * @param _10
	 * @param _11
	 * @param _12
	 * @param _13
	 * @param _20
	 * @param _21
	 * @param _22
	 * @param _23
	 */
	public AffineMatrix3(
			double _00, double _01, double _02, double _03,
			double _10, double _11, double _12, double _13,
			double _20, double _21, double _22, double _23
			) {
		this.a[0][0] = _00; this.a[0][1] = _01; this.a[0][2] = _02; this.a[0][3] = _03;
		this.a[1][0] = _10; this.a[1][1] = _11; this.a[1][2] = _12; this.a[1][3] = _13;
		this.a[2][0] = _20; this.a[2][1] = _21; this.a[2][2] = _22; this.a[2][3] = _23;
	}

	/**
	 * Initializes an affine matrix with a linear transformation.
	 * @param T The matrix representing the linear transformation.
	 */
	public AffineMatrix3(LinearMatrix3 T) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				a[i][j] = T.at(i, j);
			}
		}

		a[0][3] = a[1][3] = a[2][3] = 0.0;
	}

	/**
	 * Computes the product of this matrix with another.
	 * @param other The matrix by which to multiply this matrix.
	 * @return The product of this matrix and other.
	 */
	public AffineMatrix3 times(AffineMatrix3 other) {
		return new AffineMatrix3(
				a[0][0] * other.a[0][0] + a[0][1] * other.a[1][0] + a[0][2] * other.a[2][0],
				a[0][0] * other.a[0][1] + a[0][1] * other.a[1][1] + a[0][2] * other.a[2][1],
				a[0][0] * other.a[0][2] + a[0][1] * other.a[1][2] + a[0][2] * other.a[2][2],
				a[0][0] * other.a[0][3] + a[0][1] * other.a[1][3] + a[0][2] * other.a[2][3] + a[0][3],
				a[1][0] * other.a[0][0] + a[1][1] * other.a[1][0] + a[1][2] * other.a[2][0],
				a[1][0] * other.a[0][1] + a[1][1] * other.a[1][1] + a[1][2] * other.a[2][1],
				a[1][0] * other.a[0][2] + a[1][1] * other.a[1][2] + a[1][2] * other.a[2][2],
				a[1][0] * other.a[0][3] + a[1][1] * other.a[1][3] + a[1][2] * other.a[2][3] + a[1][3],
				a[2][0] * other.a[0][0] + a[2][1] * other.a[1][0] + a[2][2] * other.a[2][0],
				a[2][0] * other.a[0][1] + a[2][1] * other.a[1][1] + a[2][2] * other.a[2][1],
				a[2][0] * other.a[0][2] + a[2][1] * other.a[1][2] + a[2][2] * other.a[2][2],
				a[2][0] * other.a[0][3] + a[2][1] * other.a[1][3] + a[2][2] * other.a[2][3] + a[2][3]
		);
	}

	public AffineMatrix3 times(LinearMatrix3 other) {
		return new AffineMatrix3(
				a[0][0] * other.at(0, 0) + a[0][1] * other.at(1, 0) + a[0][2] * other.at(2, 0),
				a[0][0] * other.at(0, 1) + a[0][1] * other.at(1, 1) + a[0][2] * other.at(2, 1),
				a[0][0] * other.at(0, 2) + a[0][1] * other.at(1, 2) + a[0][2] * other.at(2, 2),
				a[0][3],
				a[1][0] * other.at(0, 0) + a[1][1] * other.at(1, 0) + a[1][2] * other.at(2, 0),
				a[1][0] * other.at(0, 1) + a[1][1] * other.at(1, 1) + a[1][2] * other.at(2, 1),
				a[1][0] * other.at(0, 2) + a[1][1] * other.at(1, 2) + a[1][2] * other.at(2, 2),
				a[1][3],
				a[2][0] * other.at(0, 0) + a[2][1] * other.at(1, 0) + a[2][2] * other.at(2, 0),
				a[2][0] * other.at(0, 1) + a[2][1] * other.at(1, 1) + a[2][2] * other.at(2, 1),
				a[2][0] * other.at(0, 2) + a[2][1] * other.at(1, 2) + a[2][2] * other.at(2, 2),
				a[2][3]
		);
	}

	/**
	 * Multiplies this matrix by the inverse of the specified matrix.
	 * @param other The matrix by which to divide this matrix.
	 * @return The product of this matrix and the inverse of other.
	 * @see inverse
	 */
	public AffineMatrix3 divide(AffineMatrix3 other) {
		return this.times(other.inverse());
	}

	/**
	 * Multiplies this matrix by the inverse of the specified matrix.
	 * @param other The matrix by which to divide this matrix.
	 * @return The product of this matrix and the inverse of other.
	 * @see inverse
	 */
	public AffineMatrix3 divide(LinearMatrix3 other) {
		return this.times(other.inverse());
	}

	/**
	 * Computes the inverse of this matrix.
	 * The product of {@code this} and {@code this.inverse()} will be
	 * the identity matrix.
	 * @return The inverse of this matrix.
	 */
	public AffineMatrix3 inverse() {

		double			det = determinant();
		AffineMatrix3	inv = new AffineMatrix3(
								(a[1][1] * a[2][2] - a[1][2] * a[2][1]) / det, (a[0][1] * a[2][2] - a[0][2] * a[2][1]) / det, (a[0][1] * a[1][2] - a[0][2] * a[1][1]) / det, 0.0,
								(a[1][2] * a[2][0] - a[1][0] * a[2][2]) / det, (a[0][0] * a[2][2] - a[0][2] * a[2][0]) / det, (a[0][2] * a[1][0] - a[0][0] * a[1][2]) / det, 0.0,
								(a[1][0] * a[2][1] - a[1][1] * a[2][0]) / det, (a[0][1] * a[2][0] - a[0][0] * a[2][1]) / det, (a[0][0] * a[1][1] - a[0][1] * a[1][0]) / det, 0.0
						);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				inv.a[i][3] -= inv.a[i][j] * this.a[j][3];
			}
		}

		return inv;

	}

	/**
	 * Finds the determinant of this matrix (i.e., the volume of the
	 * unit cube after the transformation represented by this matrix
	 * is applied).
	 * @return The determinant of this matrix.
	 */
	public double determinant() {
		return a[0][0] * (a[1][1] * a[2][2] - a[1][2] * a[2][1]) +
		       a[0][1] * (a[1][2] * a[2][0] - a[1][0] * a[2][2]) +
		       a[0][2] * (a[1][0] * a[2][1] - a[1][1] * a[2][0]);
	}

	/**
	 * Gets an element of the matrix.
	 * @param row The row containing the element to get (0 <= row < 3).
	 * @param col The column containing the element to get (0 <= col < 4).
	 * @return The value of the element at the specified position.
	 * @see getElement
	 */
	public double at(int row, int col) {
		return a[row][col];
	}

	/**
	 * Transforms the specified <code>Vector3</code> according to the
	 * transformation representing by this <code>AffineMatrix3</code>.
	 * @param v The <code>Vector3</code> to transform.
	 * @return The transformed <code>Vector3</code>.
	 */
	public Vector3 times(Vector3 v) {
		return new Vector3(
				a[0][0] * v.x() + a[0][1] * v.y() + a[0][2] * v.z(),
				a[1][0] * v.x() + a[1][1] * v.y() + a[1][2] * v.z(),
				a[2][0] * v.x() + a[2][1] * v.y() + a[2][2] * v.z()
		);
	}

	/**
	 * Transforms the specified <code>Point3</code> according to the
	 * transformation representing by this <code>AffineMatrix3</code>.
	 * @param p The <code>Point3</code> to transform.
	 * @return The transformed <code>Point3</code>.
	 */
	public Point3 times(Point3 p) {
		return new Point3(
				a[0][0] * p.x() + a[0][1] * p.y() + a[0][2] * p.z() + a[0][3],
				a[1][0] * p.x() + a[1][1] * p.y() + a[1][2] * p.z() + a[1][3],
				a[2][0] * p.x() + a[2][1] * p.y() + a[2][2] * p.z() + a[2][3]
		);
	}

	/**
	 * Creates a new <code>AffineMatrix3</code> representing a translation.
	 * @param v The <code>Vector3</code> to translate along.
	 * @return A translation matrix.
	 */
	public static AffineMatrix3 translateMatrix(Vector3 v) {
		return new AffineMatrix3(
				1.0, 0.0, 0.0, v.x(),
				0.0, 1.0, 0.0, v.y(),
				0.0, 0.0, 1.0, v.z()
		);
	}

	/**
	 * The identity matrix ({@code this * IDENTITY == this}).
	 */
	public static final AffineMatrix3 IDENTITY = new AffineMatrix3(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

	/** The elements of the matrix. */
	private final double[][] a = new double[3][4];

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -8679442263182238771L;

}
