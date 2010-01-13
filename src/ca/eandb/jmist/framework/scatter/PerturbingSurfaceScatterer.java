/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.ProbabilityDensityFunction;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class PerturbingSurfaceScatterer implements
		SurfaceScatterer {
	
	private final ProbabilityDensityFunction polarWarpingFunction;

	/**
	 * @param polarWarpingFunction
	 */
	public PerturbingSurfaceScatterer(
			ProbabilityDensityFunction polarWarpingFunction) {
		this.polarWarpingFunction = polarWarpingFunction;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, WavelengthPacket lambda, Random rnd) {
		
		Vector3 out;
		Vector3 N = x.getNormal();
		boolean fromSide = (v.dot(N) > 0.0);
		boolean toSide;
		Basis3 basis = Basis3.fromW(v);

		do {
			double theta = polarWarpingFunction.sample(rnd);
			double phi = 2.0 * Math.PI * rnd.next();
	
			SphericalCoordinates sc = new SphericalCoordinates(theta, phi);
			
			out = sc.toCartesian(basis);
			toSide = (out.dot(N) > 0.0);
		} while (fromSide != toSide);
		
		Ray3 ray = new Ray3(x.getPosition(), out);
		Color color = lambda.getColorModel().getWhite(lambda);
		return ScatteredRay.diffuse(ray, color, 0.0);
	}

}
