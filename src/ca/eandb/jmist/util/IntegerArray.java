/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

package ca.eandb.jmist.util;

import java.util.Arrays;

/**
 * A resizable array of integers.
 * @author brad
 */
public final class IntegerArray {

	/** The elements of this array. */
	private int[] elements;

	/** The number of elements in this array. */
	private int size;

	/**
	 * Creates an empty <code>IntegerArray</code>.
	 */
	public IntegerArray() {
		this(0);
	}

	/**
	 * Creates an empty <code>IntegerArray</code>.
	 * @param capacity The initial capacity of the array.
	 */
	public IntegerArray(int capacity) {
		elements = new int[capacity];
		size = 0;
	}

	/**
	 * Creates an <code>IntegerArray</code> containing the specified elements.
	 * @param elements An array of elements to initialize the new
	 * 		<code>IntegerArray</code> with.
	 */
	public IntegerArray(int[] elements) {
		this.elements = elements.clone();
		this.size = elements.length;
	}

	/**
	 * Gets an element of this array.
	 * @param index The index of the element to get.
	 * @return The indexed element.
	 * @throws IndexOutOfBoundsException if <code>index &lt; 0 || index &gt;= size</code>.
	 */
	public int get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return elements[index];
	}

	/**
	 * Sets an element of this array.  If <code>index</code> is greater than or
	 * equal to the size of this array, the array is expanded to accommodate
	 * the new element and the new space is zero-filled before the element is
	 * set.
	 * @param index The index of the element to set.
	 * @param e The value of the element.
	 * @throws IndexOutOfBoundsException if <code>index</code> is negative.
	 */
	public void set(int index, int e) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index >= size) {
			size = index + 1;
			ensureCapacity(size);
		}
		elements[index] = e;
	}

	/**
	 * Sets a range of elements of this array.  If necessary, the array is
	 * expanded to accommodate the new elements and the new space is zero-
	 * filled before setting the elements.
	 * @param index The index of the first element to set.
	 * @param items The array of values to set.
	 * @throws IndexOutOfBoundsException if <code>index</code> is negative.
	 */
	public void setAll(int index, int[] items) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index + items.length > size) {
			size = index + items.length;
			ensureCapacity(size);
		}
		for (int i = index, j = 0; j < items.length; i++, j++) {
			elements[i] = items[j];
		}
	}

	/**
	 * Appends a value to the end of this array.
	 * @param e The value to append.
	 */
	public void add(int e) {
		ensureCapacity(size + 1);
		elements[size++] = e;
	}

	/**
	 * Appends a range of values to the end of this array.
	 * @param items The values to append.
	 */
	public void addAll(int[] items) {
		ensureCapacity(size + items.length);
		for (int i = 0; i < items.length; i++) {
			elements[size++] = items[i];
		}
	}

	/**
	 * Clears the array.
	 */
	public void clear() {
		size = 0;
	}

	/**
	 * Returns the number of elements in this array.
	 * @return The number of elements in this array.
	 */
	public int size() {
		return size;
	}

	/**
	 * Reallocates storage for this array so that there is only enough capacity
	 * to hold all elements currently in this array.
	 */
	public void compact() {
		resize(size);
	}

	/**
	 * Ensures that there is enough room in the array to hold the specified
	 * number of elements.
	 * @param size The required capacity.
	 */
	private void ensureCapacity(int size) {
		if (size > elements.length) {
			resize(Math.max(size, 2 * elements.length));
		}
	}

	/**
	 * Resizes the underlying array.
	 * @param capacity The new size for the underlying array.
	 */
	private void resize(int capacity) {
		if (capacity != elements.length) {
			assert(size <= capacity);
			elements = Arrays.copyOf(elements, capacity);
		}
	}

}
