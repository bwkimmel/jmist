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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Describes an element in a PLY-file.
 * @see http://paulbourke.net/dataformats/ply/
 */
public final class ElementDescriptor {

	/** The name of the element. */
	private final String name;

	/** The number of instances of this element type. */
	private final int count;

	/** The list of properties this element is composed from. */
	private final List<PropertyDescriptor> properties;

	/**
	 * Creates a new <code>ElementDescriptor</code> (should only be created
	 * within this package).
	 * @param name The name of the element.
	 * @param count The number of instances of this element type.
	 * @param properties The list of properties this element is composed from.
	 */
	ElementDescriptor(String name, int count, List<PropertyDescriptor> properties) {
		this.name = name;
		this.count = count;
		this.properties = Collections.unmodifiableList(properties);
	}

	/**
	 * Gets the name of this element.
	 * @return The name of this element.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the number of instances of this element type.
	 * @return The number of instances of this element type.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Gets a read-only list of properties that this element is composed from.
	 * @return A read-only list of properties that this element is composed
	 * 		from.
	 */
	public List<PropertyDescriptor> getProperties() {
		return properties;
	}

	/**
	 * Gets the <code>PropertyDescriptor</code> for the property with the
	 * specified name.
	 * @param name The name of the property to search for.
	 * @return The <code>PropertyDescriptor</code> for the property with the
	 * 		specified name, or <code>null</code> if elements of this type do
	 * 		not have a property with the specified name.
	 */
	public PropertyDescriptor getPropertyDescriptor(String name) {
		for (PropertyDescriptor property : properties) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}

	/**
	 * Determines if this element has a property with the provided name.
	 * @param name The name of the property to search for.
	 * @return A value indicating if elements of this type have a property with
	 * 		the specified name.
	 */
	public boolean hasProperty(String name) {
		return getPropertyDescriptor(name) != null;
	}

	/**
	 * Determines if this element has properties with all of the specified
	 * names.
	 * @param names The names of the properties to search for.
	 * @return A value indicating if elements of this type have properties with
	 * 		all of the specified names.
	 */
	public boolean hasAllProperties(Iterable<String> names) {
		for (String name : names) {
			if (!hasProperty(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines if this element has properties with all of the specified
	 * names.
	 * @param names The names of the properties to search for.
	 * @return A value indicating if elements of this type have properties with
	 * 		all of the specified names.
	 */
	public boolean hasAllProperties(String... names) {
		return hasAllProperties(Arrays.asList(names));
	}

	/**
	 * Reads an element of this type from the provided <code>DataReader</code>.
	 * This method is internal to this package.
	 * @param reader The <code>DataReader</code> to read an element from.
	 * @throws IOException If an error occurs while reading from the
	 * 		<code>DataReader</code>.
	 */
	PlyElement read(DataReader reader) throws IOException {
		PlyElement element = new PlyElement(this);
		for (PropertyDescriptor property : properties) {
			element.addProperty(property.read(reader));
		}
		return element;
	}

}
