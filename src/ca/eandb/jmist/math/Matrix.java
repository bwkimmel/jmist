/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A two dimensional matrix.
 * @author Brad Kimmel
 */
public final class Matrix implements Serializable {

	/**
	 * Creates a new <code>Matrix</code>.
	 * @param elements The array of <code>double</code>s containing the
	 * 		elements of the matrix (must be large enough so that the index of
	 * 		the last element [<code>offset + (rows-1) * rowStride + (cols-1) *
	 * 		colStride</code>] is in the array).
	 * @param rows The number of rows in the matrix (must be non-negative).
	 * @param cols The number of columns in the matrix (must be non-negative).
	 * @param offset The offset into <code>elements</code> of the first element
	 * 		of the matrix (must be non-negative).
	 * @param rowStride The difference between the indices into
	 * 		<code>elements</code> of the first element of the first row and the
	 * 		first element of the second row.
	 * @param colStride The difference between the indices into
	 * 		<code>elements</code> of the first element of the first column and
	 * 		the first element of the second column.
	 * @throws IllegalArgumentException if <code>offset</code> is negative.
	 * @throws IllegalArgumentException if <code>rows</code> or
	 * 		<code>cols</code> is negative.
	 * @throws IllegalArgumentException if <code>elements</code> is not large
	 * 		enough to hold all elements of the array).
	 */
	private Matrix(double[] elements, int rows, int cols, int offset, int rowStride, int colStride) {

		if (offset < 0) {
			throw new IllegalArgumentException("offset must be non-negative");
		}

		if (rows < 0 || cols < 0) {
			throw new IllegalArgumentException("rows and cols must be non-negative");
		}

		if ((offset + (cols - 1) * colStride + (rows - 1) * rowStride) >= elements.length) {
			throw new IllegalArgumentException("not enough elements");
		}

		this.elements = elements;
		this.rows = rows;
		this.cols = cols;
		this.offset = offset;
		this.rowStride = rowStride;
		this.colStride = colStride;

	}

	/**
	 * Gets the number of rows in this <code>Matrix</code>.
	 * @return The number of rows in this <code>Matrix</code>.
	 */
	public int rows() {
		return this.rows;
	}

	/**
	 * Gets the number of columns in this <code>Matrix</code>.
	 * @return The number of columns in this <code>Matrix</code>.
	 */
	public int columns() {
		return this.cols;
	}

	/**
	 * Gets the number of elements in this <code>Matrix</code>.
	 * @return The number of elements in this <code>Matrix</code>.
	 */
	public int size() {
		return this.rows * this.cols;
	}

	/**
	 * Gets the smallest value in this <code>Matrix</code>.
	 * @return The smallest value in this <code>Matrix</code>.
	 */
	public double minimum() {

		double min = Double.POSITIVE_INFINITY;

		for (int r = 0, rpos = offset; r < rows; r++, rpos += rowStride) {
			for (int c = 0, pos = rpos; c < cols; c++, pos += colStride) {
				if (elements[pos] < min) {
					min = elements[pos];
				}
			}
		}

		return min;

	}

	/**
	 * Gets the largest value in this <code>Matrix</code>.
	 * @return The largest value in this <code>Matrix</code>.
	 */
	public double maximum() {

		double max = Double.NEGATIVE_INFINITY;

		for (int r = 0, rpos = offset; r < rows; r++, rpos += rowStride) {
			for (int c = 0, pos = rpos; c < cols; c++, pos += colStride) {
				if (elements[pos] > max) {
					max = elements[pos];
				}
			}
		}

		return max;

	}

	/**
	 * Gets the range of values in this <code>Matrix</code>.
	 * @return The ranges of values in this <code>Matrix</code>.
	 */
	public Interval range() {

		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;

		for (int r = 0, rpos = offset; r < rows; r++, rpos += rowStride) {
			for (int c = 0, pos = rpos; c < cols; c++, pos += colStride) {
				if (elements[pos] > max) {
					max = elements[pos];
				}
				if (elements[pos] < min) {
					min = elements[pos];
				}
			}
		}

		return new Interval(min, max);

	}

