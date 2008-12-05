/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.Ray3;

/**
 * Estimates the colour channel responses along a given ray.
 * @author bkimmel
 */
public interface RayShader {

	/**
	 * Computes an estimate of the colour channel responses at the origin
	 * of the ray travelling in the direction opposite the direction of
	 * the ray.
	 * @param ray The ray indicating the point and direction along which to
	 * 		compute the colour channel responses.
	 * @param pixel The array to populate with the colour channel responses (if
	 * 		null, the array will be created by this method).
	 * @return The colour channel responses.
	 */
	double[] shadeRay(Ray3 ray, double[] pixel);

}
