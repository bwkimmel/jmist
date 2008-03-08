/**
 *
 */
package org.jmist.framework;

import org.jmist.util.ArrayUtil;

/**
 * An abstract <code>ProbabilityDensityFunction</code> that provides default
 * implementations for some methods.
 * @author bkimmel
 */
public abstract class AbstractProbabilityDensityFunction implements
		ProbabilityDensityFunction {

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProbabilityDensityFunction#sample()
	 */
	public double sample() {
		return this.warp(Math.random());
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProbabilityDensityFunction#evaluate(double[], double[])
	 */
	public double[] evaluate(double[] x, double[] results) {

		results = ArrayUtil.initialize(results, x.length);

		for (int i = 0; i < results.length; i++) {
			results[i] = this.evaluate(x[i]);
		}

		return results;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProbabilityDensityFunction#sample(double[])
	 */
	public double[] sample(double[] results) {

		for (int i = 0; i < results.length; i++) {
			results[i] = this.sample();
		}

		return results;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ProbabilityDensityFunction#warp(double[], double[])
	 */
	public double[] warp(double[] seeds, double[] results) {

		if (results == null) {
			results = new double[seeds.length];
		} else if (results.length != seeds.length) {
			throw new IllegalArgumentException("results.length != seeds.length");
		}

		for (int i = 0; i < results.length; i++) {
			results[i] = this.warp(seeds[i]);
		}

		return results;

	}

}
