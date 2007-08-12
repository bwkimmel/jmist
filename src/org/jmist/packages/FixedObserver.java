/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IObserver;
import org.jmist.framework.ISpectralEstimator;

/**
 * A fixed-wavelengths observer.
 * @author bkimmel
 */
public final class FixedObserver implements IObserver {

	/**
	 * Initializes the wavelength to sample.
	 * @param wavelength The wavelength to sample (in meters).
	 */
	FixedObserver(double wavelength) {
		this.wavelengths = new double[]{ wavelength };
	}

	/**
	 * Initializes the wavelengths to sample.
	 * @param wavelengths The wavelengths to sample (in meters).
	 */
	FixedObserver(double[] wavelengths) {
		this.wavelengths = (double[]) wavelengths.clone();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IObserver#acquire(org.jmist.framework.ISpectralEstimator, double[])
	 */
	public void acquire(ISpectralEstimator estimator, double[] responses) {
		assert(responses.length == wavelengths.length);
		estimator.sample(wavelengths, responses);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IObserver#getNumberOfComponents()
	 */
	public int getNumberOfComponents() {
		return wavelengths.length;
	}

	/** The wavelengths to sample. */
	private final double[] wavelengths;

}
