/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Box2;

/**
 * @author Brad Kimmel
 *
 */
public interface PixelShader {

	/**
	 * Computes an estimate of the mean channel responses at
	 * the specified pixel.
	 * @param bounds The bounds of the pixel in normalized
	 * 		device coordinates (must be bounded by
	 * 		{@code Box2.UNIT}).
	 * @param pixel The array to populate with the mean responses for each
	 * 		channel (if null, the array will be created by this method).
	 * @return An array containing of the mean responses for each channel.
	 * @see Box2#UNIT
	 */
	double[] shadePixel(Box2 bounds, double[] pixel);

}
