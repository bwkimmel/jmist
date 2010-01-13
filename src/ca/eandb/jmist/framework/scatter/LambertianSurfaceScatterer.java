/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class LambertianSurfaceScatterer implements SurfaceScatterer {

	private final Spectrum reflectance;
	
	/**
	 * @param reflectance
	 */
	public LambertianSurfaceScatterer(Spectrum reflectance) {
		this.reflectance = reflectance;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, WavelengthPacket lambda, Random rnd) {
		
		Color color = reflectance.sample(lambda);
		Vector3 out = RandomUtil.diffuse(rnd).toCartesian(x.getBasis());
		if (v.dot(x.getNormal()) > 0.0) {
			out = out.opposite();
		}
		
		Ray3 ray = new Ray3(x.getPosition(), v);
		return ScatteredRay.diffuse(ray, color, 1.0 / Math.PI);
	}

}
