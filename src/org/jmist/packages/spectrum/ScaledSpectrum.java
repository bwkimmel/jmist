/**
 *
 */
package org.jmist.packages.spectrum;

import org.jmist.framework.AbstractSpectrum;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Tuple;
import org.jmist.util.MathUtil;

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

		/* Combine successive ScaledSpectrum instances into one. */
		while (inner instanceof ScaledSpectrum) {
			ScaledSpectrum spectrum = (ScaledSpectrum) inner;
			factor *= spectrum.factor;
			inner = spectrum.inner;
		}

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
		return MathUtil.scale(results, this.factor);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#modulate(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public void modulate(Tuple wavelengths, double[] samples)
			throws IllegalArgumentException {

		this.inner.modulate(wavelengths, samples);
		MathUtil.scale(samples, this.factor);

	}

	/** The factor by which to multiply the decorated <code>Spectrum</code>. */
	private final double factor;

	/** The <code>Spectrum</code> to be multiplied by a constant factor. */
	private final Spectrum inner;

}
