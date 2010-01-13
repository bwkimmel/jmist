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
	
	public void addLayer(SurfaceScatterer layer) {
		layers.add(layer);
	}
	
	public void removeAllLayers() {
		layers.clear();
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, Random rnd) {

		Type type = Type.SPECULAR;
		Color color = lambda.getColorModel().getWhite(lambda);
		int depth = 0;
		int dir = 1; // downward
		
		while (depth >= 0 && depth < layers.size()) {
		
			SurfaceScatterer layer = layers.get(depth);
			ScatteredRay sr = layer.scatter(x, v, adjoint, lambda, rnd);
			
			if (sr == null) { // absorbed
				return null;
			}

			if (!sr.isTransmitted()) { // reflected, reverse direction
				dir = -dir;
			}
			
			depth += dir;
			v = sr.getRay().direction();
			color = color.times(sr.getColor());
			
			if (sr.getType() == Type.DIFFUSE) {
				type = Type.DIFFUSE;
			} else if (sr.getType() == Type.GLOSSY && type != Type.DIFFUSE){
				type = Type.GLOSSY;
			}
		}
		
		Ray3 ray = new Ray3(x.getPosition(), v);
		
		return new ScatteredRay(ray, color, type, 0.0, depth >= 0);	
	}	
	
}
