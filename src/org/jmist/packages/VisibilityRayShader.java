/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.RayShader;
import org.jmist.framework.VisibilityFunction3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Pixel;
import org.jmist.toolkit.Ray3;

/**
 * A ray shader that shades based on the evaluation of a visibility
 * function.
 * @author bkimmel
 */
public final class VisibilityRayShader implements RayShader {

	/**
	 * Initializes the visibility function to evaluate.
	 * @param visibilityFunction The visibility function to evaluate.
	 */
	public VisibilityRayShader(VisibilityFunction3 visibilityFunction) {
		this.visibilityFunction = visibilityFunction;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RayShader#shadeRay(org.jmist.toolkit.Ray3, org.jmist.toolkit.Pixel)
	 */
	public void shadeRay(Ray3 ray, Pixel pixel) {
		if (this.visibilityFunction.visibility(ray, Interval.POSITIVE)) {
			pixel.setAll(0.0);
		} else {
			pixel.setAll(1.0);
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.PixelFactory#createPixel()
	 */
	public Pixel createPixel() {
		// TODO Auto-generated method stub
		return null;
	}

	/** The visibility function to evaluate. */
	private final VisibilityFunction3 visibilityFunction;

}
