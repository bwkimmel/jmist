/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public abstract class OpaqueMaterial extends AbstractMaterial {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3)
	 */
	public Color extinctionIndex(Point3 p) {
		return ColorModel.getInstance().getGray(Double.POSITIVE_INFINITY);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3)
	 */
	public Color refractiveIndex(Point3 p) {
		return ColorModel.getInstance().getWhite();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double)
	 */
	public Color transmittance(Ray3 ray, double distance) {
		return ColorModel.getInstance().getBlack();
	}

}
