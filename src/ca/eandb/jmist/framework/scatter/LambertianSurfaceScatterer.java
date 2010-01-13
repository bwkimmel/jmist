/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class LambertianSurfaceScatterer implements SurfaceScatterer {

	private final Function1 reflectance;
	
	/**
	 * @param reflectance
	 */
	public LambertianSurfaceScatterer(Function1 reflectance) {
		this.reflectance = reflectance;
	}
	
	public LambertianSurfaceScatterer(double reflectance) {
		this(new ConstantFunction1(reflectance));
	}
	
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
