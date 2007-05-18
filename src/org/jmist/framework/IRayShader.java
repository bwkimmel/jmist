/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IRayShader {

	/**
	 * Computes an estimate of the spectral radiance at the origin of
	 * the ray travelling in the direction opposite the direction of
	 * the ray.
	 * @param ray The ray indicating the point and direction along
	 * 		which to compute the spectral radiance.
	 * @param wavelength The wavelength (in meters) at which to
	 * 		evaluate the spectral radiance.
	 * @return The spectral radiance (in
	 * 		W&middot;sr<sup>-1</sup>&middot;m<sup>-3</sup>).
	 */
	double shadeRay(Ray3 ray, double wavelength);

}
