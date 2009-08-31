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

//	Color scattering(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda);
//	Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda);

	boolean isEmissive();

//	void scatter(SurfacePoint x, Vector3 v, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder);
//	void emit(SurfacePoint x, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder);

	ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint, WavelengthPacket lambda, Random rnd);
	ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda, Random rnd);

	double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out, boolean adjoint, WavelengthPacket lambda);
	double getEmissionPDF(SurfacePoint x, Vector3 out, WavelengthPacket lambda);

	Color bsdf(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda);
	Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda);


	/**
	 * A <code>Material</code> that absorbs all light and does not
	 * emit.
	 */
	public static final Material BLACK = new Material() {

		private static final long serialVersionUID = 4301103342747509476L;

		public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}

		public boolean isEmissive() {
			return false;
		}

		public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
			return lambda.getColorModel().getGray(Double.POSITIVE_INFINITY, lambda);
		}

		public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
			return lambda.getColorModel().getWhite(lambda);
		}

		public Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}

		public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
				WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}

		public ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda,
				Random rnd) {
			return null;
		}

		public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
				boolean adjoint, WavelengthPacket lambda) {
			return 0.0;
		}

		public double getEmissionPDF(SurfacePoint x, Vector3 out,
				WavelengthPacket lambda) {
			return 0.0;
		}

		public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
				WavelengthPacket lambda, Random rnd) {
			return null;
		}

	};

}
