/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public interface Medium {

	Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda);
	Color refractiveIndex(Point3 p, WavelengthPacket lambda);
	Color extinctionIndex(Point3 p, WavelengthPacket lambda);

	/**
	 * A vacuum <code>Medium</code>.
	 */
	public static final Medium VACUUM = new Medium() {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
		 */
		public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
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
			return lambda.getColorModel().getWhite(lambda);
		}

	};

}
