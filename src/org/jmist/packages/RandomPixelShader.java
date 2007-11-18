/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.ImageShader;
import org.jmist.framework.PixelShader;
import org.jmist.framework.Random;
import org.jmist.toolkit.Box2;

/**
 * A rasterizing pixel shader that shades a random point within the
 * bounds of the pixel.
 * @author bkimmel
 */
public final class RandomPixelShader extends ImageRasterizingPixelShader implements
		PixelShader {

	/**
	 * Initializes the source of random numbers and the camera.
	 * @param random The source of random numbers to use.
	 * @param camera The camera to use to shade points on the image
	 * 		plane.
	 */
	public RandomPixelShader(Random random, ImageShader camera) {
		super(camera);
		this.random = random;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.PixelShader#shadePixel(org.jmist.toolkit.Box2, double[])
	 */
	public double[] shadePixel(Box2 bounds, double[] pixel) {
		return this.shadeAt(bounds.interpolate(random.next(), random.next()), pixel);
	}

	/** The source of random numbers for this pixel shader. */
	private final Random random;

}
