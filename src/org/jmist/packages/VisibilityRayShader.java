/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.RayShader;
import org.jmist.framework.VisibilityFunction3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Ray3;
import org.jmist.util.ArrayUtil;

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
	 * @see org.jmist.framework.RayShader#shadeRay(org.jmist.toolkit.Ray3, double[])
	 */
	public double[] shadeRay(Ray3 ray, double[] pixel) {
		pixel = ArrayUtil.initialize(pixel, 1);
		if (this.visibilityFunction.visibility(ray, Interval.POSITIVE)) {
			pixel[0] = 1.0;
		} else {
			pixel[0] = 0.0;
		}
		return pixel;
	}

	/** The visibility function to evaluate. */
	private final VisibilityFunction3 visibilityFunction;

}
