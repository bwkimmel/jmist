/**
 *
 */
package ca.eandb.jmist.toolkit;

import ca.eandb.jmist.util.MathUtil;

/**
 * Provides utility methods for geometric optics.
 * @author Brad Kimmel
 */
public final class Optics {

	/**
	 * Computes a reflected vector.
	 * @param in The vector to be reflected.
	 * @param normal The normal of the surface.
	 * @return The reflected vector.
	 */
	public static Vector3 reflect(Vector3 in, Vector3 normal) {
		return in.minus(normal.times(2.0 * in.dot(normal)));
	}

	/**
	 * Computes the direction of a ray after being refracted
	 * at the interface between two media.
	 * @param in The direction of the incoming ray.
	 * @param n1 The refractive index of the medium on the side toward
	 * 		which the normal points.
	 * @param n2 The refractive index of the medium on the opposite
	 * 		side of the interface from which to the normal points.
	 * @param normal The direction normal to the interface between
	 * 		the two media.
	 * @return The direction of the refracted ray.
	 */
	public static Vector3 refract(Vector3 in, double n1, double n2, Vector3 normal) {

		//
		// Compute the refracted vector using Heckbert's method, see
		//
		// P.S. Heckbert, "Writing a Ray Tracer", in A.S. Glassner (Editor),
		// "An Introduction to Ray Tracing", Morgan Kaufmann Publishers, Inc.,
		// San Francisco, CA, 2002, Section 5.2, pp.291-293
		//
		// Note: The algorithm has been modified slightly from the one presented
		// in the book so that refract(I, n1, n2, N) == refract(I, n2, n1, -N).
		//
		double c1 = -in.dot(normal);

		if (c1 > 0.0) {

			double eta = n1 / n2;
			double det = 1.0 - eta * eta * (1.0 - c1 * c1);

			if (det < 0.0) { // total internal reflection
				return Optics.reflect(in, normal);
			}

			double c2 = Math.sqrt(det);

			// return the unit vector in the direction of
			// eta * I + (eta * c1 - c2) * N
			return in.times(eta).plus(normal.times(eta * c1 - c2)).unit();

		} else { // c1 <= 0.0

			double eta = n2 / n1;
			double det = 1.0 - eta * eta * (1.0 - c1 * c1);

			if (det < 0.0) { // total internal reflection
				return Optics.reflect(in, normal);
			}

			double c2 = Math.sqrt(det);

			// return the unit vector in the direction of
			// eta * I + (eta * c1 + c2) * N
			return in.times(eta).plus(normal.times(eta * c1 + c2)).unit();

		}

	}

	/**
	 * Computes the angle to the normal of a ray that has been
	 * refracted at the interface between two media.
	 * @param theta The angle between the normal and the incident ray.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which to the normal points.
	 * @param n2 The refractive index of the medium
	 * @return The angle between the direction of the refracted ray and
	 * 		the anti-normal.
	 */
	public static double refract(double theta, double n1, double n2) {

		double cost = Math.cos(theta);

		if (cost < 0.0) {
			double temp = n1;
			n1 = n2;
			n2 = temp;
			cost = -cost;
		}

		double eta = n1 / n2;
		double det = 1.0 - eta * eta * (1.0 - cost * cost);

		if (det < 0.0) { // total internal reflection
			return Math.PI - theta;
		}

		return Math.acos(Math.sqrt(det));

	}


	/**
	 * Computes the direction of a ray after being refracted
	 * at the interface between two media.
	 * @param in The direction of the incoming ray.
	 * @param n1 The refractive index of the medium on the side toward
	 * 		which the normal points.
	 * @param n2 The refractive index of the medium on the opposite
	 * 		side of the interface from which to the normal points.
	 * @param normal The direction normal to the interface between
	 * 		the two media.
	 * @return The direction of the refracted ray.
	 */
	public static Vector3 refract(Vector3 in, Complex n1, Complex n2, Vector3 normal) {

		double						ci = in.dot(normal);
		RefractResult				rr = Optics.refractAngle(ci, n1, n2);

		if (rr.tir) { // total internal reflection.
			return Optics.reflect(in, normal);
		}

		// return the refracted vector, recall that I + ci * N already has
		// the length sin(theta_i), so we need only divide by nEff to resize
		// to sin(theta'_t), see Born & Wolf, sec. 13.2, equation (9).
		Vector3						out = in.divide(rr.nEff).plus(normal.times(-rr.cosT + ci / rr.nEff));

		return out;

	}

