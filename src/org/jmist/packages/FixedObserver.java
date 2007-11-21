/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Observer;
import org.jmist.framework.SpectralEstimator;
import org.jmist.toolkit.Tuple;

/**
 * A fixed-wavelengths observer.
 * @author bkimmel
 */
public final class FixedObserver implements Observer {

	/**
	 * Initializes the wavelength to sample.
	 * @param wavelength The wavelength to sample (in meters).
	 */
	public FixedObserver(double wavelength) {
		this.wavelengths = new Tuple(wavelength);
	}

	/**
	 * Initializes the wavelengths to sample.
	 * @param wavelengths The wavelengths to sample (in meters).
	 */
	public FixedObserver(double[] wavelengths) {
		this.wavelengths = new Tuple(wavelengths);
	}

	/**
	 * Initializes the wavelengths to sample.
	 * @param wavelengths The wavelengths to sample (in meters).
	 */
	public FixedObserver(Tuple wavelengths) {
		this.wavelengths = wavelengths;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Observer#acquire(org.jmist.framework.SpectralEstimator, double[])
	 */
	@Override
	public double[] acquire(SpectralEstimator estimator, double[] observation) {
		return estimator.sample(this.wavelengths, observation);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Observer#getNumberOfComponents()
	 */
	public int getNumberOfComponents() {
		return wavelengths.size();
	}

	/** The wavelengths to sample. */
	private final Tuple wavelengths;

}
