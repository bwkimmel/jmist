/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IPixelShader {

	/**
	 * Computes the mean spectral radiance impinging
	 * on the specified pixel.
	 * @param pixel The bounds of the pixel in normalized
	 * 		device coordinates (must be bounded by
	 * 		{@code Box2.UNIT}).
	 * @param wavelength The wavelength (in meters) at which
	 * 		to evaluate the mean spectral radiance.
	 * @return The mean spectral radiance (in
	 * 		W&middot;sr<sup>-1</sup>&middot;m<sup>-3</sup>).
	 * @see Box2#UNIT
	 */
	double shadePixel(Box2 pixel, double wavelength);

}
