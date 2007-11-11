/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Tuple;

/**
 * A <code>Spectrum</code> that is the sum of other spectra.
 * @author bkimmel
 */
public final class SumSpectrum extends CompositeSpectrum {

	/* (non-Javadoc)
	 * @see org.jmist.packages.CompositeSpectrum#addChild(org.jmist.framework.Spectrum)
	 */
	@Override
	public SumSpectrum addChild(Spectrum child) {
		if (child instanceof SumSpectrum) {
			SumSpectrum sum = (SumSpectrum) child;
			for (Spectrum grandChild : sum.children()) {
				this.addChild(grandChild);
			}
		} else {
			super.addChild(child);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractSpectrum#sample(double)
	 */
	@Override
	public double sample(double wavelength) {

		double sum = 0.0;

		for (Spectrum component : this.children()) {
			sum += component.sample(wavelength);
		}

		return sum;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Spectrum#modulate(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public void modulate(Tuple wavelengths, double[] samples)
			throws IllegalArgumentException {

		double[] sum = this.sample(wavelengths, null);

		for (int i = 0; i < samples.length; i++) {
			samples[i] *= sum[i];
		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Spectrum#sample(org.jmist.toolkit.Tuple, double[])
	 */
	@Override
	public double[] sample(Tuple wavelengths, double[] results)
			throws IllegalArgumentException {

		if (results == null) {
			results = new double[wavelengths.size()];
		} else if (results.length == wavelengths.size()) {
			for (int i = 0; i < results.length; i++) {
				results[i] = 0.0;
			}
		} else {
			throw new IllegalArgumentException("results.length != wavelengths.size()");
		}

		if (!this.children().isEmpty()) {

			double[] summands = new double[results.length];

			for (Spectrum child : this.children()) {
				summands = child.sample(wavelengths, summands);
				for (int i = 0; i < results.length; i++) {
					results[i] += summands[i];
				}
			}

		}

		return results;

	}


}