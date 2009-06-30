/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public abstract class OpaqueMaterial extends AbstractMaterial {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
		return lambda.getColorModel().getGray(Double.POSITIVE_INFINITY, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
		return lambda.getColorModel().getWhite(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda) {
		return lambda.getColorModel().getBlack(lambda);
	}

}
