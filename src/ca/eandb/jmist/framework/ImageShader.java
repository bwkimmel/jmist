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
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Point2;

/**
 * Computes the colour channel responses for points on the image
 * plane.
 * @author Brad Kimmel
 */
public interface ImageShader extends Serializable {

	/**
	 * Obtains the camera colour channel responses at a
	 * specified point on the normalized image plane.
	 * @param p The point on the image plane in normalized
	 * 		device coordinates (must fall within
	 * 		{@code Box2.UNIT}).
	 * @param pixel The array to populate with the camera colour channel
	 * 		responses at the specified point on the image plane.  The pixel
	 * 		must have been created using {@code this.createPixel()}.  The
	 * 		values of the elements of responses shall not depend on their
	 * 		initial values.  Note that this requirement implies that this
	 * 		method is responsible for initializing the values of the pixel.  In
	 * 		particular, the implementation of this method must not assume that
	 * 		the elements have been set to zero prior to invocation.  If this
	 * 		parameter is <code>null</code>, the array will be created by this
	 * 		method.
	 * @return The array containing the colour channel responses at the
	 * 		specified point on the image plane.
	 * @see {@link Box2#UNIT}, {@link PixelFactory#createPixel()}.
	 */
	Color shadeAt(Point2 p, WavelengthPacket lambda);

}
