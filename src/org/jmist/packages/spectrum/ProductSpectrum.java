/**
 *
 */
package org.jmist.packages.spectrum;

import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Tuple;

/**
 * A <code>Spectrum</code> that is the product of other spectra.
 * @author bkimmel
 */
public final class ProductSpectrum extends CompositeSpectrum {

	/* (non-Javadoc)
	 * @see org.jmist.packages.spectrum.CompositeSpectrum#addChild(org.jmist.framework.Spectrum)
	 */
	@Override
	public ProductSpectrum addChild(Spectrum child) {
		if (child instanceof ProductSpectrum) {
			ProductSpectrum product = (ProductSpectrum) child;
			for (Spectrum grandChild : product.children()) {
				this.addChild(grandChild);
			}
		} else {
			super.addChild(child);
		}
		return this;
	}

	/**
	 * Creates a new <code>ProductSpectrum</code>.
	 */
	public ProductSpectrum() {
		/* do nothing */
	}

	/**
	 * Creates a new <code>ProductSpectrum</code>.
	 * @param a The first <code>Spectrum</code>.
	 * @param b The second <code>Spectrum</code>.
	 */
	public ProductSpectrum(Spectrum a, Spectrum b) {
		this.addChild(a).addChild(b);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(double)
	 */
	@Override
	public double sample(double wavelength) {

		double product = 1.0;

		for (Spectrum child : this.children()) {
			product *= child.sample(wavelength);
		}

		return product;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public double[] sample(Tuple wavelengths, double[] results)
			throws IllegalArgumentException {

		results = Spectrum.ONE.sample(wavelengths, results);

		for (Spectrum child : this.children()) {
			child.modulate(wavelengths, results);
		}

		return results;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#modulate(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public void modulate(Tuple wavelengths, double[] samples)
			throws IllegalArgumentException {

		for (Spectrum child : this.children()) {
			child.modulate(wavelengths, samples);
		}

	}

}
