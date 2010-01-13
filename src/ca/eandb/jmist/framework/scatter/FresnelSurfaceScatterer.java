/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class FresnelSurfaceScatterer implements SurfaceScatterer {
	
	private final Function1 riBelow;

	private final Function1 riAbove;

	/**
	 * @param riBelow
	 * @param riAbove
	 */
	public FresnelSurfaceScatterer(Function1 riBelow, Function1 riAbove) {
		this.riBelow = riBelow;
		this.riAbove = riAbove;
	}
	
	public FresnelSurfaceScatterer(double riBelow, double riAbove) {
		this(new ConstantFunction1(riBelow), new ConstantFunction1(riAbove));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double lambda, Random rnd) {
		
		double n1 = riAbove.evaluate(lambda);
		double n2 = riBelow.evaluate(lambda);
		Vector3 N = x.getNormal();
		double R = Optics.reflectance(v, n1, n2, N);
		
		if (RandomUtil.bernoulli(R, rnd)) {
			return Optics.reflect(v, N);
		} else {
			return Optics.refract(v, n1, n2, N);
		}
	}

}
