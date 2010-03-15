/**
 *
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class AbsorbingSurfaceScatterer implements SurfaceScatterer {

	/** Serialization version ID. */
	private static final long serialVersionUID = -8261906690789778531L;

	/** The absorption coefficient of the medium (in m<sup>-1</sup>). */
	private final Function1 absorptionCoefficient;

	/** The thickness of the medium (in meters). */
	private final double thickness;

	/**
	 * Creates a new <code>AbsorbingSurfaceScatterer</code>.
	 * @param absorptionCoefficient The absorption coefficient of the medium
	 * 		(in m<sup>-1</sup>).
	 * @param thickness The thickness of the medium (in meters).
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
