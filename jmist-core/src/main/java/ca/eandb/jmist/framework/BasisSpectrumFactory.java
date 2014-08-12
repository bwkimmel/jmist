/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.function.ScaledFunction1;
import ca.eandb.jmist.framework.function.SumFunction1;

/**
 * A factory for creating spectra from a vector space given the basis spectra.
 * @author Brad Kimmel
 */
public final class BasisSpectrumFactory implements Serializable {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 8689123372520021846L;

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
