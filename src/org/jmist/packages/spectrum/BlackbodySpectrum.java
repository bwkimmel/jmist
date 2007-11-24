/**
 *
 */
package org.jmist.packages.spectrum;

import org.jmist.framework.AbstractSpectrum;
import org.jmist.toolkit.Physics;
import org.jmist.util.MathUtil;

/**
 * A blackbody emission <code>Spectrum</code>.
 * @author bkimmel
 */
public final class BlackbodySpectrum extends AbstractSpectrum {

	/**
	 * Creates a new <code>BlackbodySpectrum</code>.
	 * @param temperature The temperature of the black body to simulate (in
	 * 		Kelvin).
	 */
	public BlackbodySpectrum(double temperature) {
		super();
		this.temperature = temperature;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(double)
	 */
	@Override
	public double sample(double wavelength) {

		if (MathUtil.isZero(temperature)) {
			return 0.0;
		}

		double a = (2.0 * Math.PI * Physics.PLANCK_CONSTANT
				* Physics.SPEED_OF_LIGHT * Physics.SPEED_OF_LIGHT)
				/ Math.pow(wavelength, 5.0);

		double b = (1.0 / (Math.exp((Physics.PLANCK_CONSTANT * Physics.SPEED_OF_LIGHT)
				/ (wavelength * Physics.BOLTZMANN_CONSTANT * temperature)) - 1.0));

		return a * b;

	}

	/** The temperature of the black body to simulate (in Kelvin). */
	private final double temperature;

}
