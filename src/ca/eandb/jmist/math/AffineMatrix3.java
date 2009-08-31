/**
 *
 */
package ca.eandb.jmist.math;

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
 * @author Brad Kimmel
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
		this._00 = _00; this._01 = _01; this._02 = _02; this._03 = _03;
		this._10 = _10; this._11 = _11; this._12 = _12; this._13 = _13;
		this._20 = _20; this._21 = _21; this._22 = _22; this._23 = _23;
	}

	/**
	 * Initializes an affine matrix with a linear transformation.
	 * @param T The matrix representing the linear transformation.
	 */
	public AffineMatrix3(LinearMatrix3 T) {
		_00 = T.at(0, 0); _01 = T.at(0, 1); _02 = T.at(0, 2); _03 = 0.0;
		_10 = T.at(1, 0); _11 = T.at(1, 1); _12 = T.at(1, 2); _13 = 0.0;
		_20 = T.at(2, 0); _21 = T.at(2, 1); _22 = T.at(2, 2); _23 = 0.0;
	}

	/**
	 * Computes the product of this matrix with another.
	 * @param other The matrix by which to multiply this matrix.
	 * @return The product of this matrix and other.
	 */
	public AffineMatrix3 times(AffineMatrix3 other) {
		return new AffineMatrix3(
				_00 * other._00 + _01 * other._10 + _02 * other._20,
				_00 * other._01 + _01 * other._11 + _02 * other._21,
				_00 * other._02 + _01 * other._12 + _02 * other._22,
				_00 * other._03 + _01 * other._13 + _02 * other._23 + _03,
				_10 * other._00 + _11 * other._10 + _12 * other._20,
				_10 * other._01 + _11 * other._11 + _12 * other._21,
				_10 * other._02 + _11 * other._12 + _12 * other._22,
				_10 * other._03 + _11 * other._13 + _12 * other._23 + _13,
				_20 * other._00 + _21 * other._10 + _22 * other._20,
				_20 * other._01 + _21 * other._11 + _22 * other._21,
				_20 * other._02 + _21 * other._12 + _22 * other._22,
				_20 * other._03 + _21 * other._13 + _22 * other._23 + _23
		);
	}

	public AffineMatrix3 times(LinearMatrix3 other) {
		return new AffineMatrix3(
				_00 * other.at(0, 0) + _01 * other.at(1, 0) + _02 * other.at(2, 0),
				_00 * other.at(0, 1) + _01 * other.at(1, 1) + _02 * other.at(2, 1),
				_00 * other.at(0, 2) + _01 * other.at(1, 2) + _02 * other.at(2, 2),
				_03,
				_10 * other.at(0, 0) + _11 * other.at(1, 0) + _12 * other.at(2, 0),
				_10 * other.at(0, 1) + _11 * other.at(1, 1) + _12 * other.at(2, 1),
				_10 * other.at(0, 2) + _11 * other.at(1, 2) + _12 * other.at(2, 2),
				_13,
				_20 * other.at(0, 0) + _21 * other.at(1, 0) + _22 * other.at(2, 0),
				_20 * other.at(0, 1) + _21 * other.at(1, 1) + _22 * other.at(2, 1),
				_20 * other.at(0, 2) + _21 * other.at(1, 2) + _22 * other.at(2, 2),
				_23
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
								(_11 * _22 - _12 * _21) / det, (_02 * _21 - _01 * _22) / det, (_01 * _12 - _02 * _11) / det,
								(_01 * _13 * _22 + _02 * _11 * _23 + _03 * _12 * _21 - _01 * _12 * _23 - _02 * _13 * _21 - _03 * _11 * _22) / det,
								(_12 * _20 - _10 * _22) / det, (_00 * _22 - _02 * _20) / det, (_02 * _10 - _00 * _12) / det,
								(_00 * _12 * _23 + _02 * _13 * _20 + _03 * _10 * _22 - _00 * _13 * _22 - _02 * _10 * _23 - _03 * _12 * _20) / det,
								(_10 * _21 - _11 * _20) / det, (_01 * _20 - _00 * _21) / det, (_00 * _11 - _01 * _10) / det,
								(_00 * _13 * _21 + _01 * _10 * _23 + _03 * _11 * _20 - _00 * _11 * _23 - _01 * _13 * _20 - _03 * _10 * _21) / det
						);

		return inv;

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
	 * Gets an element of the matrix.
	 * @param row The row containing the element to get (0 <= row < 3).
	 * @param col The column containing the element to get (0 <= col < 4).
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
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Transforms the specified <code>HPoint3</code> according to the
	 * transformation represented by this <code>AffineMatrix3</code>.
	 * @param p The <code>HPoint3</code> to transform.
	 * @return The transformed <code>HPoint3</code>.
	 */
	public HPoint3 times(HPoint3 p) {
		return p.isPoint() ? times(p.toPoint3()) : times(p.toVector3());
	}

	/**
	 * Transforms the specified <code>Vector3</code> according to the
	 * transformation representing by this <code>AffineMatrix3</code>.
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
	 * transformation representing by this <code>AffineMatrix3</code>.
	 * @param p The <code>Point3</code> to transform.
	 * @return The transformed <code>Point3</code>.
	 */
	public Point3 times(Point3 p) {
		return new Point3(
				_00 * p.x() + _01 * p.y() + _02 * p.z() + _03,
				_10 * p.x() + _11 * p.y() + _12 * p.z() + _13,
				_20 * p.x() + _21 * p.y() + _22 * p.z() + _23
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
	 * Creates an <code>AffineMatrix3</code> with the given columns.
	 * @param u The <code>Vector3</code> with the elements for the first
	 * 		column.
	 * @param v The <code>Vector3</code> with the elements for the second
	 * 		column.
	 * @param w The <code>Vector3</code> with the elements for the third
	 * 		column.
	 * @return The required <code>AffineMatrix3</code>.
	 */
	public static AffineMatrix3 fromColumns(Vector3 u, Vector3 v, Vector3 w) {
		return fromColumns(u, v, w, Point3.ORIGIN);
	}


	/**
	 * Creates an <code>AffineMatrix3</code> with the given columns.
	 * @param u The <code>Vector3</code> with the elements for the first
	 * 		column.
	 * @param v The <code>Vector3</code> with the elements for the second
	 * 		column.
	 * @param w The <code>Vector3</code> with the elements for the third
	 * 		column.
	 * @param p The <code>Point3</code> with the elements for the fourth
	 * 		column.
	 * @return The required <code>AffineMatrix3</code>.
	 */
	public static AffineMatrix3 fromColumns(Vector3 u, Vector3 v, Vector3 w, Point3 p) {
		return new AffineMatrix3(
				u.x(), v.x(), w.x(), p.x(),
				u.y(), v.y(), w.y(), p.y(),
				u.z(), v.z(), w.z(), p.z());
	}

	/**
	 * Creates an <code>AffineMatrix3</code> that transforms the standard view
	 * coordinate system to consist of the eye at the given coordinates looking
	 * in the specified direction.  For the standard view coordinate system,
	 * the eye is at the origin looking along the negative z-axis.  The
	 * positive y-axis points up and the positive x-axis points to the right.
	 * @param eye The <code>Point3</code> indicating the transformed eye point.
	 * @param direction The <code>Vector3</code> indicating the direction to
	 * 		look.
	 * @param up The <code>Vector3</code> indicating the direction that is up.
	 * 		This vector will be projected onto the plane perpendicular to
	 * 		<code>direction</code>.  This vector must not be parallel to
	 * 		<code>direction</code>.
	 * @return The <code>AffineMatrix3</code> representing the transformation
	 * 		from the standard view coordinate system to the specified system.
	 */
	public static AffineMatrix3 lookMatrix(Point3 eye, Vector3 direction, Vector3 up) {
		Vector3 z = direction.unit().opposite();
		Vector3 x = up.cross(z).unit();
		Vector3 y = z.cross(x);
		Vector3	e = eye.vectorFromOrigin();

		return new AffineMatrix3(
				x.x(), x.y(), x.z(), -x.dot(e),
				y.x(), y.y(), y.z(), -y.dot(e),
				z.x(), z.y(), z.z(), -z.dot(e)).inverse();
	}

	/**
	 * Creates an <code>AffineMatrix3</code> that transforms the standard view
	 * coordinate system to consist of the eye at the given coordinates looking
	 * at the specified point.  For the standard view coordinate system, the
	 * eye is at the origin looking along the negative z-axis.  The positive
	 * y-axis points up and the positive x-axis points to the right.
	 *
	 * This is equivalent to
	 * <code>AffineMatrix3.lookMatrix(eye, eye.vectorTo(target), up)</code>.
	 * @param eye The <code>Point3</code> indicating the transformed eye point.
	 * @param target The <code>Point3</code> indicating location to look at.
	 * @param up The <code>Vector3</code> indicating the direction that is up.
	 * 		This vector will be projected onto the plane perpendicular to
	 * 		the vector from <code>eye</code> to <code>target</code>.  This
	 * 		vector (<code>up</code>) must not be parallel to the vector from
	 * 		<code>eye</code> to <code>target</code>.
	 * @return The <code>AffineMatrix3</code> representing the transformation
	 * 		from the standard view coordinate system to the specified system.
	 * @see #lookMatrix(Point3, Vector3, Vector3)
	 * @see Point3#vectorTo(Point3)
	 */
	public static AffineMatrix3 lookAtMatrix(Point3 eye, Point3 target, Vector3 up) {
		return lookMatrix(eye, eye.vectorTo(target), up);
	}

	/**
	 * The identity matrix ({@code this * IDENTITY == this}).
	 */
	public static final AffineMatrix3 IDENTITY = new AffineMatrix3(
			1.0, 0.0, 0.0, 0.0,
			0.0, 1.0, 0.0, 0.0,
			0.0, 0.0, 1.0, 0.0);

	/* Matrix elements */
	private final double _00, _01, _02, _03,
						 _10, _11, _12, _13,
						 _20, _21, _22, _23;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -8679442263182238771L;

}
