/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Ray3;

/**
 * Estimates the colour channel responses along a given ray.
 * @author Brad Kimmel
 */
public interface RayShader {

	/**
	 * Computes an estimate of the colour channel responses at the origin
	 * of the ray traveling in the direction opposite the direction of
	 * the ray.
	 * @param ray The ray indicating the point and direction along which to
	 * 		compute the colour channel responses.
	 * @param lambda The wavelengths at which to compute the color channel
	 * 		responses.
	 * @return The colour channel responses.
	 */
	Color shadeRay(Ray3 ray, WavelengthPacket lambda);

	/**
	 * A <code>RayShader</code> that shades all rays black.
	 */
	public static final RayShader BLACK = new RayShader() {
		public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
			return lambda.getColorModel().getBlack(lambda);
		}
	};

}
