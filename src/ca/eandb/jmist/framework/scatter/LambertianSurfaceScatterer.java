/**
 *
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> representing a Lambertian surface.
 * @see <a href="http://en.wikipedia.org/wiki/Lambertian_reflectance">Lambertian reflectance (wikipedia)</a>
 * @author Brad Kimmel
 */
public final class LambertianSurfaceScatterer implements SurfaceScatterer {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6806933821578898186L;

	/** The reflectance of the surface. */
	private final Function1 reflectance;

	/**
	 * Creates a new <code>LambertianSurfaceScatterer</code>.
	 * @param reflectance The reflectance of the surface.
	 */
	public LambertianSurfaceScatterer(Function1 reflectance) {
		this.reflectance = reflectance;
	}

	/**
	 * Creates a new <code>LambertianSurfaceScatterer</code>.
	 * @param reflectance The reflectance of the surface.
	 */
	public LambertianSurfaceScatterer(double reflectance) {
		this(new ConstantFunction1(reflectance));
	}

	/**
	 * Creates a new <code>LambertianSurfaceScatterer</code> with unit
	 * reflectance.
	 */
	public LambertianSurfaceScatterer() {
		this(Function1.ONE);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, double lambda, Random rnd) {

		double r = reflectance.evaluate(lambda);
		if (RandomUtil.bernoulli(r, rnd)) {
			Vector3 out = RandomUtil.diffuse(rnd).toCartesian(x.getBasis());
			return (v.dot(x.getNormal()) < 0.0) ? out : out.opposite();
		}

		return null;
	}

}
