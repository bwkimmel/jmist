/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;


/**
 * A light source.
 * @author Brad Kimmel
 */
public interface Light extends Serializable {

	/**
	 * Causes the <code>Light</code> to record light samples on the provided
	 * <code>SurfacePoint</code>.
	 * @param x The <code>SurfacePoint</code> to illuminate.
	 * @param lambda The <code>WavelengthPacket</code> at which to emit.
	 * @param rnd A <code>Random</code> number generator to use.
	 * @param target The <code>Illuminable</code> to record the light samples.
	 */
	void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rnd, Illuminable target);

	/**
	 * Creates the terminal <code>LightNode</code> for path-integral based
	 * rendering algorithms.
	 * @param pathInfo The <code>PathInfo</code> describing the context in
	 * 		which the path is being generated.
	 * @param ru The first random variable (must be in [0, 1]).
	 * @param rv The second random variable (must be in [0, 1]).
	 * @param rj The third random variable (must be in [0, 1]).
	 * @return A new <code>LightNode</code>.
	 */
	LightNode sample(PathInfo pathInfo, double ru, double rv, double rj);

	/**
	 * Emits a light ray.
	 * @param lambda The <code>WavelengthPacket</code> at which to emit.
	 * @param rnd The <code>Random</code> number generator to use.
	 * @return A <code>ScatteredRay</code> describing the direction and color
	 * 		of the emitted light.
	 */
	ScatteredRay emit(WavelengthPacket lambda, Random rnd);

	/**
	 * Gets the marginal probability that this <code>Light</code> will generate
	 * a <code>LightNode</code> from the specified <code>SurfacePoint</code>.
	 * @param x The <code>SurfacePoint</code> at which to evaluate the
	 * 		probability density function.
	 * @param pathInfo The <code>PathInfo</code> describing the context in
	 * 		in which the path is generated.
	 * @return The value of the PDF.
	 * @see #sample(PathInfo, double, double, double)
	 */
	double getSamplePDF(SurfacePoint x, PathInfo pathInfo);

	/** A dummy <code>Light</code> that emits no illumination. */
	public static final Light NULL = new Light() {
		private static final long serialVersionUID = 5058166013868688853L;
		public ScatteredRay emit(WavelengthPacket lambda, Random rnd) {
			return null;
		}
		public void illuminate(SurfacePoint x, WavelengthPacket lambda,
				Random rng, Illuminable target) {
		}
		public LightNode sample(PathInfo pathInfo, double ru, double rv,
				double rj) {
			return null;
		}
		public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
			return 0;
		}
	};

}
