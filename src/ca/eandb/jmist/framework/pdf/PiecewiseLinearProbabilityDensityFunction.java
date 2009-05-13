/**
 *
 */
package ca.eandb.jmist.framework.pdf;

import java.util.Arrays;

import ca.eandb.jmist.math.MathUtil;

/**
 * A <code>ProbabilityDensityFunction</code> specified by a piecewise-linear
 * curve, described by an array of domain points and the corresponding array of
 * the PDF values.
 * @author Brad Kimmel
 */
public final class PiecewiseLinearProbabilityDensityFunction extends
		AbstractProbabilityDensityFunction {

	/**
	 * Creates a new <code>PiecewiseLinearProbabilityDensityFunction</code>.
	 * @param x The domain points (must be sorted in ascending order).  Values
	 * 		less than <code>x[0]</code> or greater than
	 * 		<code>x[x.length - 1]</code> will have a probability density of
	 * 		zero.
	 * @param pdf The values of the probability density function corresponding
	 * 		to the domain points <code>x[i]</code> (must be the same length as
	 * 		<code>x</code> and all values must be non-negative).  These values
	 * 		will be normalized to integrate to one.
	 * @throws IllegalArgumentException if <code>x.length != pdf.length</code>.
	 * @throws IllegalArgumentException if <code>x</code> is not sorted in
	 * 		ascending order.
	 * @throws IllegalArgumentException if <code>pdf</code> has negative
	 * 		elements.
	 */
	public PiecewiseLinearProbabilityDensityFunction(double[] x, double[] pdf) {

		if (x.length != pdf.length) {
			throw new IllegalArgumentException("x.length != pdf.length");
		}

		for (int i = 0; i < x.length; i++) {
			if (i > 0 && x[i - 1] >= x[i]) {
				throw new IllegalArgumentException("x is not sorted");
			}
			if (pdf[i] < 0) {
				throw new IllegalArgumentException("pdf must not have negative elements");
			}
		}

		this.x = x.clone();
		this.pdf = pdf.clone();

		double sum = 0.0;
		this.cdf = new double[pdf.length];
		this.cdf[0] = 0;

		/* Compute the cumulative distribution function. */
		for (int i = 1; i < pdf.length; i++) {
			sum += (this.x[i] - this.x[i - 1]) * (this.pdf[i - 1] + this.pdf[i]) / 2.0;
			this.cdf[i] = sum;
		}

		/* Normalize the PDF and CDF. */
		for (int i = 0; i < pdf.length; i++) {
			this.pdf[i] /= sum;
			this.cdf[i] /= sum;
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#evaluate(double)
	 */
	public double evaluate(double x) {
		return MathUtil.interpolate(this.x, this.pdf, x);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ProbabilityDensityFunction#warp(double)
	 */
	public double warp(double seed) {

		/* If the value is out of range, the results are undefined.  Returning
		 * the lowest domain point if the seed is negative and the highest
		 * domain point if the seed is at least 1 seems reasonable -- since
		 * those values would correspond to the inverse of the CDF.
		 */
		if (seed < cdf[0]) {
			return x[0];
		} else if (seed >= cdf[cdf.length - 1]) {
			return x[x.length - 1];
		}

		/* The CDF is monotonically increasing, so use binary search to find
		 * interval that contains the domain point at which the CDF evaluates
		 * to the specified seed.
		 */
		int i = Arrays.binarySearch(cdf, seed);
		if (i < 0) {
			i = -i - 2;
		}
		while (i < cdf.length - 1 && !(seed < cdf[i + 1])) {
			i++;
		}

		assert(i < cdf.length - 1);

		/* Compute the slope of the line segment describing the PDF on the
		 * interval (x[i], x[i + 1]).
		 */
		double m = (pdf[i + 1] - pdf[i]) / (x[i + 1] - x[i]);

		/* Since the PDF is piecewise linear:
		 *
		 * 		P(x) = P(x[i]) + m * (x - x[i])
		 *
		 * The CDF is piecewise quadratic:
		 *
		 * 		C(x) = (P(x[i]) - m * x[i]) * x + (1/2) * m * x^2 + C
		 * 		... for some constant C.
		 *
		 * Substituting b = P(x[i]) - m * x[i] {eq. 1}, we get
		 *
		 * 		C(x) = b * x + (1/2) * m * x^2 + C
		 *
		 * We know the value of the CDF at x[i], so we can determine C:
		 *
		 * 		C(x[i]) = b * x[i] + (1/2) * m * x[i]^2 + C
		 * 		==> C = C(x[i]) - (b * x[i] + (1/2) * m * x[i]^2)	{eq. 2}
		 *
		 * Setting C(x0) = seed and solving for x0 yields:
		 *
		 * 		seed = b * x0 + (1/2) * m * x0^2 + C
		 * 		==> (1/2) * m * x0^2 + b * x0 + C - seed = 0
		 *
		 *               -b +/- sqrt(b^2 + 2 * m * (C - seed))
		 * 		==> x0 = -------------------------------------
		 *                                m
		 *
		 * Of the two values, we accept is the one that falls in the range
		 * [x[i], x[i + 1]).  If m == 0, then the above will be undefined.
		 * This is because, in this case, the PDF is simply the constant
		 * P(x[i]), and so the CDF is linear, not quadratic.  In this case,
		 * we may simply perform linear interpolation between (C(x[i]), x[i])
		 * and (C(x[i + 1]), x[i + 1]).
		 */
		if (!MathUtil.isZero(m)) {

			double b = pdf[i] - m * x[i]; /* eq. 1 */
			double C = this.cdf[i] - (b * x[i] + 0.5 * m * x[i] * x[i]); /* eq. 2 */
			double sqrtDet = Math.sqrt(b * b + 2.0 * m * (seed - C));
			double x0 = (-b + sqrtDet) / m;

			if (MathUtil.inRangeCO(x0, x[i], x[i + 1])) {
				return x0;
			} else {
				x0 = (-b - sqrtDet) / m;
				assert(MathUtil.inRangeCO(x0, x[i], x[i + 1]));
				return x0;
			}

		} else { /* MathUtil.isZero(m) */

			return MathUtil.interpolate(cdf[i], x[i], cdf[i + 1], x[i + 1], seed);

		}

	}

	/** An array of the domain points. */
	private final double[] x;

	/**
	 * The values of this probability density functions corresponding to the
	 * domain points in {@link #x}.
	 * @see #x
	 */
	private final double[] pdf;

	/**
	 * The values of the cumulative density function (the integration of the
	 * probability density function) corresponding to the domain points in
	 * {@link #x}.
	 * @see #x
	 */
	private final double[] cdf;

}
