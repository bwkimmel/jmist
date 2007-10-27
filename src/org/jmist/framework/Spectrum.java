/**
 *
 */
package org.jmist.framework;

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
	
}