	/**
	 * Computes the angle to the normal of a ray that has been
	 * refracted at the interface between two media.
	 * @param theta The angle between the normal and the incident ray.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which to the normal points.
	 * @param n2 The refractive index of the medium
	 * @return The angle between the direction of the refracted ray and
	 * 		the anti-normal.
	 */
	public static double refract(double theta, Complex n1, Complex n2) {

		double						ci = Math.cos(theta);
		RefractResult				rr = Optics.refractAngle(ci, n1, n2);

		if (rr.tir) { // total internal reflection
			return Math.PI - theta;
		}

		return Math.acos(rr.cosT);

	}

	/**
	 * Computes the reflectance components for polarized light incident
	 * on an interface between two media.
	 * @param in The incident direction.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @param normal The direction perpendicular to the interface between
	 * 		the two media.
	 * @return The vector (R_{TE}, R_{TM}), denoting the reflectance for
	 * 		transverse electric (TE) and transverse magnetic (TM) modes
	 * 		of light incident on the interface from the specified direction.
	 */
	public static Vector2 polarizedReflectance(Vector3 in, double n1, double n2, Vector3 normal) {

		double cost = -in.dot(normal);
		double sin2t = 1.0 - cost * cost;
		double n;

		if (cost < 0.0) {
			n = n1 / n2;
			cost = -cost;
		} else { // cost >= 0.0
			n = n2 / n1;
		}

		double nSquared = n * n;

		double A = Math.sqrt(nSquared - sin2t);

		double TE = (cost - A) / (cost + A);
		double TM = (nSquared * cost - A) / (nSquared * cost + A);

		return new Vector2(MathUtil.threshold(TE * TE, 0.0, 1.0), MathUtil.threshold(TM * TM, 0.0, 1.0));

	}

	/**
	 * Computes the reflectance components for polarized light incident
	 * on an interface between two media.
	 * @param in The incident direction.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @param normal The direction perpendicular to the interface between
	 * 		the two media.
	 * @return The vector (R_{TE}, R_{TM}), denoting the reflectance for
	 * 		transverse electric (TE) and transverse magnetic (TM) modes
	 * 		of light incident on the interface from the specified direction.
	 */
	public static Vector2 polarizedReflectance(Vector3 in, double n1, Complex n2, Vector3 normal) {
		return polarizedReflectance(in, n1, n2, normal);
	}

	/**
	 * Computes the reflectance components for polarized light incident
	 * on an interface between two media.
	 * @param in The incident direction.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @param normal The direction perpendicular to the interface between
	 * 		the two media.
	 * @return The vector (R_{TE}, R_{TM}), denoting the reflectance for
	 * 		transverse electric (TE) and transverse magnetic (TM) modes
	 * 		of light incident on the interface from the specified direction.
	 */
	public static Vector2 polarizedReflectance(Vector3 in, Complex n1, double n2, Vector3 normal) {
		return polarizedReflectance(in, n1, n2, normal);
	}

	/**
	 * Computes the reflectance components for polarized light incident
	 * on an interface between two media.
	 * @param in The incident direction.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @param normal The direction perpendicular to the interface between
	 * 		the two media.
	 * @return The vector (R_{TE}, R_{TM}), denoting the reflectance for
	 * 		transverse electric (TE) and transverse magnetic (TM) modes
	 * 		of light incident on the interface from the specified direction.
	 */
	public static Vector2 polarizedReflectance(Vector3 in, Complex n1, Complex n2, Vector3 normal) {

		double		cost = -in.dot(normal);
		double		sin2t = 1.0 - cost * cost;
		Complex		n;

		if (cost < 0.0) {
			n = n1.divide(n2);
			cost = -cost;
		} else { // cost >= 0.0
			n = n2.divide(n1);
		}

		Complex		nSquared = n.times(n);

		Complex		A = nSquared.minus(sin2t).sqrt();

		// TE = (cost - A) / (cost + A)
		// TM = (n^2 * cost - A) / (n^2 * cost + A)
		double		absTE = A.negative().plus(cost).divide(A.plus(cost)).abs();
		double		absTM = nSquared.times(cost).minus(A).divide(nSquared.times(cost).plus(A)).abs();

		return new Vector2(MathUtil.threshold(absTE * absTE, 0.0, 1.0), MathUtil.threshold(absTM * absTM, 0.0, 1.0));

	}

	/**
	 * Computes the reflectance components for polarized light incident
	 * on an interface between two media.
	 * @param theta The angle between the incident direction and the normal.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @return The vector (R_{TE}, R_{TM}), denoting the reflectance for
	 * 		transverse electric (TE) and transverse magnetic (TM) modes
	 * 		of light incident on the interface from the specified direction.
	 */
	public static Vector2 polarizedReflectance(double theta, double n1, double n2) {

		double cost = Math.cos(theta);
		double sin2t = 1.0 - cost * cost;
		double n;

		if (cost < 0.0) {
			n = n1 / n2;
			cost = -cost;
		} else { // cost >= 0.0
			n = n2 / n1;
		}

		double nSquared = n * n;

		double A = Math.sqrt(nSquared - sin2t);

		double TE = (cost - A) / (cost + A);
		double TM = (nSquared * cost - A) / (nSquared * cost + A);

		return new Vector2(MathUtil.threshold(TE * TE, 0.0, 1.0), MathUtil.threshold(TM * TM, 0.0, 1.0));

	}

