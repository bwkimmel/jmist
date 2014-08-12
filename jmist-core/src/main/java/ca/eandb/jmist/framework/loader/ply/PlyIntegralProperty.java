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
 * A <code>PlyProperty</code> consisting of a single integral value.
 */
final class PlyIntegralProperty extends PlySingletonProperty {

	/** The value of this property. */
	private final long value;

	/**
	 * Creates a new <code>PlyIntegralProperty</code>.
	 * @param value The value of the property.
	 * @param descriptor The <code>PropertyDescriptor</code> describing this
	 * 		property.
	 * @throws IllegalArgumentException if <code>descriptor</code> describes a
	 * 		list or floating point (non-integral) property.
	 */
	public PlyIntegralProperty(long value, PropertyDescriptor descriptor) {
		super(descriptor);

		if (!descriptor.getDataType().isIntegral()) {
			throw new IllegalArgumentException("Integral data type required");
		}
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getNormalizedDoubleValue()
	 */
	@Override
	public double getNormalizedDoubleValue() {
		return (double) value / (double) getPropertyDescriptor().getDataType().getMaxValue();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getDoubleValue()
	 */
	@Override
	public double getDoubleValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getIntValue()
	 */
	@Override
	public int getIntValue() {
		return (int) value;
	}

}
