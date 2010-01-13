/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author bwkimmel
 *
 */
public final class DiffusingSurfaceScatterer implements SurfaceScatterer {

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
