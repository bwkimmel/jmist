/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author bwkimmel
 *
 */
public final class TrowbridgeReitzSurfaceScatterer implements SurfaceScatterer {
	
	private final double oblateness;
	
	private final Function1 riBelow;

	private final Function1 riAbove;

	/**
	 * @param riBelow
	 * @param riAbove
	 */
	public TrowbridgeReitzSurfaceScatterer(double oblateness, Function1 riBelow, Function1 riAbove) {
		this.oblateness = oblateness;
		this.riBelow = riBelow;
		this.riAbove = riAbove;
	}
	
	public TrowbridgeReitzSurfaceScatterer(double oblateness, double riBelow, double riAbove) {
		this(oblateness, new ConstantFunction1(riBelow), new ConstantFunction1(riAbove));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, double, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double lambda, Random rnd) {
		double n1 = riAbove.evaluate(lambda);
		double n2 = riBelow.evaluate(lambda);
		Vector3 N = x.getNormal();
		double R = Optics.reflectance(v, n1, n2, N);
		
		if (RandomUtil.bernoulli(R, rnd)) {
			Basis3 basis = x.getBasis();
			double sigma2 = oblateness * oblateness;
			double sigma4 = sigma2 * sigma2;
			Vector3 out;
			double theta = Math.acos(Math.sqrt(((sigma2 / Math.sqrt(sigma4 + (1.0 - sigma4) * rnd.next())) - 1.0) / (sigma2 - 1.0)));
			double phi = 2.0 * Math.PI * rnd.next();
			SphericalCoordinates sc = new SphericalCoordinates(theta, phi);
			Vector3 microN = sc.toCartesian(basis);
			out = Optics.reflect(v, microN);
			if (out.dot(N) <= 0.0) {
				return null;
			}
			return out;
		} else {
			return Optics.refract(v, n1, n2, N);
		}
	}

}
