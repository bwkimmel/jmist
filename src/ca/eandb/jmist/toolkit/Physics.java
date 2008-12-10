/**
 *
 */
package ca.eandb.jmist.toolkit;

/**
 * Provides constants and utility methods for physics.
 * @author Brad Kimmel
 */
public final class Physics {

	/** The speed of light in a vacuum (in meters per second). */
	public static final double SPEED_OF_LIGHT = 299452798.0;

	/** Planck's constant (in Joule-seconds). */
	public static final double PLANCK_CONSTANT = 6.62606876e-34;

	/** Boltzmann's constant (in Joules per Kelvin). */
	public static final double BOLTZMANN_CONSTANT = 1.3806503e-23;

	/** Stefan-Boltzmann constant (in Watts per meter squared per Kelvin^4). */
	public static final double STEFAN_BOLTZMANN_CONSTANT = 5.670400e-8;

	/**
	 * This class contains only static utility methods,
	 * and therefore should not be creatable.
	 */
	private Physics() {}

}
