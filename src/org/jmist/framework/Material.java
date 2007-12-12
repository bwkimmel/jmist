/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Tuple;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public interface Material extends Medium {

	Spectrum scattering(Intersection x, Vector3 out);
	Spectrum emission(SurfacePoint x, Vector3 out);

	void scatter(Intersection x, Tuple wavelengths, ScatterRecorder recorder);
	void emit(SurfacePoint x, Tuple wavelengths, ScatterRecorder recorder);

	/**
	 * A <code>Material</code> that absorbs all light and does not
	 * emit.
	 */
	public static final Material BLACK = new Material() {

		/* (non-Javadoc)
		 * @see org.jmist.framework.Material#emission(org.jmist.framework.SurfacePoint, org.jmist.toolkit.Vector3)
		 */
		@Override
		public Spectrum emission(SurfacePoint x, Vector3 out) {
			return Spectrum.ZERO;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Material#emit(org.jmist.framework.SurfacePoint, org.jmist.toolkit.Tuple, org.jmist.framework.ScatterRecorder)
		 */
		@Override
		public void emit(SurfacePoint x, Tuple wavelengths,
				ScatterRecorder recorder) {
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Material#scatter(org.jmist.framework.Intersection, org.jmist.toolkit.Tuple, org.jmist.framework.ScatterRecorder)
		 */
		@Override
		public void scatter(Intersection x, Tuple wavelengths,
				ScatterRecorder recorder) {
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Material#scattering(org.jmist.framework.Intersection, org.jmist.toolkit.Vector3)
		 */
		@Override
		public Spectrum scattering(Intersection x, Vector3 out) {
			return Spectrum.ZERO;
		}

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

	};

}
