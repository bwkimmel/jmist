/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class AbsorbingSurfaceScatterer implements SurfaceScatterer {
	
	private final Spectrum absorptionCoefficient;
	
	private final double thickness;

	/**
	 * @param absorptionCoefficient
	 * @param thickness
	 */
	public AbsorbingSurfaceScatterer(Spectrum absorptionCoefficient,
			double thickness) {
		this.absorptionCoefficient = absorptionCoefficient;
		this.thickness = thickness;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, WavelengthPacket lambda, Random rnd) {
		
		Color abs = absorptionCoefficient.sample(lambda);
		double meanAbs = ColorUtil.getMeanChannelValue(abs);
		
		double p = -Math.log(1.0 - rnd.next()) * Math.abs(x.getNormal().dot(v)) / meanAbs;
		if (p > thickness) {
			Ray3 ray = new Ray3(x.getPosition(), v);
			return ScatteredRay.transmitSpecular(ray, abs.divide(meanAbs), 0.0);
		}
		
		return null; // absorbed
	}

}
