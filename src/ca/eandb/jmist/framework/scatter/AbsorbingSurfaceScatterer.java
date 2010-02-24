/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
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
	
	private final Function1 absorptionCoefficient;
	
	private final double thickness;

	/**
	 * @param absorptionCoefficient
	 * @param thickness
	 */
	public AbsorbingSurfaceScatterer(Function1 absorptionCoefficient,
			double thickness) {
		this.absorptionCoefficient = absorptionCoefficient;
		this.thickness = thickness;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, double lambda, Random rnd) {
		
		double abs = absorptionCoefficient.evaluate(lambda);
		double p = -Math.log(1.0 - rnd.next()) * Math.abs(Math.cos(x.getNormal().dot(v))) / abs;
		
		return (p > thickness) ? v : null;
	}

}
