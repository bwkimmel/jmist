/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.Tuple;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * Provides default implementations for a <code>Material</code>.
 * @author Brad Kimmel
 */
public abstract class AbstractMaterial implements Material {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.toolkit.Vector3)
	 */
	public Spectrum emission(SurfacePoint x, Vector3 out) {
		return Spectrum.ZERO;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.toolkit.Tuple, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	public void emit(SurfacePoint x, Tuple wavelengths, ScatterRecorder recorder) {
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#isEmissive()
	 */
	public boolean isEmissive() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.toolkit.Tuple, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	public void scatter(Intersection x, Tuple wavelengths, ScatterRecorder recorder) {
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scattering(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.toolkit.Vector3)
	 */
	public Spectrum scattering(Intersection x, Vector3 out) {
		return Spectrum.ZERO;
	}

}
