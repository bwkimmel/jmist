/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IImageShader;
import org.jmist.framework.IPixelShader;
import org.jmist.framework.IRandom;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Pixel;

/**
 * A camera based pixel shader that shades a random point within the
 * bounds of the pixel.
 * @author bkimmel
 */
public final class RandomPixelShader extends CameraPixelShader implements
		IPixelShader {

	/**
	 * Initializes the source of random numbers and the camera.
	 * @param random The source of random numbers to use.
	 * @param camera The camera to use to shade points on the image
	 * 		plane.
	 */
	public RandomPixelShader(IRandom random, IImageShader camera) {
		super(camera);
		this.random = random;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IPixelShader#shadePixel(org.jmist.toolkit.Box2, org.jmist.toolkit.Pixel)
	 */
	public void shadePixel(Box2 bounds, Pixel pixel) {
		this.shadeAt(bounds.interpolate(random.next(), random.next()), pixel);
	}

	/** The source of random numbers for this pixel shader. */
	private final IRandom random;

}
