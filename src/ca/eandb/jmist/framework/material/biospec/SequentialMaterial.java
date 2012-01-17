/**
 * 
 */
package ca.eandb.jmist.framework.material.biospec;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class SequentialMaterial extends OpaqueMaterial {

	/** Serialization version ID. */
	private static final long serialVersionUID = -6825571743685716845L;

	private final List<Material> inner = new ArrayList<Material>();
	
	private final Random rnd = new ThreadLocalRandom(new SimpleRandom());
	
	public SequentialMaterial addScatterer(Material e) {
		inner.add(e);
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {
		
		ScatteredRay sr = null;
		Color col = lambda.getColorModel().getWhite(lambda);
		
		for (Material e : inner) {
			sr = e.scatter(x, v, adjoint, lambda, rnd.next(), rnd.next(), rnd.next());
			if (sr == null) break;
			v = sr.getRay().direction();
			col = col.times(sr.getColor());
		}
		
		if (sr == null) {
			return null;
		} else if (sr.getRay().direction().dot(x.getNormal()) < 0.0) {
			return ScatteredRay.diffuse(sr.getRay(), col, 1.0);
		} else {
			return ScatteredRay.transmitDiffuse(sr.getRay(), col, 1.0);
		}
	}

}
