/**
 * 
 */
package org.jmist.packages.material;

import org.jmist.framework.AbstractMaterial;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;

/**
 * @author bkimmel
 *
 */
public final class ConductiveMaterial extends AbstractMaterial {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#extinctionIndex(org.jmist.toolkit.Point3)
	 */
	@Override
	public Spectrum extinctionIndex(Point3 p) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#refractiveIndex(org.jmist.toolkit.Point3)
	 */
	@Override
	public Spectrum refractiveIndex(Point3 p) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Medium#transmittance(org.jmist.toolkit.Ray3, double)
	 */
	@Override
	public Spectrum transmittance(Ray3 ray, double distance) {
		// TODO Auto-generated method stub
		return null;
	}

}