	/**
	 * Gets the element at the specified position in this <code>Matrix</code>.
	 * @param row The row of the element to obtain (must be non-negative and
	 * 		less than <code>this.rows()</code>).
	 * @param col The column of the element to obtain (must be non-negative and
	 * 		less than <code>this.columns()</code>).
	 * @return The element at the specified row and column in this
	 * 		<code>Matrix</code>.
	 * @throws IllegalArgumentException if <code>row</code> or <code>col</code>
	 * 		is out of bounds.
	 * @see #rows()
	 * @see #columns()
	 */
	public double at(int row, int col) {
		return this.elements[indexOf(row, col)];
	}

	/**
	 * Gets the index into {@link #elements} of the specified row and column.
	 * @param row The row of the element to get the index of (must be
	 * 		non-negative and less than <code>this.rows()</code>).
	 * @param col The column of the element to get the index of (must be
	 * 		non-negative and less than <code>this.columns()</code>).
	 * @return The index of the element at the specified row and column in this
	 * 		<code>Matrix</code>.
	 * @throws IllegalArgumentException if <code>row</code> or <code>col</code>
	 * 		is out of bounds.
	 * @see #rows()
	 * @see #columns()
	 */
	private int indexOf(int row, int col) {
		if (0 <= row && row < rows && 0 <= col && col < cols) {
			return offset + row * rowStride + col * colStride;
		} else {
			throw new IllegalArgumentException("Out of bounds.");
		}
	}

	/**
	 * Gets the transpose of this <code>Matrix</code> (i.e., the
	 * <code>Matrix</code> <code>T</code> such that
	 * <code>this.at(i, j) == T.at(j, i)</code> for all
	 * <code>i, j</code> with <code>0 &lt;= i &lt; this.rows()</code> and
	 * <code>0 &lt;= j &lt; this.columns()</code>.
	 * @return The transpose of this <code>Matrix</code>.
	 * @see #at(int, int)
	 * @see #rows()
	 * @see #columns()
	 */
	public Matrix transpose() {
		return new Matrix(elements, cols, rows, offset, colStride, rowStride);
	}

	/**
	 * Gets a view of a sub-matrix of this <code>Matrix</code> (i.e.,
	 * @param row The index of the first row of the sub-matrix (must be
	 * 		non-negative).
	 * @param col The index of the first column of the sub-matrix (must be
	 * 		non-negative).
	 * @param rows The number of rows of the sub-matrix (must be non-negative
	 * 		and satisfy <code>row + rows &lt;= this.rows()</code>).
	 * @param cols The number of columns of the sub-matrix (must be
	 * 		non-negative and satisfy
	 * 		<code>col + cols &lt;= this.columns()</code>).
	 * @return The <code>Matrix</code>, <code>T</code>, with <code>rows</code>
	 * 		rows and <code>cols</code> columns satisfying
	 * 		<code>T.at(i, j) == this.at(row + i, col + j)</code> for all
	 * 		<code>i, j</code> with <code>0 &lt;= i &lt; rows</code> and
	 * 		<code>0 &lt;= j &lt; cols</code>.
	 * @throws IllegalArgumentException if <code>row</code> is negative,
	 * 		<code>col</code> is negative, <code>rows</code> is negative,
	 * 		<code>cols</code> is negative,
	 * 		<code>row + rows &gt; this.rows()</code>, or
	 * 		<code>col + cols &gt; this.columns()</code>.
	 * @see #at(int, int)
	 * @see #rows()
	 * @see #columns()
	 */
	public Matrix slice(int row, int col, int rows, int cols) {
		return new Matrix(elements, rows, cols, indexOf(row, col), rowStride, colStride);
	}

	/**
	 * Gets a column vector of the diagonal elements of this
	 * <code>Matrix</code> (i.e., the elements <code>this.at(i, i)</code> for
	 * <code>0 &lt;= i &lt; Math.min(this.rows(), this.columns())</code>).
	 * @return The column vector of the diagonal elements of this
	 * 		<code>Matrix</code>.
	 * @see #at(int, int)
	 * @see Math#min(int, int)
	 */
	public Matrix diagonal() {
		return new Matrix(elements, Math.min(rows, cols), 1, offset, rowStride + colStride, 0);
	}