	/**
	 * Computes the reflectance components for polarized light incident
	 * on an interface between two media.
	 * @param theta The angle between the incident direction and the normal.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @return The vector (R_{TE}, R_{TM}), denoting the reflectance for
	 * 		transverse electric (TE) and transverse magnetic (TM) modes
	 * 		of light incident on the interface from the specified direction.
	 */
	public static Vector2 polarizedReflectance(double theta, double n1, Complex n2) {
		return polarizedReflectance(theta, n1, n2);
	}

	/**
	 * Computes the reflectance components for polarized light incident
	 * on an interface between two media.
	 * @param theta The angle between the incident direction and the normal.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @return The vector (R_{TE}, R_{TM}), denoting the reflectance for
	 * 		transverse electric (TE) and transverse magnetic (TM) modes
	 * 		of light incident on the interface from the specified direction.
	 */
	public static Vector2 polarizedReflectance(double theta, Complex n1, double n2) {
		return polarizedReflectance(theta, n1, n2);
	}

	/**
	 * Computes the reflectance components for polarized light incident
	 * on an interface between two media.
	 * @param theta The angle between the incident direction and the normal.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @return The vector (R_{TE}, R_{TM}), denoting the reflectance for
	 * 		transverse electric (TE) and transverse magnetic (TM) modes
	 * 		of light incident on the interface from the specified direction.
	 */
	public static Vector2 polarizedReflectance(double theta, Complex n1, Complex n2) {

		double		cost = Math.cos(theta);
		double		sin2t = 1.0 - cost * cost;
		Complex		n;

		if (cost < 0.0) {
			n = n1.divide(n2);
			cost = -cost;
		} else { // cost >= 0.0
			n = n2.divide(n1);
		}

		Complex		nSquared = n.times(n);

		Complex		A = nSquared.minus(sin2t).sqrt();

		// TE = (cost - A) / (cost + A)
		// TM = (n^2 * cost - A) / (n^2 * cost + A)
		double		absTE = A.negative().plus(cost).divide(A.plus(cost)).abs();
		double		absTM = nSquared.times(cost).minus(A).divide(nSquared.times(cost).plus(A)).abs();

		return new Vector2(MathUtil.threshold(absTE * absTE, 0.0, 1.0), MathUtil.threshold(absTM * absTM, 0.0, 1.0));

	}

	/**
	 * Computes the reflectance components for unpolarized light incident
	 * on an interface between two media.
	 * @param in The incident direction.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @param normal The direction perpendicular to the interface between
	 * 		the two media.
	 * @return The reflectance of unpolarized light incident on the
	 * 		interface from the specified direction.
	 */
	public static double reflectance(Vector3 in, double n1, double n2, Vector3 normal) {
		Vector2 R = polarizedReflectance(in, n1, n2, normal);
		return 0.5 * (R.x() + R.y());
	}

	/**
	 * Computes the reflectance components for unpolarized light incident
	 * on an interface between two media.
	 * @param in The incident direction.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @param normal The direction perpendicular to the interface between
	 * 		the two media.
	 * @return The reflectance of unpolarized light incident on the
	 * 		interface from the specified direction.
	 */
	public static double reflectance(Vector3 in, double n1, Complex n2, Vector3 normal) {
		Vector2 R = polarizedReflectance(in, n1, n2, normal);
		return 0.5 * (R.x() + R.y());
	}

	/**
	 * Computes the reflectance components for unpolarized light incident
	 * on an interface between two media.
	 * @param in The incident direction.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @param normal The direction perpendicular to the interface between
	 * 		the two media.
	 * @return The reflectance of unpolarized light incident on the
	 * 		interface from the specified direction.
	 */
	public static double reflectance(Vector3 in, Complex n1, double n2, Vector3 normal) {
		Vector2 R = polarizedReflectance(in, n1, n2, normal);
		return 0.5 * (R.x() + R.y());
	}

	/**
	 * Computes the reflectance components for unpolarized light incident
	 * on an interface between two media.
	 * @param in The incident direction.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @param normal The direction perpendicular to the interface between
	 * 		the two media.
	 * @return The reflectance of unpolarized light incident on the
	 * 		interface from the specified direction.
	 */
	public static double reflectance(Vector3 in, Complex n1, Complex n2, Vector3 normal) {
		Vector2 R = polarizedReflectance(in, n1, n2, normal);
		return 0.5 * (R.x() + R.y());
	}

