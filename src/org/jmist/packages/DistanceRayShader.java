/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Intersection;
import org.jmist.framework.RayCaster;
import org.jmist.framework.RayShader;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Pixel;
import org.jmist.toolkit.Ray3;

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
	 * @see org.jmist.framework.RayShader#shadeRay(org.jmist.toolkit.Ray3, org.jmist.toolkit.Pixel)
	 */
	public void shadeRay(Ray3 ray, Pixel pixel) {
		Intersection intersection = this.caster.castRay(ray, Interval.POSITIVE);

		// TODO finish implementing DistanceRayShader.shadeRay.
		if (intersection != null) {
			//intersection.getRayParameter();
		} else {
			//0.0;
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.PixelFactory#createPixel()
	 */
	public Pixel createPixel() {
		// TODO Auto-generated method stub
		return null;
	}

	/** The ray caster to use. */
	private final RayCaster caster;

}
