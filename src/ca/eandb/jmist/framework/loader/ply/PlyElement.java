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

import java.util.HashMap;
import java.util.Map;

/**
 * An element read from a PLY-file.
 * @see http://paulbourke.net/dataformats/ply/
 */
public final class PlyElement {

	/** The <code>ElementDescriptor</code> describing this element. */
	private final ElementDescriptor descriptor;

	/** The properties of this element. */
	private final Map<String, PlyProperty> properties = new HashMap<String, PlyProperty>();

	/**
	 * Creates a new <code>PlyElement</code> (only accessible within this
	 * package).
	 * @param descriptor The <code>ElementDescriptor</code> describing this
	 * 		element.
	 */
	PlyElement(ElementDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Adds a property to this element (only accessible within this package).
	 * @param property The <code>PlyProperty</code> to add.
	 */
	/* package */
	void addProperty(PlyProperty property) {
		properties.put(property.getPropertyDescriptor().getName(), property);
	}

	/**
	 * Gets the <code>ElementDescriptor</code> describing this element.
	 * @return The <code>ElementDescriptor</code> describing this element.
	 */
	public ElementDescriptor getElementDescriptor() {
		return descriptor;
	}

	/**
	 * Gets the property with the specified name.
	 * @param name The name of the property to get.
	 * @return The requested <code>PlyProperty</code>, if it exists, or
	 * 		<code>null</code> otherwise.
	 */
	public PlyProperty getProperty(String name) {
		return properties.get(name);
	}

}
