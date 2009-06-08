/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public interface Material extends Medium {

	Color scattering(Intersection x, Vector3 out);
	Color emission(SurfacePoint x, Vector3 out);

	boolean isEmissive();

	void scatter(Intersection x, ScatterRecorder recorder);
	void emit(SurfacePoint x, ScatterRecorder recorder);

	/**
	 * A <code>Material</code> that absorbs all light and does not
	 * emit.
	 */
	public static final Material BLACK = new Material() {

		@Override
		public Color emission(SurfacePoint x, Vector3 out) {
			return ColorModel.getInstance().getBlack();
		}

		@Override
		public void emit(SurfacePoint x, ScatterRecorder recorder) {
			/* nothing to do. */
		}

		@Override
		public boolean isEmissive() {
			return false;
		}

		@Override
		public void scatter(Intersection x, ScatterRecorder recorder) {
			/* nothing to do. */
		}

		@Override
		public Color scattering(Intersection x, Vector3 out) {
			return ColorModel.getInstance().getBlack();
		}

		@Override
		public Color extinctionIndex(Point3 p) {
			return ColorModel.getInstance().getGray(Double.POSITIVE_INFINITY);
		}

		@Override
		public Color refractiveIndex(Point3 p) {
			return ColorModel.getInstance().getWhite();
		}

		@Override
		public Color transmittance(Ray3 ray, double distance) {
			return ColorModel.getInstance().getBlack();
		}

	};

}
