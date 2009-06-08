/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
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
	 * @return The colour channel responses.
	 */
	Color shadeRay(Ray3 ray);

}
