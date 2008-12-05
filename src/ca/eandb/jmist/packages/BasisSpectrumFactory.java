/**
 *
 */
package ca.eandb.jmist.packages;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Spectrum;
import ca.eandb.jmist.packages.spectrum.ScaledSpectrum;
import ca.eandb.jmist.packages.spectrum.SumSpectrum;

/**
 * A factory for creating spectra from a vector space given the basis spectra.
 * @author bkimmel
 */
public final class BasisSpectrumFactory {

	/**
	 * Adds a basis <code>Spectrum</code>.
	 * @param spectrum The basis <code>Spectrum</code> to add.
	 * @return A reference to this <code>BasisSpectrumFactory</code> so that
	 * 		calls to this method may be chained.
	 */
	public BasisSpectrumFactory addBasisSpectrum(Spectrum spectrum) {
		this.basis.add(spectrum);
		return this;
	}

	/**
	 * Creates a <code>Spectrum</code> at the coordinates in the vector space
	 * represented by the basis spectra registered with this
	 * <code>BasisSpectrumFactory</code>.
	 * @param coefficients The coefficients to multiply each of the basis
	 * 		spectra by.
	 * @return The <code>Spectrum</code> at the specified coordinates.
	 */
	public Spectrum create(double... coefficients) {

		SumSpectrum		sum		= new SumSpectrum();
		int				n		= Math.min(coefficients.length, this.basis.size());

		for (int i = 0; i < n; i++) {
			sum.addChild(new ScaledSpectrum(coefficients[i], this.basis.get(i)));
		}

		return sum;

	}

	/** A <code>List</code> of the basis spectra. */
	private final List<Spectrum> basis = new ArrayList<Spectrum>();

}
