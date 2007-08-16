/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * Computes the colour channel responses for points on the image
 * plane.
 * @author bkimmel
 */
public interface IImageShader extends IPixelFactory {

	/**
	 * Obtains the camera colour channel responses at a
	 * specified point on the normalized image plane.
	 * @param p The point on the image plane in normalized
	 * 		device coordinates (must fall within
	 * 		{@code Box2.UNIT}).
	 * @param pixel The camera colour channel responses
	 * 		at the specified point on the image plane.  The pixel
	 * 		must have been created using {@code this.createPixel()}.
	 * 		The values of the elements of responses shall not
	 * 		depend on their initial values.  Note that this
	 * 		requirement implies that this method is responsible
	 * 		for initializing the values of the pixel.  In
	 * 		particular, the implementation of this method must
	 * 		not assume that the elements have been set to zero
	 * 		prior to invocation.
	 * @see {@link Box2#UNIT}, {@link IPixelFactory#createPixel()}.
	 */
	void shadeAt(Point2 p, Pixel pixel);

}
