/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface PixelShader extends PixelFactory {

	/**
	 * Computes an estimate of the mean channel responses at
	 * the specified pixel.
	 * @param pixel The bounds of the pixel in normalized
	 * 		device coordinates (must be bounded by
	 * 		{@code Box2.UNIT}).
	 * @return An array of the mean responses for each
	 * 		channel.
	 * @see Box2#UNIT
	 */
	void shadePixel(Box2 bounds, Pixel pixel);

}
