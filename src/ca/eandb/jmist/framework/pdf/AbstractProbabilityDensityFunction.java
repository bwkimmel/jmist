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
package ca.eandb.jmist.framework.pdf;

import ca.eandb.jmist.framework.ProbabilityDensityFunction;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * An abstract <code>ProbabilityDensityFunction</code> that provides default
 * implementations for some methods.
 * @author Brad Kimmel
 */
public abstract class AbstractProbabilityDensityFunction implements
		ProbabilityDensityFunction {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#sample(ca.eandb.jmist.framework.Random)
	 */
	public double sample(Random random) {
		return this.warp(RandomUtil.canonical(random));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#evaluate(double[], double[])
	 */
	public double[] evaluate(double[] x, double[] results) {

		results = ArrayUtil.initialize(results, x.length);

		for (int i = 0; i < results.length; i++) {
			results[i] = this.evaluate(x[i]);
		}

		return results;

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#sample(ca.eandb.jmist.framework.Random, double[])
	 */
	public double[] sample(Random random, double[] results) {

		for (int i = 0; i < results.length; i++) {
			results[i] = this.sample(random);
		}

		return results;

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#warp(double[], double[])
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
