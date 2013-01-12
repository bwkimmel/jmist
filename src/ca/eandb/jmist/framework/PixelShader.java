/**
 *
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
