/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class FresnelSurfaceScattterer implements SurfaceScatterer {
	
	private final Spectrum riBelow;

	private final Spectrum riAbove;

	/**
	 * @param riBelow
	 * @param riAbove
	 */
	public FresnelSurfaceScattterer(Spectrum riBelow, Spectrum riAbove) {
		this.riBelow = riBelow;
		this.riAbove = riAbove;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, Random rnd) {
		
		double n1 = ColorUtil.getMeanChannelValue(riAbove.sample(lambda));
		double n2 = ColorUtil.getMeanChannelValue(riBelow.sample(lambda));
		Vector3 N = x.getNormal();
		double R = Optics.reflectance(v, n1, n2, N);
		
		if (RandomUtil.bernoulli(R, rnd)) {
			Vector3 out = Optics.reflect(v, N);
			Ray3 ray = new Ray3(x.getPosition(), out);
			return ScatteredRay.specular(ray, lambda.getColorModel().getWhite(
					lambda), R);
		} else {
			Vector3 out = Optics.refract(v, n1, n2, N);
			Ray3 ray = new Ray3(x.getPosition(), out);
			return ScatteredRay.transmitSpecular(ray, lambda.getColorModel().getWhite(
					lambda), R);
		}
	}

}
