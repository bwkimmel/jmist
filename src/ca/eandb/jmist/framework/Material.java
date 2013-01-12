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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * Simulates how a <code>SceneElement</code> scatters and absorbs light.
 * @author Brad Kimmel
 */
public interface Material extends Medium, Serializable {

//	Color scattering(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda);
//	Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda);

	/** Gets a value indicating if this <code>Material</code> emits light. */
	boolean isEmissive();

//	void scatter(SurfacePoint x, Vector3 v, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder);
//	void emit(SurfacePoint x, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder);

	/**
	 * Scatters a ray incident on this material.
	 * @param x The <code>SurfacePoint</code> that the ray is incident upon.
	 * @param v The <code>Vector3</code> indicating the direction of travel of
	 * 		the incident ray (note that the <code>Vector3</code> points
	 * 		<em>toward</code> the surface).
	 * @param adjoint A value indicating whether the path being simulated is in
	 * 		the reverse direction from the direction in which light is
	 * 		traveling (i.e., <code>true</code> for path tracing from the eye,
	 * 		<code>false</code> for light tracing).
	 * @param lambda The <code>WavelengthPacket</code> associated with the
	 * 		light traveling along this path.
	 * @param ru The first random variable (must be in [0, 1]).
	 * @param rv The second random variable (must be in [0, 1]).
	 * @param rj The third random variable (must be in [0, 1]).
	 * @return The <code>ScatteredRay</code>, or <code>null</code> to indicate
	 * 		that the ray was absorbed.
	 */
	ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint, WavelengthPacket lambda, double ru, double rv, double rj);

	/**
	 * Emits a ray from this material.
	 * @param x The <code>SurfacePoint</code> from which to emit.
	 * @param lambda The <code>WavelengthPacket</code> associated with the
	 * 		light traveling along the path.
	 * @param ru The first random variable (must be in [0, 1]).
	 * @param rv The second random variable (must be in [0, 1]).
	 * @param rj The third random variable (must be in [0, 1]).
	 * @return The <code>ScatteredRay</code>, or <code>null</code> if this
	 * 		material is not emissive.
	 */
	ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda, double ru, double rv, double rj);

	/**
	 * Computes the marginal probability of emission in the specified
	 * direction.
	 * @param x The <code>SurfacePoint</code> for which to compute the emission
	 * 		probability density function (PDF).
	 * @param in The <code>Vector3</code> indicating the incident direction
	 * 		(note that this points <em>toward</em> the surface).
	 * @param out The <code>Vector3</code> indicating the outgoing direction
	 * 		for which to evaluate the PDF.
	 * @param adjoint A value indicating whether the path being simulated is in
	 * 		the reverse direction from the direction in which light is
	 * 		traveling (i.e., <code>true</code> for path tracing from the eye,
	 * 		<code>false</code> for light tracing).
	 * @param lambda The <code>WavelengthPacket</code> associated with the
	 * 		light traveling along the path.
	 * @return The marginal probability that the point <code>x</code> scatters
	 * 		in the direction <code>out</code>, given that light was incident
	 * 		from the direction <code>in</code>.
	 */
	double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out, boolean adjoint, WavelengthPacket lambda);

	/**
	 * Computes the marginal probability of emission in the specified
	 * direction.
	 * @param x The <code>SurfacePoint</code> for which to compute the emission
	 * 		probability density function (PDF).
	 * @param out The <code>Vector3</code> indicating the outgoing direction
	 * 		for which to evaluate the PDF.
	 * @param lambda The <code>WavelengthPacket</code> associated with the
	 * 		light traveling along the path.
	 * @return The marginal probability that the point <code>x</code> emits in
	 * 		the direction <code>out</code>.
	 */
	double getEmissionPDF(SurfacePoint x, Vector3 out, WavelengthPacket lambda);

	/**
	 * Evaluates the bidirectional scattering distribution function (BSDF).
	 * @param x The <code>SurfacePoint</code> for which to compute the BSDF.
	 * @param out The <code>Vector3</code> indicating the outgoing direction
	 * 		for which to evaluate the BSDF.
	 * @param out The <code>Vector3</code> indicating the outgoing direction
	 * 		for which to evaluate the BSDF.
	 * @param lambda The <code>WavelengthPacket</code> associated with the
	 * 		light traveling along the path.
	 * @return The value of the BSDF.
	 * @see <a href="http://en.wikipedia.org/wiki/Bidirectional_scattering_distribution_function">Bidirectional scattering distribution function</a>.
	 * @see <a href="http://en.wikipedia.org/wiki/Bidirectional_reflectance_distribution_function">Bidirectional reflectance distribution function</a>.
	 */
	Color bsdf(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda);

	/**
	 * Evaluates the emission distribution function (EDF).
	 * @param x The <code>SurfacePoint</code> for which to compute the EDF.
	 * @param out The <code>Vector3</code> indicating the outgoing direction
	 * 		for which to evaluate the EDF.
	 * @param out The <code>Vector3</code> indicating the outgoing direction
	 * 		for which to evaluate the EDF.
	 * @param lambda The <code>WavelengthPacket</code> associated with the
	 * 		light traveling along the path.
	 * @return The value of the EDF.
	 */
	Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda);


	/**
	 * A <code>Material</code> that absorbs all light and does not
	 * emit.
	 */
	public static final Material BLACK = new Material() {

		private static final long serialVersionUID = 4301103342747509476L;

		public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}

		public boolean isEmissive() {
			return false;
		}

		public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
			return lambda.getColorModel().getGray(Double.POSITIVE_INFINITY, lambda);
		}

		public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
			return lambda.getColorModel().getWhite(lambda);
		}

		public Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}

		public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
				WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}

		public ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda,
				double ru, double rv, double rj) {
			return null;
		}

		public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
				boolean adjoint, WavelengthPacket lambda) {
			return 0.0;
		}

		public double getEmissionPDF(SurfacePoint x, Vector3 out,
				WavelengthPacket lambda) {
			return 0.0;
		}

		public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
				WavelengthPacket lambda, double ru, double rv, double rj) {
			return null;
		}

	};

}
