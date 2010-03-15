/**
 *
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> representing a Lambertian surface with unit
 * reflectance.
 * @see <a href="http://en.wikipedia.org/wiki/Lambertian_reflectance">Lambertian reflectance (wikipedia)</a>
 * @author Brad Kimmel
 */
public final class DiffusingSurfaceScatterer implements SurfaceScatterer {

	/** Serialization version ID. */
	private static final long serialVersionUID = 5755264699518450187L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, double lambda, Random rnd) {

		Vector3 N = x.getNormal();

		if (v.dot(N) < 0.0) {
			N = N.opposite();
		}

		return RandomUtil.diffuse(rnd).toCartesian(Basis3.fromW(N));
	}

}
