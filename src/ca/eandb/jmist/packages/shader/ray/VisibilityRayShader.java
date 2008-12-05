/**
 *
 */
package ca.eandb.jmist.packages.shader.ray;

import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.toolkit.Interval;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.util.ArrayUtil;

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
		this.hitValue = 0.0;
		this.missValue = 1.0;
	}

	/**
	 * Initializes the visibility function to evaluate and the values to assign
	 * to rays that hit or do not hit an object.
	 * @param visibilityFunction The visibility function to evaluate.
	 * @param hitValue The value to assign to rays that hit an object.
	 * @param missValue The value to assign to rays that do not hit an object.
	 */
	public VisibilityRayShader(VisibilityFunction3 visibilityFunction,
			double hitValue, double missValue) {
		this.visibilityFunction = visibilityFunction;
		this.hitValue = hitValue;
		this.missValue = missValue;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.toolkit.Ray3, double[])
	 */
	public double[] shadeRay(Ray3 ray, double[] pixel) {
		pixel = ArrayUtil.initialize(pixel, 1);
		if (ray == null || this.visibilityFunction.visibility(ray, Interval.POSITIVE)) {
			pixel[0] = this.missValue;
		} else {
			pixel[0] = this.hitValue;
		}
		return pixel;
	}

	/** The visibility function to evaluate. */
	private final VisibilityFunction3 visibilityFunction;

	/** The value to assign to rays that hit an object. */
	private final double hitValue;

	/** The value to assign to rays that do not hit an object. */
	private final double missValue;

}
