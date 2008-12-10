/**
 *
 */
package ca.eandb.jmist.packages.observer;

import ca.eandb.jmist.framework.Observer;
import ca.eandb.jmist.framework.SpectralEstimator;
import ca.eandb.jmist.toolkit.Tuple;

/**
 * A fixed-wavelengths observer.
 * @author Brad Kimmel
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
	 * @see ca.eandb.jmist.framework.Observer#acquire(ca.eandb.jmist.framework.SpectralEstimator, double[])
	 */
	public double[] acquire(SpectralEstimator estimator, double[] observation) {
		return estimator.sample(this.wavelengths, observation);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Observer#getNumberOfComponents()
	 */
	public int getNumberOfComponents() {
		return wavelengths.size();
	}

	/** The wavelengths to sample. */
	private final Tuple wavelengths;

}
