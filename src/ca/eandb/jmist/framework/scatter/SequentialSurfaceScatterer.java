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
	
	public void addScatterer(SurfaceScatterer e) {
		inner.add(e);
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, WavelengthPacket lambda, Random rnd) {

		Color color = lambda.getColorModel().getWhite(lambda);
		Type type = Type.SPECULAR;
		
		for (SurfaceScatterer e : inner) {
			ScatteredRay sr = e.scatter(x, v, adjoint, lambda, rnd);
			if (sr == null) {
				return null;
			}
			assert(sr.isTransmitted());

			color = color.times(sr.getColor());
			v = sr.getRay().direction();
			
			if (sr.getType() == Type.DIFFUSE) {
				type = Type.DIFFUSE;
			} else if (sr.getType() == Type.GLOSSY && type != Type.DIFFUSE) {
				type = Type.GLOSSY;
			}
		}
		
		Ray3 ray = new Ray3(x.getPosition(), v);
		return new ScatteredRay(ray, color, type, 0.0, true);
		
	}

}
