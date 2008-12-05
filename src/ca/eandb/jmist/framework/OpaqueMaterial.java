/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.Point3;
import ca.eandb.jmist.toolkit.Ray3;

/**
 * @author bkimmel
 *
 */
public abstract class OpaqueMaterial extends AbstractMaterial {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.toolkit.Point3)
	 */
	public Spectrum extinctionIndex(Point3 p) {
		return Spectrum.POSITIVE_INFINITY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.toolkit.Point3)
	 */
	public Spectrum refractiveIndex(Point3 p) {
		return Spectrum.ONE;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.toolkit.Ray3, double)
	 */
	public Spectrum transmittance(Ray3 ray, double distance) {
		return Spectrum.ZERO;
	}

}
