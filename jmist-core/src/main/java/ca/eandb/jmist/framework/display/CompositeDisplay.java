/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;

/**
 * A <code>Display</code> that forwards methods to each of a collection of
 * child <code>Display</code>s.
 * 
 * @author Brad Kimmel
 */
public final class CompositeDisplay implements Display, Serializable {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 1296043359308113088L;
	
	/** The <code>List</code> of child <code>Display</code>s to draw to. */
	private final List<Display> children = new ArrayList<Display>();
	
	/**
	 * Adds a new child <code>Display</code>.
	 * @param child The <code>Display</code> to add to the list.
	 * @return A reference to this <code>CompositeDisplay</code>.
	 */
	public CompositeDisplay addDisplay(Display child) {
		children.add(child);
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public void fill(int x, int y, int w, int h, Color color) {
		for (Display child : children) {
			child.fill(x, y, w, h, color);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#finish()
	 */
	public void finish() {
		for (Display child : children) {
			child.finish();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
	 */
	public void initialize(int w, int h, ColorModel colorModel) {
		for (Display child : children) {
			child.initialize(w, h, colorModel);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public void setPixel(int x, int y, Color pixel) {
		for (Display child : children) {
			child.setPixel(x, y, pixel);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
	 */
	public void setPixels(int x, int y, Raster pixels) {
		for (Display child : children) {
			child.setPixels(x, y, pixels);
		}
	}

}
