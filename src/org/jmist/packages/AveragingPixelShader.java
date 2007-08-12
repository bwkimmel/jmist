/**
 * 
 */
package org.jmist.packages;

import org.jmist.framework.IPixelShader;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Pixel;

/**
 * A pixel shader decorator that averages the results of another
 * pixel shader.
 * @author bkimmel
 */
public final class AveragingPixelShader implements IPixelShader {

	/**
	 * Initializes the inner pixel shader.
	 * @param numSamples The number of samples to average when shading
	 * 		a pixel.
	 * @param pixelShader The pixel shader average the results from.
	 */
	public AveragingPixelShader(int numSamples, IPixelShader pixelShader) {
		this.numSamples = numSamples;
		this.pixelShader = pixelShader;
	}
	
	/* (non-Javadoc)
	 * @see org.jmist.framework.IPixelShader#shadePixel(org.jmist.toolkit.Box2, org.jmist.toolkit.Pixel)
	 */
	public void shadePixel(Box2 bounds, Pixel pixel) {
		Pixel sample = this.createPixel();

		pixel.reset();
		for (int i = 0; i < this.numSamples; i++) {
			this.pixelShader.shadePixel(bounds, sample);
			pixel.add(sample);
		}
		
		pixel.scale(1.0 / (double) this.numSamples);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IPixelFactory#createPixel()
	 */
	public Pixel createPixel() {
		return this.pixelShader.createPixel();
	}
	
	/**
	 * The number of samples to average from the decorated pixel
	 * shader.
	 */
	private final int numSamples;
	
	/** The pixel shader from which to average the results. */
	private final IPixelShader pixelShader;

}
