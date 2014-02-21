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
 * A target for <code>PlyReader</code> to receive and process elements from a
 * PLY-file.
 */
public interface PlyTarget {

	/**
	 * Indicates the beginning of a section (i.e., a block of elements of a
	 * single type).  Implementations should return an
	 * <code>ElementListener</code> to receive the individual elements as they
	 * are read.
	 * @param desc The <code>ElementDescriptor</code> describing the elements
	 * 		to follow.
	 * @return The <code>ElementListener</code> that is to receive the elements
	 * 		read from the PLY-file, or <code>null</code> if the elements are to
	 * 		be ignored.
	 */
	ElementListener beginSection(ElementDescriptor desc);

	/** Indicates the end of a section. */
	void endSection();

}
