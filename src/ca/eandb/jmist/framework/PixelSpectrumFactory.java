/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * A factory that creates extrapolated spectra from pixel representations.
 * @author Brad Kimmel
 */
public interface PixelSpectrumFactory {

	/**
	 * Creates a <code>Spectrum</code> extrapolated from the specified pixel.
	 * @param pixel The <code>Pixel</code> to extrapolate.
	 * @return The extrapolated <code>Spectrum</code>.
	 */
	Spectrum createSpectrum(double[] pixel);

}
