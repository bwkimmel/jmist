/**
 *
 */
package ca.eandb.jmist.packages.spectrum;

import ca.eandb.jmist.framework.AbstractSpectrum;
import ca.eandb.jmist.util.MathUtil;

/**
 * A piecewise linear <code>Spectrum</code>.
 * @author bkimmel
 */
public final class PiecewiseLinearSpectrum extends AbstractSpectrum {

	/**
	 * Creates a new <code>PiecewiseLinearSpectrum</code>.
	 * @param wavelengths The array of wavelengths (in meters).
	 * @param values The array of values corresponding to the wavelengths.
	 */
	public PiecewiseLinearSpectrum(double[] wavelengths, double[] values) {
		this.wavelengths = wavelengths.clone();
		this.values = values.clone();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractSpectrum#sample(double)
	 */
	@Override
	public double sample(double wavelength) {
		return MathUtil.interpolate(this.wavelengths, this.values, wavelength);
	}

	/** The array of wavelengths (in meters). */
	private final double[] wavelengths;

	/** The array of values corresponding to the wavelengths. */
	private final double[] values;

}
