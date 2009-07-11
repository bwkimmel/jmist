/**
 *
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
