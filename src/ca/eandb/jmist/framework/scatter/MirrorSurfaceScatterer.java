/**
 *
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> that specularly reflects.
 * @author Brad Kimmel
 */
public final class MirrorSurfaceScatterer implements SurfaceScatterer {

	/** Serialization version ID. */
	private static final long serialVersionUID = -3125700858196796582L;

	/**
	 * The <code>Function1</code> indicating the probability that an incident
	 * ray is reflected.
	 */
	private final Function1 reflectance;

	/**
	 * Creates a new <code>MirrorSurfaceScatterer</code>.
	 * @param reflectance The <code>Function1</code> indicating the
	 *		probability that an incident ray is reflected.
	 */
	public MirrorSurfaceScatterer(Function1 reflectance) {
		this.reflectance = reflectance;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, double, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double wavelength, Random rnd) {
		double R = reflectance.evaluate(wavelength);

		if (RandomUtil.bernoulli(R, rnd)) {
			Vector3 N = x.getNormal();
			return Optics.reflect(v, N);
		} else {
			return null;
		}
	}

}
