/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Observer;
import org.jmist.toolkit.Tuple;
import org.jmist.util.ArrayUtil;

/**
 * A fixed-wavelengths observer.
 * @author bkimmel
 */
public final class FixedObserver implements Observer {

	/**
	 * Initializes the wavelength to sample.
	 * @param wavelength The wavelength to sample (in meters).
	 */
	FixedObserver(double wavelength) {
		this.wavelengths = new Tuple(wavelength);
	}

	/**
	 * Initializes the wavelengths to sample.
	 * @param wavelengths The wavelengths to sample (in meters).
	 */
	FixedObserver(double[] wavelengths) {
		this.wavelengths = new Tuple(wavelengths);
	}

	/**
	 * Initializes the wavelengths to sample.
	 * @param wavelengths The wavelengths to sample (in meters).
	 */
	FixedObserver(Tuple wavelengths) {
		this.wavelengths = wavelengths;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Observer#sample()
	 */
	@Override
	public Sample sample() {
		double[] weights = ArrayUtil.setAll(new double[this.wavelengths.size()], 1.0);
		return new Sample(this.wavelengths, weights);
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
