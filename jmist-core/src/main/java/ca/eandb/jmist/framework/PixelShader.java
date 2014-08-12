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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Box2;

/**
 * Computes a <code>Color</code> for a pixel (as indicated by its bounds,
 * represented as a 2D box in normalized device coordinates).
 * @author Brad Kimmel
 */
public interface PixelShader extends Serializable {

	/**
	 * Computes an estimate of the mean channel responses at
	 * the specified pixel.
	 * @param bounds The bounds of the pixel in normalized
	 * 		device coordinates (must be bounded by
	 * 		{@code Box2.UNIT}).
	 * @return An <code>Color</code> containing of the pixel responses.
	 * @see Box2#UNIT
	 */
	Color shadePixel(Box2 bounds);

}
