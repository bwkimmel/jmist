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

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

/**
 * A resizable array of bytes.
 * @author brad
 */
public final class ByteArray extends AbstractList<Byte> implements RandomAccess {

	/** The elements of this array. */
	private byte[] elements;

	/** The number of elements in this array. */
	private int size;

	/**
	 * Creates an empty <code>ByteArray</code>.
	 */
	public ByteArray() {
		this(0);
	}

	/**
	 * Creates an empty <code>ByteArray</code>.
	 * @param capacity The initial capacity of the array.
	 */
	public ByteArray(int capacity) {
		elements = new byte[capacity];
		size = 0;
	}

	/**
	 * Creates an <code>ByteArray</code> containing the specified elements.
	 * @param elements An array of elements to initialize the new array with.
	 */
	public ByteArray(byte[] elements) {
		this.elements = elements.clone();
		this.size = elements.length;
	}

	/**
	 * Creates an <code>ByteArray</code> containing the specified elements.
	 * @param c A collection of elements to initialize the new array with.
	 */
	public ByteArray(Collection<Byte> c) {
		this.elements = new byte[c.size()];
		this.size = 0;
		for (byte e : c) {
			elements[size++] = e;
		}
	}

	/**
	 * Creates a copy of an <code>ByteArray</code>.
	 * @param other The array to copy.
	 */
	public ByteArray(ByteArray other) {
		this.elements = other.elements.clone();
		this.size = other.size;
	}

