/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;

/**
 * @author bkimmel
 *
 */
public abstract class OpaqueMaterial extends AbstractMaterial {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#extinctionIndex(org.jmist.toolkit.Point3)
	 */
	@Override
	public Spectrum extinctionIndex(Point3 p) {
		return Spectrum.POSITIVE_INFINITY;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#refractiveIndex(org.jmist.toolkit.Point3)
	 */
	@Override
	public Spectrum refractiveIndex(Point3 p) {
		return Spectrum.ONE;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#transmittance(org.jmist.toolkit.Ray3, double)
	 */
	@Override
	public Spectrum transmittance(Ray3 ray, double distance) {
		return Spectrum.ZERO;
	}

}
