/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.PixelShader;
import org.jmist.toolkit.Box2;
import org.jmist.util.MathUtil;

/**
 * A pixel shader decorator that averages stratified samples from the
 * decorated pixel shader.
 * @author bkimmel
 */
public final class StratifyingPixelShader implements PixelShader {

	/**
	 * Initializes the number of rows and columns to divide each pixel
	 * into as well as the underlying decorated pixel shader.
	 * @param columns The number of columns to divide each pixel into.
	 * @param rows The number of rows to divide each pixel into.
	 * @param pixelShader The decorated pixel shader.
	 */
	public StratifyingPixelShader(int columns, int rows, PixelShader pixelShader) {
		this.columns = columns;
		this.rows = rows;
		this.pixelShader = pixelShader;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.PixelShader#shadePixel(org.jmist.toolkit.Box2, double[])
	 */
	public double[] shadePixel(Box2 bounds, double[] pixel) {
		double x0, x1, y0, y1;
		double[] sample = null;
		Box2 subpixel;

		for (int i = 0; i < this.rows; i++) {
			x0 = bounds.interpolateX((double) i / (double) this.rows);
			x1 = bounds.interpolateX((double) (i + 1) / (double) this.rows);

			for (int j = 0; j < this.columns; j++) {
				y0 = bounds.interpolateY((double) j / (double) this.columns);
				y1 = bounds.interpolateY((double) (j + 1) / (double) this.columns);

				subpixel = new Box2(x0, y0, x1, y1);
				sample = this.pixelShader.shadePixel(subpixel, sample);
				pixel = MathUtil.add(pixel, sample);
			}
		}

		return MathUtil.scale(pixel, 1.0 / (double) (this.rows * this.columns));
	}

	/** The number of columns to divide each pixel into. */
	private final int columns;

	/** The number of rows to divide each pixel into. */
	private final int rows;

	/** The pixel shader to average the results from. */
	private final PixelShader pixelShader;

}
