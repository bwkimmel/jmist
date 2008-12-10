/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.Point3;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Tuple;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public interface Material extends Medium {

	Spectrum scattering(Intersection x, Vector3 out);
	Spectrum emission(SurfacePoint x, Vector3 out);

	boolean isEmissive();

	void scatter(Intersection x, Tuple wavelengths, ScatterRecorder recorder);
	void emit(SurfacePoint x, Tuple wavelengths, ScatterRecorder recorder);

	/**
	 * A <code>Material</code> that absorbs all light and does not
	 * emit.
	 */
	public static final Material BLACK = new Material() {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.toolkit.Vector3)
		 */
		public Spectrum emission(SurfacePoint x, Vector3 out) {
			return Spectrum.ZERO;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.toolkit.Tuple, ca.eandb.jmist.framework.ScatterRecorder)
		 */
		public void emit(SurfacePoint x, Tuple wavelengths,
				ScatterRecorder recorder) {
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
		public void scatter(Intersection x, Tuple wavelengths,
				ScatterRecorder recorder) {
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Material#scattering(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.toolkit.Vector3)
		 */
		public Spectrum scattering(Intersection x, Vector3 out) {
			return Spectrum.ZERO;
		}

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

	};

}