	/**
	 * Gets the specified row of this <code>Matrix</code>.
	 * @param row The row to get (must be non-negative and less than
	 * 		<code>this.rows()</code>).
	 * @return The specified row of this <code>Matrix</code>.
	 * @throws IllegalArgumentException if <code>row</code> is negative or if
	 * 		<code>row &gt;= this.rows()</code>.
	 * @see #rows()
	 */
	public Matrix row(int row) {
		return slice(row, 0, 1, cols);
	}

	/**
	 * Gets the specified column of this <code>Matrix</code>.
	 * @param col The column to get (must be non-negative and less than
	 * 		<code>this.columns()</code>).
	 * @return The specified column of this <code>Matrix</code>.
	 * @throws IllegalArgumentException if <code>col</code> is negative or if
	 * 		<code>col &gt;= this.columns()</code>.
	 * @see #columns()
	 */
	public Matrix column(int col) {
		return slice(0, col, rows, 1);
	}

	/**
	 * Gets a <code>Matrix</code> consisting of all zeros.
	 * @param rows The number of rows in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @param cols The number of columns in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @return The zero <code>Matrix</code> with the specified dimensions.
	 * @throws IllegalArgumentException if <code>rows</code> or
	 * 		<code>cols</code> is negative.
	 */
	public static Matrix zeros(int rows, int cols) {
		return new Matrix(ZERO, rows, cols, 0, 0, 0);
	}

	/**
	 * Gets a <code>Matrix</code> consisting of all ones.
	 * @param rows The number of rows in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @param cols The number of columns in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @return The <code>Matrix</code> with the specified dimensions consisting
	 * 		of all ones.
	 * @throws IllegalArgumentException if <code>rows</code> or
	 * 		<code>cols</code> is negative.
	 */
	public static Matrix ones(int rows, int cols) {
		return new Matrix(ONE, rows, cols, 0, 0, 0);
	}

	/**
	 * Gets a <code>Matrix</code> with each element having the same value.
	 * @param rows The number of rows in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @param cols The number of columns in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @return The <code>Matrix</code> with all elements having the same value.
	 * @throws IllegalArgumentException if <code>rows</code> or
	 * 		<code>cols</code> is negative.
	 */
	public static Matrix constant(int rows, int cols, double value) {
		return new Matrix(new double[]{ value }, rows, cols, 0, 0, 0);
	}

	/**
	 * Gets the identity <code>Matrix</code> of the specified size.
	 * @param n The size of the <code>Matrix</code> (must be non-negative).
	 * @return The <code>n</code> by <code>n</code> identity
	 * 		<code>Matrix</code>.
	 * @throws IllegalArgumentException if <code>n</code> is negative.
	 */
	public static Matrix identity(int n) {
		int size = n * n;
		double[] elements = new double[size];
		for (int i = 0; i < size; i += (n + 1)) {
			elements[i] = 1.0;
		}
		return Matrix.columnMajor(n, n, elements);
	}

	/**
	 * Creates a <code>Matrix</code> with the specified values in column-major
	 * order.
	 * @param rows The number of rows in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @param cols The number of columns in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @param elements The elements of the <code>Matrix</code> in column-major
	 * 		order (must have at least <code>rows * cols</code> elements --
	 * 		additional elements will be ignored).
	 * @return The <code>rows</code> by <code>cols</code> <code>Matrix</code>
	 * 		consisting of the elements specified.
	 * @throws IllegalArgumentException if
	 * 		<code>elements.length &lt; rows * cols</code>.
	 * @throws IllegalArgumentException if <code>rows</code> or
	 * 		<code>cols</code> is negative.
	 */
	public static Matrix columnMajor(int rows, int cols, double[] elements) {
		return new Matrix(elements, rows, cols, 0, 1, rows);
	}

