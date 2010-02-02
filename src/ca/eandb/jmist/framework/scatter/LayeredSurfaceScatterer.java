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
public final class LayeredSurfaceScatterer implements SurfaceScatterer {

	private final List<SurfaceScatterer> layers = new ArrayList<SurfaceScatterer>();

	public LayeredSurfaceScatterer addLayerToTop(SurfaceScatterer e) {
		layers.add(0, e);
		return this;
	}
	
	public LayeredSurfaceScatterer addLayerToBottom(SurfaceScatterer e) {
		layers.add(e);
		return this;
	}
	
	public void clear() {
		layers.clear();
	}
	
	public int getNumLayers() {
		return layers.size();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double lambda, Random rnd) {

		Vector3 N = x.getNormal();
		int	depth = (v.dot(N) > 0.0) ? (layers.size() - 1) : 0;
		int dir;
		
		do  {

			SurfaceScatterer layer = layers.get(depth);
			v = layer.scatter(x, v, adjoint, lambda, rnd);
			
			if (v == null) break;
		
			dir = (v.dot(N) > 0.0) ? -1 : 1;
			depth += dir;

		} while (depth >= 0 && depth < layers.size());
		
		return v;
	}	
	
}
