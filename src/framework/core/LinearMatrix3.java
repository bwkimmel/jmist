/**
 *
 */
package framework.core;

/**
 * A 3x3 matrix for applying three dimensional linear transformations.
 * @author brad
 */
public final class LinearMatrix3 {

	/**
	 * Default constructor.
	 */
	public LinearMatrix3() {
	}

	/**
	 * Initializes the matrix from its elements.
	 * @param _00
	 * @param _01
	 * @param _02
	 * @param _10
	 * @param _11
	 * @param _12
	 * @param _20
	 * @param _21
	 * @param _22
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
	 * Adds another matrix to this matrix.
	 * Equivalent to {@code this = this.plus(other);}
	 * @param other The matrix to add to this matrix.
	 * @see plus
	 */
	public void add(LinearMatrix3 other) {
		_00 += other._00; _01 += other._01; _02 += other._02;
		_10 += other._10; _11 += other._11; _12 += other._12;
		_20 += other._20; _21 += other._21; _22 += other._22;
	}

	/**
	 * Adds this matrix to another matrix.
	 * @param other The matrix to add to this matrix.
	 * @return The sum of this matrix and other.
	 */
	public LinearMatrix3 plus(LinearMatrix3 other) {
		return new LinearMatrix3(
				_00 + other._00, _01 + other._01, _02 + other._02,
				_10 + other._10, _11 + other._11, _12 + other._12,
				_20 + other._20, _21 + other._21, _22 + other._22
				);
	}

	/**
	 * Subtracts another matrix from this one.
	 * Equivalent to {@code this = this.minus(other);}
	 * @param other The matrix to subtract from this one.
	 * @see minus
	 */
	public void subtract(LinearMatrix3 other) {
		_00 -= other._00; _01 -= other._01; _02 -= other._02;
		_10 -= other._10; _11 -= other._11; _12 -= other._12;
		_20 -= other._20; _21 -= other._21; _22 -= other._22;
	}

	/**
	 * Subtracts another matrix from this one.
	 * @param other The matrix to subtract from this one.
	 * @return The difference between this matrix and other.
	 */
	public LinearMatrix3 minus(LinearMatrix3 other) {
		return new LinearMatrix3(
				_00 - other._00, _01 - other._01, _02 - other._02,
				_10 - other._10, _11 - other._11, _12 - other._12,
				_20 - other._20, _21 - other._21, _22 - other._22
				);
	}

	/**
	 * Multiplies this matrix by another.
	 * Equivalent to {@code this = this.times(other);}
	 * @param other The matrix by which to multiply this matrix.
	 * @see times
	 */
	public void multiply(LinearMatrix3 other) {
		copy(this.times(other));
	}

	/**
	 * Computes the product of this matrix with another.
	 * @param other The matrix by which to multiply this matrix.
	 * @return The product of this matrix and other.
	 */
	public LinearMatrix3 times(LinearMatrix3 other) {
		return new LinearMatrix3(
				_00 * other._00 + _01 * other._10 + _02 * other._20,
				_00 * other._10 + _01 * other._11 + _02 * other._21,
				_00 * other._20 + _01 * other._12 + _02 * other._22,
				_10 * other._00 + _11 * other._10 + _12 * other._20,
				_10 * other._10 + _11 * other._11 + _12 * other._21,
				_10 * other._20 + _11 * other._12 + _12 * other._22,
				_20 * other._00 + _21 * other._10 + _22 * other._20,
				_20 * other._10 + _21 * other._11 + _22 * other._21,
				_20 * other._20 + _21 * other._12 + _22 * other._22
				);
	}

	/**
	 * Multiples this matrix by the inverse of the specified matrix.
	 * Equivalent to {@code this = this.dividedBy(other);}
	 * @param other The matrix by which to divide this matrix.
	 * @see dividedBy, inverse
	 */
	public void divide(LinearMatrix3 other) {
		multiply(other.inverse());
	}

	/**
	 * Multiplies this matrix by the inverse of the specified matrix.
	 * @param other The matrix by which to divide this matrix.
	 * @return The product of this matrix and the inverse of other.
	 * @see inverse
	 */
	public LinearMatrix3 dividedBy(LinearMatrix3 other) {
		return this.times(other.inverse());
	}

	/**
	 * Scales the matrix by a constant factor.
	 * @param c The factor by which to scale the matrix.
	 */
	public void scale(double c) {
		_00 *= c; _01 *= c; _02 *= c;
		_10 *= c; _11 *= c; _12 *= c;
		_20 *= c; _21 *= c; _22 *= c;
	}

	/**
	 * Negates this matrix.
	 * Equivalent to {@code this = this.negative();} or {@code this.scale(-1.0);}
	 * @see negative, scale
	 */
	public void negate() {
		_00 = -_00; _01 = -_01; _02 = -_02;
		_10 = -_10; _11 = -_11; _12 = -_12;
		_20 = -_20; _21 = -_21; _22 = -_22;
	}

	/**
	 * Computes the negative of this matrix.
	 * @return The negation of this matrix.
	 */
	public LinearMatrix3 negative() {
		return new LinearMatrix3(
				-_00, -_01, -_02,
				-_10, -_11, -_12,
				-_20, -_21, -_22
				);
	}

	/**
	 * Inverts this matrix.
	 * Equivalent to {@code this = this.inverse();}
	 * @see inverse
	 */
	public void invert() {
		copy(this.inverse());
	}

	/**
	 * Computes the inverse of this matrix.
	 * The product of {@code this} and {@code this.inverse()} will be
	 * the identity matrix.
	 * @return The inverse of this matrix.
	 */
	public LinearMatrix3 inverse() {
		double			det = determinant();

		return new LinearMatrix3(
					(_11 * _22 - _12 * _21) / det, (_01 * _22 - _02 * _21) / det, (_01 * _12 - _02 * _11) / det,
					(_12 * _20 - _10 * _22) / det, (_00 * _22 - _02 * _20) / det, (_02 * _10 - _00 * _12) / det,
					(_10 * _21 - _11 * _20) / det, (_01 * _20 - _00 * _21) / det, (_00 * _11 - _01 * _10) / det
					);
	}

	/**
	 * Transposes this matrix.
	 * Equivalent to {@code this = this.transposed();}
	 * @see transposed
	 */
	public void transpose() {
		double temp;

		temp = _01; _01 = _10; _10 = temp;
		temp = _02; _02 = _20; _20 = temp;
		temp = _12; _12 = _21; _21 = temp;
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
	 * Copies another matrix.
	 * @param other The matrix to copy.
	 */
	private void copy(LinearMatrix3 other) {
		_00 = other._00; _01 = other._01; _02 = other._02;
		_10 = other._10; _11 = other._11; _12 = other._12;
		_20 = other._20; _21 = other._21; _22 = other._22;
	}

	/**
	 * The identity matrix ({@code this * IDENTITY == this}).
	 */
	public static final LinearMatrix3 IDENTITY = new LinearMatrix3(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0);

	/**
	 * The zero matrix ({@code this + IDENTITY == this}).
	 */
	public static final LinearMatrix3 ZERO = new LinearMatrix3(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

	private double _00, _01, _02;
	private double _10, _11, _12;
	private double _20, _21, _22;

}
