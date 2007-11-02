/**
 *
 */
package org.jmist.framework;


/**
 * A <code>Spectrum</code> that has the same value at all wavelengths.  This
 * class is immutable.
 * @author bkimmel
 */
public final class ConstantSpectrum extends AbstractSpectrum {

	/**
	 * Creates a new <code>ConstantSpectrum</code>.
	 * @param value The value of the <code>Spectrum</code> at all wavelengths.
	 */
	public ConstantSpectrum(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(double)
	 */
	@Override
	public double sample(double wavelength) {
		return this.value;
	}

	/** The value of this <code>Spectrum</code> at all wavelengths. */
	private final double value;

}
