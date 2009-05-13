/**
 *
 */
package ca.eandb.jmist.framework.shader.pixel;

import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * A pixel shader decorator that averages the results of another
 * pixel shader.
 * @author Brad Kimmel
 */
public final class AveragingPixelShader implements PixelShader {

	/**
	 * Initializes the inner pixel shader.
	 * @param numSamples The number of samples to average when shading
	 * 		a pixel.
	 * @param pixelShader The pixel shader average the results from.
	 */
	public AveragingPixelShader(int numSamples, PixelShader pixelShader) {
		if (numSamples <= 0) {
			throw new IllegalArgumentException("numSamples <= 0");
		}
		this.numSamples = numSamples;
		this.pixelShader = pixelShader;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.PixelShader#shadePixel(ca.eandb.jmist.toolkit.Box2, ca.eandb.jmist.toolkit.Pixel)
	 */
	public double[] shadePixel(Box2 bounds, double[] pixel) {
		double[] sample = null;

		ArrayUtil.reset(pixel);

		for (int i = 0; i < this.numSamples; i++) {
			sample = this.pixelShader.shadePixel(bounds, sample);
			pixel = MathUtil.add(pixel, sample);
		}

		return MathUtil.scale(pixel, 1.0 / (double) this.numSamples);
	}

	/**
	 * The number of samples to average from the decorated pixel
	 * shader.
	 */
	private final int numSamples;

	/** The pixel shader from which to average the results. */
	private final PixelShader pixelShader;

}
