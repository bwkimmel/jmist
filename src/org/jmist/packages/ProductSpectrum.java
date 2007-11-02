/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.AbstractSpectrum;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Tuple;

/**
 * A <code>Spectrum</code> that is the product of two other spectra.
 * @author bkimmel
 */
public final class ProductSpectrum extends AbstractSpectrum {

	/**
	 * Creates a new <code>ProductSpectrum</code>.
	 * @param a The first <code>Spectrum</code>.
	 * @param b The second <code>Spectrum</code>.
	 */
	public ProductSpectrum(Spectrum a, Spectrum b) {
		this.a = a;
		this.b = b;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(double)
	 */
	@Override
	public double sample(double wavelength) {
		return this.a.sample(wavelength) * this.b.sample(wavelength);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public double[] sample(Tuple wavelengths, double[] results)
			throws IllegalArgumentException {

		results = this.a.sample(wavelengths, results);
		double[] factors = this.b.sample(wavelengths, null);

		assert(results.length == factors.length);

		for (int i = 0; i < results.length; i++) {
			results[i] *= factors[i];
		}

		return results;

	}

	/** The first <code>Spectrum</code>. */
	private final Spectrum a;

	/** The second <code>Spectrum</code>. */
	private final Spectrum b;

}
