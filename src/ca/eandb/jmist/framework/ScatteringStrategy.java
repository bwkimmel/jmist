package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * A surface scattering algorithm.  Used to control how a <code>Material</code>
 * chooses scattering directions independently of the BSDF for the material.
 * @author Brad Kimmel
 */
public interface ScatteringStrategy {

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
	ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj);

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
	ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda, double ru,
			double rv, double rj);

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
	double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
			boolean adjoint, WavelengthPacket lambda);

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

	double getWeight(SurfacePoint x, WavelengthPacket lambda);

}