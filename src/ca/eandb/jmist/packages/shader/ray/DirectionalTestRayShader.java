/**
 *
 */
package ca.eandb.jmist.packages.shader.ray;

import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * A test ray shader that shades rays based on their direction.
 * @author bkimmel
 */
public final class DirectionalTestRayShader implements RayShader {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.toolkit.Ray3, double[])
	 */
	public double[] shadeRay(Ray3 ray, double[] pixel) {

		pixel = ArrayUtil.initialize(pixel, 3);

		if (ray != null) {

			Vector3 V = ray.direction().unit();

			pixel[0] = 1.0 + V.x() / 2.0;
			pixel[1] = 1.0 + V.y() / 2.0;
			pixel[2] = 1.0 + V.z() / 2.0;

		}

		return pixel;

	}

}
