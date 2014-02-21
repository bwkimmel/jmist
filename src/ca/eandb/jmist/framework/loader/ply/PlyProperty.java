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
 * A property within an element in a PLY-file.
 * @see http://paulbourke.net/dataformats/ply/
 */
public interface PlyProperty {

	/**
	 * Gets the <code>PropertyDescriptor</code> describing this property.
	 * @return The <code>PropertyDescriptor</code> describing this property.
	 */
	PropertyDescriptor getPropertyDescriptor();

	/**
	 * Gets the normalized value of this property.  If this property is a
	 * floating point property, its value is returned.  If this property is an
	 * integral property, the value divided by the maximal value for this
	 * property's type is returned.  For example, if this property is an
	 * unsigned character (8-bit) whose value is <code>255</code>, a value of
	 * <code>1.0</code> would be returned.
	 *
	 * If this property is a list, the first value in the list will be
	 * returned.
	 *
	 * Equivalent to <code>getNormalizedDoubleValue(0)</code>.
	 *
	 * @return The normalized value of this property.
	 * @throws IndexOutOfBoundsException if this property is an empty list.
	 * @see #getNormalizedDoubleValue(int)
	 */
	double getNormalizedDoubleValue();

	/**
	 * Gets the value of this property, cast to a floating point value if
	 * necessary.
	 *
	 * Equivalent to <code>getDoubleValue(0)</code>.
	 *
	 * @return The value of this property.
	 * @throws IndexOutOfBoundsException if this property is an empty list.
	 * @see #getDoubleValue(int)
	 */
	double getDoubleValue();

	/**
	 * Gets the value of this property.  For floating point values, the value
	 * returned will be the value resulting from casting a value of that type
	 * to an <code>int</code>.
	 *
	 * Equivalent to <code>getIntValue(0)</code>.
	 *
	 * @return The value of this property.
	 * @throws IndexOutOfBoundsException if this property is an empty list.
	 * @see #getIntValue(int)
	 */
	int getIntValue();

	/**
	 * Gets the normalized value of an entry within this property.
	 * If this property has a floating point data type, the value is returned
	 * directly.  If this property has an integral data type, the value divided
	 * by the maximal value for this property's type is returned.  For example,
	 * if this property has an unsigned character (8-bit) data type and the
	 * underlying value is <code>255</code>, a value of <code>1.0</code> would
	 * be returned.
	 *
	 * If this property is a singleton, this method will behave as if the
	 * property were a list containing a single element.
	 *
	 * @param index The index of the entry in the list to get.
	 * @return The normalized value of the entry.
	 * @throws IndexOutOfBoundsException if <code>index &lt; 0</code>,
	 * 		<code>index &gt; getCount()</code>.
	 * @see #getCount()
	 */
	double getNormalizedDoubleValue(int index);

	/**
	 * Gets a value of an entry within this property, casting to a floating
	 * point value if necessary.
	 *
	 * If this property is a singleton, this method will behave as if the
	 * property were a list containing a single element.
	 *
	 * @param index The index of the entry in the list to get.
	 * @return The value of the entry.
	 * @throws IndexOutOfBoundsException if <code>index &lt; 0</code>,
	 * 		<code>index &gt; getCount()</code>.
	 * @see #getCount()
	 */
	double getDoubleValue(int index);

	/**
	 * Gets a value of an entry within this property.  For floating point
	 * values, the value returned will be the value resulting from casting a
	 * value of that type to an <code>int</code>.
	 *
	 * If this property is a singleton, this method will behave as if the
	 * property were a list containing a single element.
	 *
	 * @param index The index of the entry in the list to get.
	 * @return The value of the entry.
	 * @throws IndexOutOfBoundsException if <code>index &lt; 0</code>,
	 * 		<code>index &gt; getCount()</code>.
	 * @see #getCount()
	 */
	int getIntValue(int index);

	/**
	 * Gets the number of entries in this property.  If this property is a
	 * list, the size of the list is returned.  If this property is a
	 * singleton, <code>1</code> is returned.
	 * @return The number of entries in this property.
	 */
	int getCount();

}
