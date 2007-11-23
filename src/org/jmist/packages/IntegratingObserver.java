/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Observer;
import org.jmist.framework.ProbabilityDensityFunction;
import org.jmist.framework.SpectralEstimator;
import org.jmist.toolkit.Tuple;
import org.jmist.util.ArrayUtil;
import org.jmist.util.MathUtil;

/**
 * An <code>Observer</code> that uses <code>ProbabilityDensityFunction</code>s
 * to describe the spectral responses for each channel.
 * @author bkimmel
 */
public class IntegratingObserver implements Observer {

	/**
	 * Creates a new <code>IntegratingObserver</code>.
	 * @param pdf The array of <code>ProbabilityDensityFunction</code>s for
	 * 		each channel.
	 * @param scale The factor by which to scale the results from each channel.
	 */
	public IntegratingObserver(ProbabilityDensityFunction[] pdf, double[] scale) {
		if (pdf.length != scale.length) {
			throw new IllegalArgumentException("pdf.length != scale.length");
		}
		this.pdf = pdf.clone();
		this.scale = scale.clone();
	}

	/**
	 * Creates a new <code>IntegratingObserver</code>.
	 * @param pdf The array of <code>ProbabilityDensityFunction</code>s for
	 * 		each channel.
	 */
	public IntegratingObserver(ProbabilityDensityFunction[] pdf) {
		this.pdf = pdf.clone();
		this.scale = ArrayUtil.setAll(new double[pdf.length], 1.0);
	}

	/**
	 * Creates a new single channel <code>IntegratingObserver</code>.
	 * @param pdf The <code>ProbabilityDensityFunction</code>s for the channel.
	 * @param scale The factor by which to scale the results from the channel.
	 */
	public IntegratingObserver(ProbabilityDensityFunction pdf, double scale) {
		this.pdf = new ProbabilityDensityFunction[]{ pdf };
		this.scale = new double[]{ scale };
	}

	/**
	 * Creates a new single channel <code>IntegratingObserver</code>.
	 * @param pdf The <code>ProbabilityDensityFunction</code>s for the channel.
	 */
	public IntegratingObserver(ProbabilityDensityFunction pdf) {
		this.pdf = new ProbabilityDensityFunction[]{ pdf };
		this.scale = new double[]{ 1.0 };
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Observer#acquire(org.jmist.framework.SpectralEstimator, double[])
	 */
	@Override
	public double[] acquire(SpectralEstimator estimator, double[] observation) {
		double[] wavelengths = new double[pdf.length];
		for (int i = 0; i < pdf.length; i++) {
			wavelengths[i] = this.pdf[i].sample();
		}
		observation = estimator.sample(new Tuple(wavelengths), observation);
		return MathUtil.modulate(observation, this.scale);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Observer#getNumberOfComponents()
	 */
	@Override
	public int getNumberOfComponents() {
		return pdf.length;
	}

	/**
	 * The array of <code>ProbabilityDensityFunction</code>s for each channel.
	 */
	private final ProbabilityDensityFunction[] pdf;

	/** The factor by which to scale the results from each channel. */
	private final double[] scale;

}
