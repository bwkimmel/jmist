/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public interface Medium {

	Color transmittance(Ray3 ray, double distance);
	Color refractiveIndex(Point3 p);
	Color extinctionIndex(Point3 p);

	/**
	 * A vacuum <code>Medium</code>.
	 */
	public static final Medium VACUUM = new Medium() {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.toolkit.Point3)
		 */
		public Color extinctionIndex(Point3 p) {
			return ColorModel.getInstance().getBlack();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.toolkit.Point3)
		 */
		public Color refractiveIndex(Point3 p) {
			return ColorModel.getInstance().getUnit();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.toolkit.Ray3, double)
		 */
		public Color transmittance(Ray3 ray, double distance) {
			return ColorModel.getInstance().getUnit();
		}

	};

}
