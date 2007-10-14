/**
 *
 */
package org.jmist.util;

/**
 * Solves mathematical equations.
 * @author bkimmel
 */
public final class Solver {

	/**
	 * Finds the real roots of a polynomial:
	 * 0 = sum(c[j] * x^j, j = 0 to c.length).
	 * @param c		an array of coefficients of the polynomial to
	 * 				find the roots of.
	 * @return An array containing the roots of the polynomial with
	 * 		the specified coefficients.
	 */
	public static final double[] roots(double[] c) {

		switch (c.length) {

			case 1:		/* constant -- no roots */
				return new double[0];

			case 2:		/* linear */
				return roots(c[0], c[1]);

			case 3:		/* quadratic */
				return roots(c[0], c[1], c[2]);

			default:
				// TODO: handle higher order polynomials.
				return new double[0];

		}

	}

	/**
	 * Computes the real roots of the linear equation:
	 * 0 = c0 + (c1 * x)
	 * @param c0	the coefficient to x^0
	 * @param c1	the coefficient to x^1
	 * @return An array containing the real roots of the linear
	 * 		equation.
	 */
	public static final double[] roots(double c0, double c1) {

		/*
		 * c0 is the y-intercept and c1 is the slope.  If the c1 (the
		 * slope) is zero, then there is no root.  Otherwise, the root
		 * is at x = -c0 / c1.
		 */
		return (c1 != 0.0) ? new double[]{ -c0 / c1 } : new double[0];

	}

	/**
	 * Computes the real roots of the quadratic equation:
	 * 0 = c0 + (c1 * x) + (c2 * x^2)
	 * @param c0	the coefficient to x^0
	 * @param c1	the coefficient to x^1
	 * @param c2	the coefficient to x^2
	 * @return An array containing the real roots of the quadratic
	 * 		equation with the given coefficients.
	 */
	public static final double[] roots(double c0, double c1, double c2) {

		/*
		 * If c2 is zero, then this is not a quadratic equation, but a
		 * linear equation.  In this case, call the method to solve for
		 * the roots of the linear equation.
		 */
		if (c2 == 0)
			return roots(c0, c1);

		/*
		 * The descriminant in the quadratic equation.  This value will
		 * determine whether there are zero, one, or two real roots.
		 */
		double descriminant = (c1 * c1) - (4.0 * c2 * c0);

		if (descriminant > 0.0) {

			/*
			 * If the descriminant is positive, then there are two real
			 * roots: (-c1 +/- sqrt(c1 * c1 - 4 * c2 * c0)) / (2 * c2).
			 */
			double h = Math.sqrt(descriminant);
			double d = 2.0 * c2;

			return new double[]{ (-c1 - h) / d, (-c1 + h) / d };

		} else if (descriminant < 0.0) {

			/* If the descriminant is negative, then there are no real roots. */
			return new double[0];

		} else { /* descriminant == 0 */

			/*
			 * If the descriminant is equal to zero, then there is one real
			 * root: -c1 / (2.0 * c2).
			 */
			return new double[]{ -c1 / (2.0 * c2) };

		}

	}

}
