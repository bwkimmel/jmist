/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.AbstractSpectrum;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Tuple;

/**
 * A <code>Spectrum</code> that differs from another by a constant factor.
 * @author bkimmel
 */
public final class ScaledSpectrum extends AbstractSpectrum {

	/**
	 * Creates a new <code>ScaledSpectrum</code>.
	 * @param factor The factor by which to multiply the decorated
	 * 		<code>Spectrum</code>.
	 * @param inner The <code>Spectrum</code> to be multiplied by a constant
	 * 		factor.
	 */
	public ScaledSpectrum(double factor, Spectrum inner) {
		this.factor = factor;
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(double)
	 */
	@Override
	public double sample(double wavelength) {
		return this.factor * this.inner.sample(wavelength);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public double[] sample(Tuple wavelengths, double[] results)
			throws IllegalArgumentException {

		results = this.inner.sample(wavelengths, results);

		for (int i = 0; i < results.length; i++) {
			results[i] *= this.factor;
		}

		return results;

	}

	/** The factor by which to multiply the decorated <code>Spectrum</code>. */
	private final double factor;

	/** The <code>Spectrum</code> to be multiplied by a constant factor. */
	private final Spectrum inner;

}