	/**
	 * Computes the reflectance components for unpolarized light incident
	 * on an interface between two media.
	 * @param theta The angle between the incident direction and the normal.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @return The reflectance of unpolarized light incident on the
	 * 		interface from the specified direction.
	 */
	public static double reflectance(double theta, double n1, double n2) {
		Vector2 R = polarizedReflectance(theta, n1, n2);
		return 0.5 * (R.x() + R.y());
	}

	/**
	 * Computes the reflectance components for unpolarized light incident
	 * on an interface between two media.
	 * @param theta The angle between the incident direction and the normal.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @return The reflectance of unpolarized light incident on the
	 * 		interface from the specified direction.
	 */
	public static double reflectance(double theta, double n1, Complex n2) {
		Vector2 R = polarizedReflectance(theta, n1, n2);
		return 0.5 * (R.x() + R.y());
	}

	/**
	 * Computes the reflectance components for unpolarized light incident
	 * on an interface between two media.
	 * @param theta The angle between the incident direction and the normal.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @return The reflectance of unpolarized light incident on the
	 * 		interface from the specified direction.
	 */
	public static double reflectance(double theta, Complex n1, double n2) {
		Vector2 R = polarizedReflectance(theta, n1, n2);
		return 0.5 * (R.x() + R.y());
	}

	/**
	 * Computes the reflectance components for unpolarized light incident
	 * on an interface between two media.
	 * @param theta The angle between the incident direction and the normal.
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the opposite side
	 * 		of the interface from which the normal points.
	 * @return The reflectance of unpolarized light incident on the
	 * 		interface from the specified direction.
	 */
	public static double reflectance(double theta, Complex n1, Complex n2) {
		Vector2 R = polarizedReflectance(theta, n1, n2);
		return 0.5 * (R.x() + R.y());
	}

	/**
	 * The structure returned from refractAngle.
	 * @author Brad Kimmel
	 * @see {@link Optics#refractAngle(double, Complex, Complex)}.
	 */
	private static class RefractResult {

		/** The cosine of the transmitted ray. */
		public double cosT;

		/** The effective real refractive index. */
		public double nEff;

		/**
		 * A value indicating whether total internal
		 * reflection has occurred.
		 */
		public boolean tir;

	}

	/**
	 * Computes the cosine of the refracted angle for an interface between
	 * two conductive media, the effective refractive index, and a value
	 * indicating whether total internal reflection has occurred.
	 * @param ci The cosine of the incident angle (the angle between the
	 * 		incident direction and the normal).
	 * @param n1 The refractive index of the medium on the side of the
	 * 		interface to which the normal points.
	 * @param n2 The refractive index of the medium on the side of the
	 * 		interface opposite from which the normal points.
	 * @return An object containing the cosine of the angle between the
	 * 		refracted direction and the anti-normal (cosT), the effective
	 * 		real refractive index (nEff), and a value indicating whether
	 * 		total internal reflection has occurred (tir).
	 * @see {@link Optics.RefractResult}.
	 */
	private static RefractResult refractAngle(double ci, Complex n1, Complex n2) {

		RefractResult result;
		Complex eta;

		if (ci > 0.0) {
			eta = n2.divide(n1);
		} else { // ci <= 0.0

			//
			// if the ray comes from the other side, rewrite the problem
			// with the normal pointing toward the incident direction.
			//
			eta = n2.divide(n1);
			ci = -ci;

		}

		double si2 = 1.0 - ci * ci;

		// get components of the refractive index, eta = n + ik = n(1 + i*kappa)
		double n = eta.re();
		double k = eta.im();
		double kappa = k / n;

		// quantities that will appear later that we would rather not write out
		// each time.
		double A = n * n * (1.0 + kappa * kappa) * (1.0 + kappa * kappa);
		double B = (1.0 - kappa * kappa) * si2;
		double C = 2.0 * kappa * si2;

		// components of cos(theta_t)^2 (the complex theta_t, Born & Wolf, sec 13.2 eq (3b))
		double X = 1.0 - B / A;
		double Y = C / A;

		// compute q and gamma, where cos(theta_t) = qe^{i*gamma}
		double q = Math.pow(X * X + Y * Y, 1.0 / 4.0);
		double gamma = 0.5 * Math.atan2(A - B, C);

		// will need cos and sin of gamma
		double cg = Math.cos(gamma);
		double sg = Math.sin(gamma);

		// compute the modified refractive index
		double K = n * q * (cg - kappa * sg);
		double np = Math.sqrt(si2 + K * K);

		// cosine of theta'_t, the real angle of refraction.
		result = new RefractResult();
		result.cosT = K / np;
		result.nEff = np;
		result.tir = (result.cosT < 0.0);

		return result;

	}

	/**
	 * This class contains only static utility methods,
	 * and therefore should not be creatable.
	 */
	private Optics() {}

}
