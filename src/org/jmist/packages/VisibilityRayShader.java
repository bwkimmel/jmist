/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IRayShader;
import org.jmist.framework.IVisibilityFunction3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Pixel;
import org.jmist.toolkit.Ray3;

/**
 * A ray shader that shades based on the evaluation of a visibility
 * function.
 * @author bkimmel
 */
public final class VisibilityRayShader implements IRayShader {

	/**
	 * Initializes the visibility function to evaluate.
	 * @param visibilityFunction The visibility function to evaluate.
	 */
	public VisibilityRayShader(IVisibilityFunction3 visibilityFunction) {
		this.visibilityFunction = visibilityFunction;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IRayShader#shadeRay(org.jmist.toolkit.Ray3, org.jmist.toolkit.Pixel)
	 */
	public void shadeRay(Ray3 ray, Pixel pixel) {
		if (this.visibilityFunction.visibility(ray, Interval.POSITIVE)) {
			// TODO set pixel off
		} else {
			// TODO set pixel on
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IPixelFactory#createPixel()
	 */
	public Pixel createPixel() {
		// TODO Auto-generated method stub
		return null;
	}

	/** The visibility function to evaluate. */
	private final IVisibilityFunction3 visibilityFunction;

}
