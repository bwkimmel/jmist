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

import java.util.Iterator;

/**
 * A generic two dimensional array.
 * @author Brad Kimmel
 */
public final class Array2<T> implements Iterable<T> {

  /** The number of rows in the array. */
  private final int rows;

  /** The number of columns in the array. */
  private final int cols;

  /**
   * The difference between the indices of the first elements of successive
   * rows.
   */
  private final int rowStride;

  /** The offset into the array of the first element. */
  private final int offset;

  /** The linear array of elements. */
  private final Object[] elements;

  /**
   * Creates a new <code>Array2</code>.
   * @param rows The number of rows.
   * @param cols The number of columns.
   * @param rowStride The difference between the indices of the first
   *     elements of successive rows.
   * @param offset The offset into the array of the first element.
   * @param elements The linear array of elements.
   * @throws IllegalArgumentException if
   *     <code>rows &lt; 0 || cols &lt; 0</code>.
   * @throws IllegalArgumentException if <code>offset &lt; 0</code>.
   * @throws IllegalArgumentException if <code>elements</code> is not large
   *     enough to hold all elements of the array.
   */
  private Array2(int rows, int cols, int rowStride, int offset, Object[] elements) {
    if (rows < 0 || cols < 0) {
      throw new IllegalArgumentException("Dimensions must be non-negative");
    }
    if (offset < 0) {
      throw new IllegalArgumentException("offset < 0");
    }
    if ((offset + (rows - 1) * rowStride + cols) > elements.length) {
      throw new IllegalArgumentException("elements array not large enough");
    }
    this.rows = rows;
    this.cols = cols;
    this.rowStride = rowStride;
    this.offset = offset;
    this.elements = elements;
  }

  /**
   * Creates a new <code>Array2</code>.
   * @param rows The number of rows.
   * @param cols The number of columns.
   */
  public Array2(int rows, int cols) {
    this(rows, cols, cols, 0, new Object[rows * cols]);
  }

  /**
   * Creates a new <code>Array2</code>.
   * @param rows The number of rows.
   * @param cols The number of columns.
   * @param elements The linear array of elements.
   * @throws IllegalArgumentException if
   *     <code>elements.length &lt; rows * cols</code>.
   */
  public Array2(int rows, int cols, T[] elements) {
    this(rows, cols, cols, 0, elements);
  }

  /**
   * Gets the total number of elements.
   * @return The total number of elements.
   */
  public int size() {
    return rows * cols;
  }

  /**
   * Gets the length of this <code>Array2</code> along the specified
   * dimension.
   * @param dim The index of the dimension along which to get the length.
   * @return The length of the array along the specified dimension.
   * @throws IndexOutOfBoundsException if
   *     <code>dim &lt; 0 || dim &gt;= 2</code>.
   */
  public int size(int dim) {
    switch (dim) {
    case 0: return rows;
    case 1: return cols;
    }
    throw new IndexOutOfBoundsException();
  }

  /**
   * Gets the number of rows.
   * @return The number of rows.
   */
  public int rows() {
    return rows;
  }

  /**
   * Gets the number of columns.
   * @return The number of columns.
   */
  public int columns() {
    return cols;
  }

  /**
   * Gets the index into the linear array of the specified element.
   * @param row The row containing the element to get the index of.
   * @param col The column containing the element to get the index of.
   * @throws IndexOutOfBoundsException if
   *     <code>row &lt; 0 || row &gt;= this.rows() ||
   *     col &lt; 0 || col &gt;= this.columns()</code>.
   */
  private int indexOf(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IndexOutOfBoundsException();
    }
    return offset + row * rowStride + col;
  }

  /**
   * Creates a view of a block of elements of this <code>Array2</code>.  Any
   * changes to the view will be reflected in this <code>Array2</code>.
   * @param row The first row of the slice.
   * @param col The first column of the slice.
   * @param rows The number of rows in the slice.
   * @param cols The number of columns in the slice.
   * @return A view of a block of elements of this <code>Array2</code>.
   * @throws IndexOutOfBoundsException if
   *     <code>row &lt; 0 || row + rows &gt;= this.rows() ||
   *     col &lt; 0 || col + cols &gt; this.columns()</code>.
   * @throws IllegalArgumentException if
   *     <code>rows &lt; 0 || cols &lt; 0</code>.
   */
  public Array2<T> slice(int row, int col, int rows, int cols) {
    if (row + rows > this.rows || col + cols > this.cols) {
      throw new IndexOutOfBoundsException();
    }
    return new Array2<>(rows, cols, rowStride, indexOf(row, col), elements);
  }

  /**
   * Sets the specified element of this <code>Array2</code>.
   * @param row The row containing the element to set.
   * @param col The column containing the element to set.
   * @param value The value to assign to the element.
   * @throws IndexOutOfBoundsException if
   *     <code>row &lt; 0 || row &gt;= this.rows() ||
   *     col &lt; 0 || col &gt;= this.columns()</code>.
   */
  public void set(int row, int col, T value) {
    elements[indexOf(row, col)] = value;
  }

  /**
   * Gets the specified element.
   * @param row The row containing the element to get.
   * @param col The column containing the element to get.
   * @return The specified element.
   * @throws IndexOutOfBoundsException if
   *     <code>row &lt; 0 || row &gt;= this.rows() ||
   *     col &lt; 0 || col &gt;= this.columns()</code>.
   */
  @SuppressWarnings("unchecked")
  public T get(int row, int col) {
    return (T) elements[indexOf(row, col)];
  }

  /**
   * Sets all elements of this <code>Array2</code> to the specified value.
   * @param value The value to set all elements to.
   */
  public void setAll(T value) {
    int rowOffset = offset;
    for (int row = 0; row < rows; row++) {
      int index = rowOffset;
      for (int col = 0; col < cols; col++) {
        elements[index++] = value;
      }
      rowOffset += rowStride;
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new Array2Iterator();
  }

  /**
   * An <code>Iterator</code> for traversing the elements of this
   * <code>Array2</code>.
   */
  private final class Array2Iterator implements Iterator<T> {

    /** The current row. */
    private int row = 0;

    /** The current column. */
    private int col = 0;

    @Override
    public boolean hasNext() {
      return row < rows;
    }

    @Override
    public T next() {
      T value = get(row, col++);
      if (col >= cols) {
        row++;
        col = 0;
      }
      return value;
    }

  }

}
