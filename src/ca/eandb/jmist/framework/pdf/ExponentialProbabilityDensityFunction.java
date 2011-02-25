/**
 * 
 */
package ca.eandb.jmist.framework.pdf;

/**
 * Represents the probability density function for an exponential random
 * variable.
 * 
 * @author Brad Kimmel
 */
public final class ExponentialProbabilityDensityFunction extends
		AbstractProbabilityDensityFunction {

	/** Serialization version ID. */
	private static final long serialVersionUID = 2821787249029006451L;
	
	/** The parameter for an exponential random variable. */
	private final double lambda;
	
	/**
	 * Creates a new <code>ExponentialProbabilityDensityFunction</code>.
	 * @param lambda The parameter for the exponential random variable.  The
	 * 		distribution will be <code>p(x) = &lambda;e<sup>-&lambda;x</sup></code>.
	 * 		The expected value is <code>E[X] = 1/&lambda;</code>.
	 */
	public ExponentialProbabilityDensityFunction(double lambda) {
		this.lambda = lambda;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#warp(double)
	 */
	@Override
	public double warp(double seed) {
		return -Math.log(1.0 - seed) / lambda; 
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#evaluate(double)
	 */
	@Override
	public double evaluate(double x) {
		return x > 0.0 ? lambda * Math.exp(-lambda * x) : 0.0; 
	}

}