	/**
	 * Converts this <code>ByteArray</code> to an array of bytes.
	 * @return An array of bytes containing the same elements as this array.
	 */
	public byte[] toByteArray() {
		return Arrays.copyOf(elements, size);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ByteArray clone() throws CloneNotSupportedException {
		return new ByteArray(this);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#clear()
	 */
	@Override
	public void clear() {
		size = 0;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Ensures that the specified index is valid for this array.
	 * @param index The index to check.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>index &lt; 0 || index &gt;= size</code>.
	 */
	private void rangeCheck(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Ensures that the specified range of indices is valid for this array.
	 * @param fromIndex The (inclusive) start of the range of indices to check.
	 * @param toIndex The (exclusive) end of the range of indices to check.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>fromIndex &lt; 0 || toIndex &gt; size</code>.
	 */
	private void rangeCheck(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > size) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Gets an element of this array.
	 * @param index The index of the element to get.
	 * @return The indexed element.
	 * @throws IndexOutOfBoundsException if <code>index &lt; 0 || index &gt;= size</code>.
	 */
	public Byte get(int index) {
		rangeCheck(index);
		return elements[index];
	}

	/**
	 * Sets an element of this array.
	 * @param index The index of the element to set.
	 * @param e The value of the element.
	 * @return The value previously stored at the specified index.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>index &lt; 0 || index &gt;= size()</code>.
	 */
	public byte set(int index, byte e) {
		rangeCheck(index);
		byte value = elements[index];
		elements[index] = e;
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#set(int, java.lang.Object)
	 */
	@Override
	public Byte set(int index, Byte element) {
		return set(index, element.byteValue());
	}

	/**
	 * Sets a range of elements of this array.
	 * @param index The index of the first element to set.
	 * @param items The values to set.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>index &lt; 0 || index + items.length &gt; size()</code>.
	 */
	public void setAll(int index, byte[] items) {
		rangeCheck(index, index + items.length);
		for (int i = index, j = 0; j < items.length; i++, j++) {
			elements[i] = items[j];
		}
	}

	/**
	 * Sets a range of elements of this array.
	 * @param index The index of the first element to set.
	 * @param items The values to set.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>index &lt; 0 || index + items.size() &gt; size()</code>.
	 */
	public void setAll(int index, ByteArray items) {
		rangeCheck(index, index + items.size);
		for (int i = index, j = 0; j < items.size; i++, j++) {
			elements[i] = items.elements[j];
		}
	}

	/**
	 * Sets a range of elements of this array.
	 * @param index The index of the first element to set.
	 * @param items The values to set.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>index &lt; 0 || index + items.size() &gt; size()</code>.
	 */
	public void setAll(int index, Collection<? extends Byte> items) {
		rangeCheck(index, index + items.size());
		for (byte e : items) {
			elements[index++] = e;
		}
	}

	/**
	 * Appends a value to the end of this array.
	 * @param e The value to append.
	 * @return Always returns <code>true</code>.
	 */
	public boolean add(byte e) {
		ensureCapacity(size + 1);
		elements[size++] = e;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#add(java.lang.Object)
	 */
	@Override
	public boolean add(Byte e) {
		return add(e.byteValue());
	}

	/**
	 * Appends a range of values to the end of this array.
	 * @param items The values to append.
	 * @return A value indicating if the array has changed.
	 */
	public boolean addAll(byte[] items) {
		ensureCapacity(size + items.length);
		for (int i = 0; i < items.length; i++) {
			elements[size++] = items[i];
		}
		return items.length > 0;
	}

	/**
	 * Appends a range of values to the end of this array.
	 * @param items The values to append.
	 * @return A value indicating if the array has changed.
	 */
	public boolean addAll(ByteArray items) {
		ensureCapacity(size + items.size);
		for (int i = 0; i < items.size; i++) {
			elements[size++] = items.elements[i];
		}
		return items.size > 0;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends Byte> c) {
		ensureCapacity(size + c.size());
		for (byte e : c) {
			elements[size++] = e;
		}
		return c.size() > 0;
	}

	/**
	 * Inserts a value into the array at the specified index.
	 * @param index The index at which to insert the new value.
	 * @param e The new value to insert.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>index &lt; 0 || index &gt; size()</code>.
	 */
	public void add(int index, byte e) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		ensureCapacity(size + 1);
		if (index < size) {
			for (int i = size; i > index; i--) {
				elements[i] = elements[i - 1];
			}
		}
		elements[index] = e;
		size++;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, Byte element) {
		add(index, element.byteValue());
	}

	/**
	 * Inserts values into the array at the specified index.
	 * @param index The index at which to insert the new values.
	 * @param items An array of values to insert.
	 * @return A value indicating if the array has changed.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>index &lt; 0 || index &gt; size()</code>.
	 */
	public boolean addAll(int index, byte[] items) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		ensureCapacity(size + items.length);
		if (index < size) {
			for (int i = size - 1; i >= index; i--) {
				elements[i + items.length] = elements[i];
			}
		}
		for (byte e : items) {
			elements[index++] = e;
		}
		size += items.length;
		return items.length > 0;
	}

	/**
	 * Inserts values into the array at the specified index.
	 * @param index The index at which to insert the new values.
	 * @param items A <code>ByteArray</code> containing the values to insert.
	 * @return A value indicating if the array has changed.
	 * @throws IndexOutOfBoundsException if
	 * 		<code>index &lt; 0 || index &gt; size()</code>.
	 */
	public boolean addAll(int index, ByteArray items) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		ensureCapacity(size + items.size);
		if (index < size) {
			for (int i = size - 1; i >= index; i--) {
				elements[i + items.size] = elements[i];
			}
		}
		for (int i = 0; i < items.size; i++) {
			elements[index++] = items.elements[i];
		}
		size += items.size;
		return items.size > 0;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends Byte> c) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		ensureCapacity(size + c.size());
		size += c.size();
		for (int i = size + c.size() - 1, j = size - 1; j >= index; i--, j--) {
			elements[i] = elements[j];
		}
		for (byte e : c) {
			elements[index++] = e;
		}
		return c.size() > 0;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public Byte remove(int index) {
		rangeCheck(index);
		byte value = elements[index];
		for (int i = index + 1; i < size; i++) {
			elements[i - 1] = elements[i];
		}
		size -= 1;
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#removeRange(int, int)
	 */
	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		rangeCheck(fromIndex, toIndex);
		for (int i = fromIndex, j = toIndex; j < size; j++) {
			elements[i] = elements[j];
		}
		size -= (toIndex - fromIndex);
	}

	/**
	 * Resizes the array to the specified length, truncating or zero-padding
	 * the array as necessary.
	 * @param newSize The new size of the array.
	 */
	public void resize(int newSize) {
		if (newSize > elements.length) {
			reallocate(Math.max(newSize, 2 * elements.length));
		} else {
			for (int i = size; i < newSize; i++) {
				elements[i] = 0;
			}
		}
		size = newSize;
	}

	/**
	 * Reallocates storage for this array so that there is only enough capacity
	 * to hold all elements currently in this array.
	 */
	public void trimToSize() {
		reallocate(size);
	}

	/**
	 * Ensures that there is enough room in the array to hold the specified
	 * number of elements.
	 * @param size The required capacity.
	 */
	public void ensureCapacity(int size) {
		if (size > elements.length) {
			reallocate(Math.max(size, 2 * elements.length));
		}
	}

	/**
	 * Resizes the underlying array.
	 * @param capacity The new size for the underlying array.
	 */
	private void reallocate(int capacity) {
		if (capacity != elements.length) {
			assert(size <= capacity);
			elements = Arrays.copyOf(elements, capacity);
		}
	}

}
