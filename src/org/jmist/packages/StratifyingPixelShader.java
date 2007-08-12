/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IPixelShader;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Pixel;

/**
 * A pixel shader decorator that averages stratified samples from the
 * decorated pixel shader.
 * @author bkimmel
 */
public final class StratifyingPixelShader implements IPixelShader {

	/**
	 * Initializes the number of rows and columns to divide each pixel
	 * into as well as the underlying decorated pixel shader.
	 * @param columns The number of columns to divide each pixel into.
	 * @param rows The number of rows to divide each pixel into.
	 * @param pixelShader The decorated pixel shader.
	 */
	public StratifyingPixelShader(int columns, int rows, IPixelShader pixelShader) {
		this.columns = columns;
		this.rows = rows;
		this.pixelShader = pixelShader;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IPixelShader#shadePixel(org.jmist.toolkit.Box2, org.jmist.toolkit.Pixel)
	 */
	public void shadePixel(Box2 bounds, Pixel pixel) {
		double x0, x1, y0, y1;
		Pixel sample = this.createPixel();
		Box2 subpixel;

		pixel.reset();
		for (int i = 0; i < this.rows; i++) {
			x0 = bounds.interpolateX((double) i / (double) this.rows);
			x1 = bounds.interpolateX((double) (i + 1) / (double) this.rows);

			for (int j = 0; j < this.columns; j++) {
				y0 = bounds.interpolateY((double) j / (double) this.columns);
				y1 = bounds.interpolateY((double) (j + 1) / (double) this.columns);

				subpixel = new Box2(x0, y0, x1, y1);
				this.pixelShader.shadePixel(subpixel, sample);
				pixel.add(sample);
			}
		}

		pixel.scale(1.0 / (double) (this.rows * this.columns));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IPixelFactory#createPixel()
	 */
	public Pixel createPixel() {
		return this.pixelShader.createPixel();
	}

	/** The number of columns to divide each pixel into. */
	private final int columns;

	/** The number of rows to divide each pixel into. */
	private final int rows;

	/** The pixel shader to average the results from. */
	private final IPixelShader pixelShader;

}
