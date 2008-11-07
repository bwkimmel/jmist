/**
 *
 */
package org.jmist.packages.shader.ray;

import org.jmist.framework.Intersection;
import org.jmist.framework.RayCaster;
import org.jmist.framework.RayShader;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Ray3;
import org.jmist.util.ArrayUtil;

/**
 * A ray shader that shades a ray according to the distance to the
 * nearest intersection along the ray.
 * @author bkimmel
 */
public final class DistanceRayShader implements RayShader {

	/**
	 * Initializes the ray caster to use.
	 * @param caster The ray caster to use.
	 */
	public DistanceRayShader(RayCaster caster) {
		this.caster = caster;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RayShader#shadeRay(org.jmist.toolkit.Ray3, double[])
	 */
	public double[] shadeRay(Ray3 ray, double[] pixel) {
		Intersection x = this.caster.castRay(ray, Interval.POSITIVE);

		pixel = ArrayUtil.initialize(pixel, 2);

		if (x != null) {
			pixel[0] = x.distance();
			pixel[1] = 1.0;
		}

		return pixel;
	}

	/** The ray caster to use. */
	private final RayCaster caster;

}
