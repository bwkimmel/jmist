/**
 *
 */
package ca.eandb.jmist.packages.observer;

import ca.eandb.jmist.framework.Observer;
import ca.eandb.jmist.framework.ProbabilityDensityFunction;
import ca.eandb.jmist.framework.SpectralEstimator;
import ca.eandb.jmist.toolkit.Tuple;
import ca.eandb.jmist.util.ArrayUtil;
import ca.eandb.jmist.util.MathUtil;

/**
 * An <code>Observer</code> that uses <code>ProbabilityDensityFunction</code>s
 * to describe the spectral responses for each channel.
 * @author Brad Kimmel
 */
public class IntegratingObserver implements Observer {

	/**
	 * Creates a new <code>IntegratingObserver</code>.
	 * @param pdf The array of <code>ProbabilityDensityFunction</code>s for
	 * 		each channel.
	 * @param scale The factor by which to scale the results from each channel.
	 * @param samplesPerComponent The number of samples to measure for each
	 * 		component.
	 */
	public IntegratingObserver(ProbabilityDensityFunction[] pdf, double[] scale, int samplesPerComponent) {
		if (pdf.length != scale.length) {
			throw new IllegalArgumentException("pdf.length != scale.length");
		}
		this.pdf = pdf;
		this.scale = scale.clone();
		this.samplesPerComponent = samplesPerComponent;
	}

	/**
	 * Creates a new <code>IntegratingObserver</code>.
	 * @param pdf The array of <code>ProbabilityDensityFunction</code>s for
	 * 		each channel.
	 * @param scale The factor by which to scale the results from each channel.
	 */
	public IntegratingObserver(ProbabilityDensityFunction[] pdf, double[] scale) {
		this(pdf, scale, 1);
	}

	/**
	 * Creates a new <code>IntegratingObserver</code>.
	 * @param pdf The array of <code>ProbabilityDensityFunction</code>s for
	 * 		each channel.
	 * @param samplesPerComponent The number of samples to measure for each
	 * 		component.
	 */
	public IntegratingObserver(ProbabilityDensityFunction[] pdf, int samplesPerComponent) {
		this(pdf, ArrayUtil.setAll(new double[pdf.length], 1.0), samplesPerComponent);
	}

	/**
	 * Creates a new <code>IntegratingObserver</code>.
	 * @param pdf The array of <code>ProbabilityDensityFunction</code>s for
	 * 		each channel.
	 */
	public IntegratingObserver(ProbabilityDensityFunction[] pdf) {
		this(pdf, 1);
	}

	/**
	 * Creates a new single channel <code>IntegratingObserver</code>.
	 * @param pdf The <code>ProbabilityDensityFunction</code>s for the channel.
	 * @param scale The factor by which to scale the results from the channel.
	 * @param samples The number of samples to measure.
	 */
	public IntegratingObserver(ProbabilityDensityFunction pdf, double scale, int samples) {
		this(new ProbabilityDensityFunction[]{ pdf }, new double[]{ scale }, samples);
	}

	/**
	 * Creates a new single channel <code>IntegratingObserver</code>.
	 * @param pdf The <code>ProbabilityDensityFunction</code>s for the channel.
	 * @param scale The factor by which to scale the results from the channel.
	 */
	public IntegratingObserver(ProbabilityDensityFunction pdf, double scale) {
		this(pdf, scale, 1);
	}

	/**
	 * Creates a new single channel <code>IntegratingObserver</code>.
	 * @param pdf The <code>ProbabilityDensityFunction</code>s for the channel.
	 * @param samples The number of samples to measure.
	 */
	public IntegratingObserver(ProbabilityDensityFunction pdf, int samples) {
		this(pdf, 1.0, samples);
	}

	/**
	 * Creates a new single channel <code>IntegratingObserver</code>.
	 * @param pdf The <code>ProbabilityDensityFunction</code>s for the channel.
	 */
	public IntegratingObserver(ProbabilityDensityFunction pdf) {
		this(pdf, 1);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Observer#acquire(ca.eandb.jmist.framework.SpectralEstimator, double[])
	 */
	public double[] acquire(SpectralEstimator estimator, double[] observation) {
		if (this.samplesPerComponent == 1) {
			return this.acquireOne(estimator, observation);
		} else {
			return this.acquireMultiple(estimator, observation);
		}
	}

	private double[] acquireOne(SpectralEstimator estimator, double[] observation) {
		double[] wavelengths = new double[pdf.length];
		for (int i = 0; i < pdf.length; i++) {
			wavelengths[i] = this.pdf[i].sample();
		}
		observation = estimator.sample(new Tuple(wavelengths), observation);
		return MathUtil.modulate(observation, this.scale);
	}

	private double[] acquireMultiple(SpectralEstimator estimator, double[] observation) {
		double[] wavelengths = new double[pdf.length * samplesPerComponent];
		for (int i = 0, n = 0; i < pdf.length; i++) {
			for (int j = 0; j < samplesPerComponent; j++, n++) {
				wavelengths[n] = this.pdf[i].sample();
			}
		}
		double[] estimate = estimator.sample(new Tuple(wavelengths), new double[wavelengths.length]);
		observation = ArrayUtil.initialize(observation, pdf.length);
		for (int i = 0, n = 0; i < pdf.length; i++) {
			for (int j = 0; j < samplesPerComponent; j++, n++) {
				observation[i] += estimate[n];
			}
			observation[i] /= (double) samplesPerComponent;
		}
		return MathUtil.modulate(observation, this.scale);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Observer#getNumberOfComponents()
	 */
	public int getNumberOfComponents() {
		return pdf.length;
	}

	/**
	 * The array of <code>ProbabilityDensityFunction</code>s for each channel.
	 */
	private final ProbabilityDensityFunction[] pdf;

	/** The factor by which to scale the results from each channel. */
	private final double[] scale;

	/** The number of samples to measure for each component. */
	private final int samplesPerComponent;

}
