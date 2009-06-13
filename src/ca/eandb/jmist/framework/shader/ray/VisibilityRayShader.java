/**
 *
 */
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Ray3;

/**
 * A ray shader that shades based on the evaluation of a visibility
 * function.
 * @author Brad Kimmel
 */
public final class VisibilityRayShader implements RayShader {

	/**
	 * Initializes the visibility function to evaluate.
	 * @param visibilityFunction The visibility function to evaluate.
	 */
	public VisibilityRayShader(VisibilityFunction3 visibilityFunction) {
		this(visibilityFunction, ColorModel.getInstance().getWhite(), ColorModel
				.getInstance().getBlack());
	}

	/**
	 * Initializes the visibility function to evaluate and the values to assign
	 * to rays that hit or do not hit an object.
	 * @param visibilityFunction The visibility function to evaluate.
	 * @param hitValue The <code>Color</code> to assign to rays that hit an
	 * 		object.
	 * @param missValue The <code>Color</code> to assign to rays that do not
	 * 		hit an object.
	 */
	public VisibilityRayShader(VisibilityFunction3 visibilityFunction,
			Color hitValue, Color missValue) {
		this.visibilityFunction = visibilityFunction;
		this.hitValue = hitValue;
		this.missValue = missValue;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.math.Ray3)
	 */
	public Color shadeRay(Ray3 ray) {
		if (ray == null || this.visibilityFunction.visibility(ray)) {
			return missValue;
		} else {
			return hitValue;
		}
	}

	/** The visibility function to evaluate. */
	private final VisibilityFunction3 visibilityFunction;

	/** The value to assign to rays that hit an object. */
	private final Color hitValue;

	/** The value to assign to rays that do not hit an object. */
	private final Color missValue;

}
