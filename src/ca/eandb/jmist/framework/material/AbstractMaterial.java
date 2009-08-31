/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
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
	 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda, Random rng) {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#isEmissive()
	 */
	public boolean isEmissive() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint, WavelengthPacket lambda, Random rng) {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda) {
		return lambda.getColorModel().getBlack(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#pdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda) {
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#getEmissionPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public double getEmissionPDF(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
		return 0.0;
	}

}
