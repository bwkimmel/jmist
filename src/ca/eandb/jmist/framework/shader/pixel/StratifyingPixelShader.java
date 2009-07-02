/**
 *
 */
package ca.eandb.jmist.framework.shader.pixel;

import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Box2;

/**
 * A pixel shader decorator that averages stratified samples from the
 * decorated pixel shader.
 * @author Brad Kimmel
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
	 * @see ca.eandb.jmist.framework.PixelShader#shadePixel(ca.eandb.jmist.math.Box2)
	 */
	public Color shadePixel(Box2 bounds) {
		double x0, x1, y0, y1;
		Color pixel = null;
		Color sample;
		Box2 subpixel;

		for (int i = 0; i < this.rows; i++) {
			x0 = bounds.interpolateX((double) i / (double) this.rows);
			x1 = bounds.interpolateX((double) (i + 1) / (double) this.rows);

			for (int j = 0; j < this.columns; j++) {
				y0 = bounds.interpolateY((double) j / (double) this.columns);
				y1 = bounds.interpolateY((double) (j + 1) / (double) this.columns);

				subpixel = new Box2(x0, y0, x1, y1);
				sample = pixelShader.shadePixel(subpixel);
				pixel = add(pixel, sample);
			}
		}

		return pixel.divide(this.rows * this.columns);
	}

	/**
	 * Adds two <code>Color</code>s.
	 * @param a The first <code>Color</code> (may be null).
	 * @param b The second <code>Color</code>.
	 * @return The sum of the two <code>Color</code>s.
	 */
	public Color add(Color a, Color b) {
		return (a != null) ? a.plus(b) : b;
	}

	/** The number of columns to divide each pixel into. */
	private final int columns;

	/** The number of rows to divide each pixel into. */
	private final int rows;

	/** The pixel shader to average the results from. */
	private final PixelShader pixelShader;

}
