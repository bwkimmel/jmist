/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public interface Material extends Medium, Serializable {

	Color scattering(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda);
	Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda);

	boolean isEmissive();

	void scatter(SurfacePoint x, Vector3 v, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder);
	void emit(SurfacePoint x, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder);

	/**
	 * A <code>Material</code> that absorbs all light and does not
	 * emit.
	 */
	public static final Material BLACK = new Material() {

		private static final long serialVersionUID = 4301103342747509476L;

		@Override
		public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}

		@Override
		public void emit(SurfacePoint x, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder) {
			/* nothing to do. */
		}

		@Override
		public boolean isEmissive() {
			return false;
		}

		@Override
		public void scatter(SurfacePoint x, Vector3 in, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder) {
			/* nothing to do. */
		}

		@Override
		public Color scattering(SurfacePoint x, Vector3 v, Vector3 out, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack().sample(lambda);
		}

		@Override
		public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
			return lambda.getColorModel().getGray(Double.POSITIVE_INFINITY, lambda);
		}

		@Override
		public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
			return lambda.getColorModel().getWhite(lambda);
		}

		@Override
		public Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}

	};

}
