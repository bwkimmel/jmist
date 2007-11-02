/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Tuple;

/**
 * @author bkimmel
 *
 */
public interface Spectrum {

	/**
	 * The value of the spectrum at the specified
	 * wavelength.
	 * @param wavelength The wavelength (in meters) at
	 * 		which to evaluate the spectrum
	 * @return The value of the spectrum (units defined
	 * 		by the concrete class).
	 */
	double sample(double wavelength);

	/**
	 * Samples the spectrum at the specified wavelengths.
	 * @param wavelengths The <code>Tuple</code> of wavelengths (in meters) at
	 * 		which to sample this <code>Spectrum</code>.
	 * @param results An optional array to write the results to (may be null,
	 * 		in which case one will be created).  If not null, the length of the
	 * 		array must be equal to that of <code>wavelengths</code>.
	 * @return An array containing the results of the sample.
	 * @throws IllegalArgumentException if {@code results.length !=
	 * 		wavelengths.size()}.
	 */
	double[] sample(Tuple wavelengths, double[] results) throws IllegalArgumentException;

	void modulate(Tuple wavelengths, double[] samples) throws IllegalArgumentException;

	/**
	 * A <code>Spectrum</code> that evaluates to zero at every wavelength.
	 */
	public static final Spectrum ZERO = new ConstantSpectrum(0.0) {

		/* (non-Javadoc)
		 * @see org.jmist.framework.Spectrum#sample(org.jmist.toolkit.Tuple, double[])
		 */
		@Override
		public double[] sample(Tuple wavelengths, double[] results)
				throws IllegalArgumentException {

			if (results == null) {
				return new double[wavelengths.size()];
			}

			return super.sample(wavelengths, results);

		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Spectrum#modulate(org.jmist.toolkit.Tuple, double[])
		 */
		@Override
		public void modulate(Tuple wavelengths, double[] samples)
				throws IllegalArgumentException {

			this.sample(wavelengths, samples);

		}

	};

	/**
	 * A <code>Spectrum</code> whose value is <code>1.0</code>
	 * at all wavelengths.
	 */
	public static final Spectrum ONE = new ConstantSpectrum(1.0) {

		/* (non-Javadoc)
		 * @see org.jmist.framework.AbstractSpectrum#modulate(org.jmist.toolkit.Tuple, double[])
		 */
		@Override
		public void modulate(Tuple wavelengths, double[] samples) {
			/* nothing to do */
		}

	};

	/**
	 * A <code>Spectrum</code> whose value is <code>POSITIVE_INFINITY</code>
	 * at all wavelengths.
	 */
	public static final Spectrum POSITIVE_INFINITY = new ConstantSpectrum(Double.POSITIVE_INFINITY);

	/**
	 * A <code>Spectrum</code> whose value is <code>NEGATIVE_INFINITY</code>
	 * at all wavelengths.
	 */
	public static final Spectrum NEGATIVE_INFINITY = new ConstantSpectrum(Double.NEGATIVE_INFINITY);

}
