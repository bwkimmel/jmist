/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.RayShader;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;
import org.jmist.util.ArrayUtil;

/**
 * A test ray shader that shades rays based on their direction.
 * @author bkimmel
 */
public final class DirectionalTestRayShader implements RayShader {

	/* (non-Javadoc)
	 * @see org.jmist.framework.RayShader#shadeRay(org.jmist.toolkit.Ray3, double[])
	 */
	public double[] shadeRay(Ray3 ray, double[] pixel) {

		Vector3 V = ray.direction().unit();

		pixel = ArrayUtil.initialize(pixel, 3);
		pixel[0] = 1.0 + V.x() / 2.0;
		pixel[1] = 1.0 + V.y() / 2.0;
		pixel[2] = 1.0 + V.z() / 2.0;

		return pixel;

	}

}
