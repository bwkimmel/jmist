/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.spectrum.ConstantSpectrum;
import ca.eandb.jmist.math.Tuple;

/**
 * @author Brad Kimmel
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

	/**
	 * Multiplies the provided samples by a sample of the spectrum at the
	 * specified wavelengths.  Equivalent to multiplying <code>samples</code>
	 * by <code>this.sample(wavelengths, null)</code> component-wise.
	 * @param wavelengths The <code>Tuple</code> of wavelengths at which to
	 * 		sample this <code>Spectrum</code>.
	 * @param samples The array of samples to modulate (must not be
	 * 		<code>null</code> and must have <code>samples.length ==
	 * 		wavelengths.size()</code>.
	 * @throws IllegalArgumentException if <code>samples</code> is
	 * 		<code>null</code> or if the length of <code>samples</code> does
	 * 		not match that of <code>wavelengths</code>.
	 */
	void modulate(Tuple wavelengths, double[] samples) throws IllegalArgumentException;

	/**
	 * A <code>Spectrum</code> that evaluates to zero at every wavelength.
	 */
	public static final Spectrum ZERO = new ConstantSpectrum(0.0) {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Spectrum#sample(ca.eandb.jmist.toolkit.Tuple, double[])
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
		 * @see ca.eandb.jmist.framework.Spectrum#modulate(ca.eandb.jmist.toolkit.Tuple, double[])
		 */
		@Override
		public void modulate(Tuple wavelengths, double[] samples)
				throws IllegalArgumentException {

			this.sample(wavelengths, samples);

		}

		/**
		 * Serialization version ID.
		 */
		private static final long serialVersionUID = -5831651385031349750L;

	};

	/**
	 * A <code>Spectrum</code> whose value is <code>1.0</code>
	 * at all wavelengths.
	 */
	public static final Spectrum ONE = new ConstantSpectrum(1.0) {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.AbstractSpectrum#modulate(ca.eandb.jmist.toolkit.Tuple, double[])
		 */
		@Override
		public void modulate(Tuple wavelengths, double[] samples) throws IllegalArgumentException {

			if (samples == null || samples.length != wavelengths.size()) {
				throw new IllegalArgumentException("samples == null || samples.length != wavelengths.size()");
			}

			/* nothing to do */

		}

		/**
		 * Serialization version ID.
		 */
		private static final long serialVersionUID = -6865514731221019215L;

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
