/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRayRecorder;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * Provides default implementations for a <code>Material</code>.
 * @author Brad Kimmel
 */
public abstract class AbstractMaterial implements Material {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
		return lambda.getColorModel().getBlack(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	public void emit(SurfacePoint x, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#isEmissive()
	 */
	public boolean isEmissive() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	public void scatter(SurfacePoint x, Vector3 v, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scattering(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color scattering(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda) {
		return lambda.getColorModel().getBlack(lambda);
	}

}
