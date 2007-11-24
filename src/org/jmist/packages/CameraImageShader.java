/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.ImageShader;
import org.jmist.framework.Lens;
import org.jmist.framework.RayShader;
import org.jmist.toolkit.Point2;

/**
 * An image shader that uses a Lens to shade rays corresponding to points
 * on the image plane.
 * @author bkimmel
 */
public final class CameraImageShader implements ImageShader {

	/**
	 * Initializes the lens and ray shader to use to shade points on
	 * the image plane.
	 * @param lens The lens to use to generate rays corresponding to
	 * 		points on the image plane.
	 * @param rayShader The shader to use to shade rays.
	 */
	public CameraImageShader(Lens lens, RayShader rayShader) {
		this.lens = lens;
		this.rayShader = rayShader;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ImageShader#shadeAt(org.jmist.toolkit.Point2, double[])
	 */
	public double[] shadeAt(Point2 p, double[] pixel) {
		return this.rayShader.shadeRay(this.lens.rayAt(p), pixel);
	}

	/**
	 * The lens to use to obtain rays corresponding to points on the
	 * image plane.
	 */
	private final Lens lens;

	/** The shader to use to shade rays. */
	private final RayShader rayShader;

}
