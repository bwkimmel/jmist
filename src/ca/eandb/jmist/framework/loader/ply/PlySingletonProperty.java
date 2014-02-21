/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.loader.ply;

/**
 * A <code>PlyProperty</code> representing a singleton (non-list) value.
 */
abstract class PlySingletonProperty implements PlyProperty {

	/** The <code>PropertyDescriptor</code> describing this property. */
	private final PropertyDescriptor descriptor;

	/**
	 * Initializes this <code>PlySingletonProperty</code>.
	 * @param descriptor The <code>PropertyDescriptor</code> describing this
	 * 		property.
	 * @throws IllegalArgumentException If <code>descriptor</code> describes a
	 * 		list property.
	 */
	public PlySingletonProperty(PropertyDescriptor descriptor) {
		if (descriptor.isList()) {
			throw new IllegalArgumentException("Property type must be a singleton");
		}
		this.descriptor = descriptor;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getPropertyDescriptor()
	 */
	@Override
	public final PropertyDescriptor getPropertyDescriptor() {
		return descriptor;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getCount()
	 */
	@Override
	public final int getCount() {
		return 1;
	}

	/**
	 * Ensures that the index is valid for this property.
	 * @param index The index to check.
	 * @throws IndexOutOfBoundsException if <code>index != 0</code>.
	 */
	private void checkIndex(int index) {
		if (index != 0) {
			throw new IndexOutOfBoundsException();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getNormalizedDoubleValue(int)
	 */
	@Override
	public double getNormalizedDoubleValue(int index) {
		checkIndex(index);
		return getNormalizedDoubleValue();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getDoubleValue(int)
	 */
	@Override
	public double getDoubleValue(int index) {
		checkIndex(index);
		return getDoubleValue();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getIntValue(int)
	 */
	@Override
	public int getIntValue(int index) {
		checkIndex(index);
		return getIntValue();
	}

}
