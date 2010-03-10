/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class SequentialSurfaceScatterer implements SurfaceScatterer {

	private final List<SurfaceScatterer> inner = new ArrayList<SurfaceScatterer>();
	
	public SequentialSurfaceScatterer addScatterer(SurfaceScatterer e) {
		inner.add(e);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, double lambda, Random rnd) {
		
		for (SurfaceScatterer e : inner) {
			v = e.scatter(x, v, adjoint, lambda, rnd);
			if (v == null) break;
		}

		return v;
	}

}