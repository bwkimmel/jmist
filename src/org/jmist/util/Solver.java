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

			case 4:		/* cubic */
				return roots(c[0], c[1], c[2], c[3]);

			case 5:		/* quartic */
				return roots(c[0], c[1], c[2], c[3], c[4]);

			default:
				// TODO: handle higher order polynomials.
				assert(false);
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
		if (c2 == 0.0)
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

	/**
	 * Computes the real roots of the cubic equation:
	 * 0 = c0 + (c1 * x) + (c2 * x^2) + (c3 * x^3)
	 * @param c0	the coefficient to x^0
	 * @param c1	the coefficient to x^1
	 * @param c2	the coefficient to x^2
	 * @param c3	the coefficient to x^3
	 * @return An array containing the real roots of the cubic
	 * 		equation with the given coefficients.
	 */
	public static final double[] roots(double c0, double c1, double c2, double c3) {

		// Make sure the cubic coefficient is non-zero.
		if (c3 == 0.0)
			return roots(c0, c1, c2);

		/*
		 * The following uses the method from:
		 *
		 * Nickalls, R.W.D., "A new approach to solving the cubic:  Cardan's
		 * solution revealed", The Mathematical Gazette 77 (354-359), 1993.
		 */

		double		xN			= -c2 / (3.0 * c3);
		double		yN			= c0 + xN * (c1 + xN * (c2 + c3 * xN));
		double		two_a		= 2.0 * c3;
		double		delta_sq	= (c2 * c2 - 3.0 * c3 * c1) / (9.0 * c3 * c3);
		double		h_sq 		= two_a * two_a * delta_sq * delta_sq * delta_sq;
		double		dis			= yN * yN - h_sq;

		if (isZero(dis)) {

			// three real roots (two or three equal):
			double	delta3		= yN / two_a;

			if (isZero(delta3)) {
				return new double[]{ xN };
			} else {
				double delta	= Math.cbrt(delta3);
				return new double[]{ xN + delta, xN - 2.0 * delta };
			}

		} else if (dis > 0.0) {

			// one real root:
			double	dis_sqrt	= Math.sqrt(dis);
			double	r_p 		= yN - dis_sqrt;
			double	r_q			= yN + dis_sqrt;
			double	p			= -Math.signum(r_p) * Math.cbrt(Math.signum(r_p) * r_p / two_a);
			double	q			= -Math.signum(r_q) * Math.cbrt(Math.signum(r_q) * r_q / two_a);

			return new double[]{ xN + p + q };

			// The two complex roots are:
			// x(2) = xN + p * exp(2*pi*i/3) + q * exp(-2*pi*i/3);
			// x(3) = conj(x(2));

		} else { // dis < 0.0

			// three distinct real roots:
			double	theta		= Math.acos(-yN / Math.sqrt(h_sq)) / 3.0;
			double	delta		= Math.sqrt(delta_sq);
			double	two_d		= 2.0 * delta;
			double	twop3		= 2.0 * Math.PI / 3.0;

			return new double[]{
					xN + two_d*Math.cos(theta),
					xN + two_d*Math.cos(twop3-theta),
					xN + two_d*Math.cos(twop3+theta)
			};

		}

	}

	/**
	 * Computes the real roots of the quartic equation:
	 * 0 = c0 + (c1 * x) + (c2 * x^2) + (c3 * x^3) + (c4 * x^4)
	 * @param c0	the coefficient to x^0
	 * @param c1	the coefficient to x^1
	 * @param c2	the coefficient to x^2
	 * @param c3	the coefficient to x^3
	 * @param c4	the coefficient to x^4
	 * @return An array containing the real roots of the quartic
	 * 		equation with the given coefficients.
	 */
	public static double[] roots(double c0, double c1, double c2, double c3, double c4) {

		/*
		 * The following is adapted from:
		 *
		 * Glassner, A., "Graphics Gems", Academic Press, 1995.  (Appendix 2)
		 */

		/*
		 *  Before we do anything, make sure it's not really a cubic (or a
		 *  polynomial of lesser degree).
		 */
		if (c4 == 0.0)
			return roots(c0, c1, c2, c3);

	    double[]	result;
	    double[]	s, t;
	    double		z, u, v, sub;
	    double		A, B, C, D;
	    double		sq_A, p, q, r;
	    int			i;

	    /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */

	    A = c3 / c4;
	    B = c2 / c4;
	    C = c1 / c4;
	    D = c0 / c4;

	    /*  substitute x = y - A/4 to eliminate cubic term:
		x^4 + px^2 + qx + r = 0 */

	    sq_A = A * A;
	    p = - 3.0/8 * sq_A + B;
	    q = 1.0/8 * sq_A * A - 1.0/2 * A * B + C;
	    r = - 3.0/256*sq_A*sq_A + 1.0/16*sq_A*B - 1.0/4*A*C + D;

	    if (isZero(r)) {

			/* no absolute term: y(y^3 + py + q) = 0 */

			s = roots(q, p, 0.0, 1.0);

			result = new double[s.length + 1];
			for (i = 0; i < s.length; i++) {
				result[i] = s[i];
			}

			result[i] = 0;

	    } else {

			/* solve the resolvent cubic ... */

			s = roots(
					1.0/2 * r * p - 1.0/8 * q * q,
					-r,
					-1.0/2 * p,
					1
			);

			/* ... and take the one real solution ... */

			z = s[ 0 ];

			/* ... to build two quadric equations */

			u = z * z - r;
			v = 2 * z - p;

			if (isZero(u))
			    u = 0;
			else if (u > 0)
			    u = Math.sqrt(u);
			else
			    return new double[0];

			if (isZero(v))
			    v = 0;
			else if (v > 0)
			    v = Math.sqrt(v);
			else
			    return new double[0];

			s = roots(z - u, q < 0 ? -v : v, 1);
			t = roots(z + u, q < 0 ? v : -v, 1);

			result = new double[s.length + t.length];

			for (i = 0; i < s.length; i++) {
				result[i] = s[i];
			}

			for (i = 0; i < t.length; i++) {
				result[i + s.length] = t[i];
			}

	    }

	    /* resubstitute */

	    sub = 1.0/4 * A;

	    for (i = 0; i < result.length; ++i)
	    	result[ i ] -= sub;

	    return result;

	}

	/**
	 * Determines if the specified floating point value is close to zero.
	 * @param value The value to compare with zero.
	 * @return A value that indicates whether {@code value} is near zero.
	 */
	private static boolean isZero(double value) {

		final double eps = Double.longBitsToDouble(Double.doubleToRawLongBits(1.0) + 1) - 1.0;

		return Math.abs(value) < eps;

	}

}