	/**
	 * Creates a <code>Matrix</code> with the specified values in row-major
	 * order.
	 * @param rows The number of rows in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @param cols The number of columns in the <code>Matrix</code> (must be
	 * 		non-negative).
	 * @param elements The elements of the <code>Matrix</code> in row-major
	 * 		order (must have at least <code>rows * cols</code> elements --
	 * 		additional elements will be ignored).
	 * @return The <code>rows</code> by <code>cols</code> <code>Matrix</code>
	 * 		consisting of the elements specified.
	 * @throws IllegalArgumentException if
	 * 		<code>elements.length &lt; rows * cols</code>.
	 * @throws IllegalArgumentException if <code>rows</code> or
	 * 		<code>cols</code> is negative.
	 */
	public static Matrix rowMajor(int rows, int cols, double[] elements) {
		return new Matrix(elements, rows, cols, 0, cols, 1);
	}

	/**
	 * Computes the product of two matrices.
	 * @param other The <code>Matrix</code> by which to multiply this
	 * 		<code>Matrix</code> (must have as many rows as this
	 * 		<code>Matrix</code> has columns).
	 * @return The product of this <code>Matrix</code> with <code>other</code>.
	 * @throws IllegalArgumentException if
	 * 		<code>this.columns() != other.rows()</code>.
	 * @see #rows()
	 * @see #columns()
	 */
	public Matrix times(Matrix other) {

		if (this.cols != other.rows) {
			throw new IllegalArgumentException("Cannot multiply matrices: wrong dimensions.");
		}

		double[] elements = new double[this.rows * other.cols];
		for (int r = 0, i = 0; r < this.rows; r++) {
			for (int c = 0; c < other.cols; c++, i++) {
				for (int j = 0; j < this.cols; j++) {
					elements[i] += this.at(r, j) * other.at(j, c);
				}
			}
		}

		return Matrix.columnMajor(this.rows, other.cols, elements);

	}

	/**
	 * Computes the sum of two matrices.
	 * @param other The <code>Matrix</code> to which to add this
	 * 		<code>Matrix</code> (must have the same dimensions as this
	 * 		<code>Matrix</code>).
	 * @return The sum of this <code>Matrix</code> and <code>other</code>.
	 * @throws IllegalArgumentException if <code>other</code> has different
	 * 		dimensions than <code>this</code>.
	 */
	public Matrix plus(Matrix other) {

		if (this.rows != other.rows || this.cols != other.cols) {
			throw new IllegalArgumentException("Cannot add matrices of different dimensions.");
		}

		double[] elements = new double[rows * cols];
		for (int r = 0, i = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++, i++) {
				elements[i] = this.at(r, c) + other.at(r, c);
			}
		}

		return Matrix.columnMajor(rows, cols, elements);

	}

	/**
	 * Computes the difference of two matrices.
	 * @param other The <code>Matrix</code> subtract from this
	 * 		<code>Matrix</code> (must have the same dimensions as this
	 * 		<code>Matrix</code>).
	 * @return The difference between this <code>Matrix</code> and
	 * 		<code>other</code>.
	 * @throws IllegalArgumentException if <code>other</code> has different
	 * 		dimensions than <code>this</code>.
	 */
	public Matrix minus(Matrix other) {

		if (this.rows != other.rows || this.cols != other.cols) {
			throw new IllegalArgumentException("Cannot subtract matrices of different dimensions.");
		}

		double[] elements = new double[rows * cols];
		for (int r = 0, i = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++, i++) {
				elements[i] = this.at(r, c) - other.at(r, c);
			}
		}

		return Matrix.columnMajor(rows, cols, elements);

	}

	/** The array to use for the elements of zero matrices. */
	private static final double[] ZERO = new double[]{ 0.0 };

	/** The array to use for elements of matrices consisting of all ones. */
	private static final double[] ONE = new double[]{ 1.0 };

	/** The array containing the elements of this <code>Matrix</code>. */
	private final double[] elements;

	/** The number of rows. */
	private final int rows;

	/** The number of columns. */
	private final int cols;

	/** The index into {@link #elements} of the first element. */
	private final int offset;

	/**
	 * The difference between the indices into {@link #elements} of the first
	 * element of the first row and the first element of the second row.
	 */
	private final int rowStride;

	/**
	 * The difference between the indices into {@link #elements} of the first
	 * element of the first column and the first element of the second column.
	 */
	private final int colStride;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -5903472706313095023L;

}
