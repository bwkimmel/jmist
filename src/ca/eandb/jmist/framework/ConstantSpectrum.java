/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;


/**
 * A <code>Spectrum</code> that has the same value at all wavelengths.  This
 * class is immutable.
 * @author bkimmel
 */
public class ConstantSpectrum extends AbstractSpectrum implements Serializable {

	/**
	 * Creates a new <code>ConstantSpectrum</code>.
	 * @param value The value of the <code>Spectrum</code> at all wavelengths.
	 */
	public ConstantSpectrum(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AbstractSpectrum#sample(double)
	 */
	@Override
	public double sample(double wavelength) {
		return this.value;
	}

	/** The value of this <code>Spectrum</code> at all wavelengths. */
	private final double value;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 8993721217448172058L;

}
