/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import java.io.Serializable;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents the surface scattering properties of a material.
 * 
 * @author Brad Kimmel
 */
public interface SurfaceScatterer extends Serializable {
	
	/**
	 * Simulates surface scattering.
	 * @param x The <code>SurfacePointGeometry</code> describing the geometry
	 * 		at the point on the surface to which the light is incident.
	 * @param v The <code>Vector3</code> indicating the incident direction.
	 * @param adjoint A value indicating whether ray tracing is proceeding
	 * 		backwards (i.e., from the eye).
	 * @param wavelength The wavelength of the incident light (in meters).
	 * @param rnd The <code>Random</code> number generator to use.
	 * @return A <code>Vector3</code> indicating the direction of scattering,
	 * 		or <code>null</code> if the ray is absorbed.
	 */
	Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double wavelength, Random rnd);
	
	/** A <code>SurfaceScatterer</code> that absorbs all incident light. */
	public static final SurfaceScatterer ABSORB = new SurfaceScatterer() {
		private static final long serialVersionUID = 8194517746654987095L;
		public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
				boolean adjoint, double wavelength, Random rnd) {
			return null;
		}
	};
	
	/** A <code>SurfaceScatterer</code> that transmits all incident light. */
	public static final SurfaceScatterer TRANSMIT = new SurfaceScatterer() {
		private static final long serialVersionUID = -927167595504776971L;
		public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
				boolean adjoint, double wavelength, Random rnd) {
			return v;
		}
	};
	
}
