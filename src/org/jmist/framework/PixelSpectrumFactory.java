/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Pixel;

/**
 * A factory that creates extrapolated spectra from pixel representations.
 * @author bkimmel
 */
public interface PixelSpectrumFactory {

	/**
	 * Creates a <code>Spectrum</code> extrapolated from the specified pixel.
	 * @param pixel The <code>Pixel</code> to extrapolate.
	 * @return The extrapolated <code>Spectrum</code>.
	 */
	Spectrum createSpectrum(Pixel pixel);

}
