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

package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;

/**
 * A mutable, two-dimensional array of <code>Color</code>s.
 * @author Brad Kimmel
 */
public interface Raster extends RasterWriter, Serializable {

	/**
	 * Gets the <code>Color</code> stored at the specified pixel.
	 * @param x The x-coordinate of the pixel.
	 * @param y The y-coordinate of the pixel.
	 * @return The <code>Color</code> stored at the pixel.
	 */
	Color getPixel(int x, int y);

	/**
	 * Adds the <code>Color</code> to the value stored at the specified pixel.
	 * @param x The x-coordinate of the pixel.
	 * @param y The y-coordinate of the pixel.
	 */
	void addPixel(int x, int y, Color pixel);

}
