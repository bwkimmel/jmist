/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Pixel;
import org.jmist.toolkit.Ray3;

/**
 * Estimates the colour channel responses along a given ray.
 * @author bkimmel
 */
public interface RayShader extends PixelFactory {

	/**
	 * Computes an estimate of the colour channel responses at the origin
	 * of the ray travelling in the direction opposite the direction of
	 * the ray.
	 * @param ray The ray indicating the point and direction along which to
	 * 		compute the colour channel responses.
	 * @param pixel The colour channel responses.
	 */
	void shadeRay(Ray3 ray, Pixel pixel);

}
