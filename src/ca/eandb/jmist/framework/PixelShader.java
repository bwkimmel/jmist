/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
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
	 * @return An <code>Color</code> containing of the pixel responses.
	 * @see Box2#UNIT
	 */
	Color shadePixel(Box2 bounds);

}
