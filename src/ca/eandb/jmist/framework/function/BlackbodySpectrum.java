/**
 *
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Physics;

/**
 * A <code>Function1</code> representing a blackbody emission spectrum.
 * @author Brad Kimmel
 */
public final class BlackbodySpectrum implements Function1 {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 5120826959435651065L;

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
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	public double evaluate(double wavelength) {

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
