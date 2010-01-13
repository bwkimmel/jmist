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
 * @author brad
 *
 */
public interface SurfaceScatterer extends Serializable {
	
	Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double wavelength, Random rnd);
	
	public static final SurfaceScatterer ABSORB = new SurfaceScatterer() {
		private static final long serialVersionUID = 8194517746654987095L;
		public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
				boolean adjoint, double wavelength, Random rnd) {
			return null;
		}
	};
	
	public static final SurfaceScatterer TRANSMIT = new SurfaceScatterer() {
		private static final long serialVersionUID = -927167595504776971L;
		public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
				boolean adjoint, double wavelength, Random rnd) {
			return v;
		}
	};
	
}
