/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Tuple;

/**
 * A base implementation of <code>Spectrum</code> that provides a default
 * implementation for sampling at multiple wavelengths.
 * @author bkimmel
 */
public abstract class AbstractSpectrum implements Spectrum {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Spectrum#sample(double)
	 */
	@Override
	public abstract double sample(double wavelength);

	/* (non-Javadoc)
	 * @see org.jmist.framework.Spectrum#sample(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public double[] sample(Tuple wavelengths, double[] results) throws IllegalArgumentException {

		/* If no results array was passed in, then make one. */
		if (results == null) {
			results = new double[wavelengths.size()];
		} else if (results.length != wavelengths.size()) {
			throw new IllegalArgumentException("results.length != wavelengths.size()");
		}

		/* Call the overload of this method to sample a single wavelength
		 * once for each wavelength in the tuple.
		 */
		for (int i = 0; i < results.length; i++) {
			results[i] = this.sample(wavelengths.at(i));
		}

		return results;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Spectrum#modulate(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public void modulate(Tuple wavelengths, double[] samples)
			throws IllegalArgumentException {

		/* Make sure the samples to modulate were provided and that the
		 * number of samples matches the number of wavelengths.
		 */
		if (samples == null || samples.length != wavelengths.size()) {
			throw new IllegalArgumentException("samples == null || samples.length != wavelengths.size()");
		}

		/* Sample the spectrum to determine the factor for each wavelength. */
		for (int i = 0; i < samples.length; i++) {
			samples[i] *= this.sample(wavelengths.at(i));
		}

	}

}
