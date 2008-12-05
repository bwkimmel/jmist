/**
 *
 */
package ca.eandb.jmist.packages.shader.pixel;

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.toolkit.Box2;

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
	 * @see ca.eandb.jmist.framework.PixelShader#shadePixel(ca.eandb.jmist.toolkit.Box2, double[])
	 */
	public double[] shadePixel(Box2 bounds, double[] pixel) {
		return this.shadeAt(bounds.interpolate(random.next(), random.next()), pixel);
	}

	/** The source of random numbers for this pixel shader. */
	private final Random random;

}
