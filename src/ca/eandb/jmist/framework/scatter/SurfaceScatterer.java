/**
 * 
 */
package ca.eandb.jmist.framework.scatter;

import java.io.Serializable;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public interface SurfaceScatterer extends Serializable {
	
	ScatteredRay scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, Random rnd);
	
}
