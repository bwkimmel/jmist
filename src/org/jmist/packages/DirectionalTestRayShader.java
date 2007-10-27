/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.RayShader;
import org.jmist.toolkit.Pixel;
import org.jmist.toolkit.PixelFormat;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;

/**
 * A test ray shader that shades rays based on their direction.
 * @author bkimmel
 */
public final class DirectionalTestRayShader implements RayShader {

	/* (non-Javadoc)
	 * @see org.jmist.framework.RayShader#shadeRay(org.jmist.toolkit.Ray3, org.jmist.toolkit.Pixel)
	 */
	public void shadeRay(Ray3 ray, Pixel pixel) {

		Vector3 V = ray.direction().unit();

		pixel.setAt(0, (1.0 + V.x()) / 2.0);
		pixel.setAt(1, (1.0 + V.y()) / 2.0);
		pixel.setAt(2, (1.0 + V.z()) / 2.0);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.PixelFactory#createPixel()
	 */
	public Pixel createPixel() {
		return new Pixel(PixelFormat.RGB);
	}

}
