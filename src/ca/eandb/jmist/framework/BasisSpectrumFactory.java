/**
 *
 */
package ca.eandb.jmist.framework;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.function.ScaledFunction1;
import ca.eandb.jmist.framework.function.SumFunction1;

/**
 * A factory for creating spectra from a vector space given the basis spectra.
 * @author Brad Kimmel
 */
public final class BasisSpectrumFactory {

	/**
	 * Adds a basis <code>Function1</code>.
	 * @param spectrum The basis <code>Function1</code> to add.
	 * @return A reference to this <code>BasisSpectrumFactory</code> so that
	 * 		calls to this method may be chained.
	 */
	public BasisSpectrumFactory addBasisSpectrum(Function1 spectrum) {
		this.basis.add(spectrum);
		return this;
	}

	/**
	 * Creates a <code>Function1</code> at the coordinates in the vector space
	 * represented by the basis spectra registered with this
	 * <code>BasisSpectrumFactory</code>.
	 * @param coefficients The coefficients to multiply each of the basis
	 * 		spectra by.
	 * @return The <code>Function1</code> at the specified coordinates.
	 */
	public Function1 create(double... coefficients) {

		SumFunction1	sum		= new SumFunction1();
		int				n		= Math.min(coefficients.length, this.basis.size());

		for (int i = 0; i < n; i++) {
			sum.addChild(new ScaledFunction1(coefficients[i], this.basis.get(i)));
		}

		return sum;

	}

	/** A <code>List</code> of the basis spectra. */
	private final List<Function1> basis = new ArrayList<Function1>();

}
