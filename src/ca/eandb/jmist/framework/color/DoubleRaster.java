/*
 * Copyright (c) 2008 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.framework.Raster;

/**
 * @author brad
 *
 */
public abstract class DoubleRaster implements Raster {

	private final double[] raster;

	private final int width;

	private final int height;

	private final int channels;

	protected DoubleRaster(int width, int height, int channels) {
		this.width = width;
		this.height = height;
		this.channels = channels;
		this.raster = new double[width * height * channels];
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Raster#getPixel(int, int)
	 */
	@Override
	public Color getPixel(int x, int y) {
		int index = (y * width + x) * channels;
		return getPixel(raster, index);
	}

	protected abstract Color getPixel(double[] raster, int index);

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Raster#addPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public void addPixel(int x, int y, Color pixel) {
		int index = (y * width + x) * channels;
		for (int ch = 0; ch < channels; ch++) {
			raster[index++] += pixel.getValue(ch);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RasterWriter#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RasterWriter#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RasterWriter#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public void setPixel(int x, int y, Color color) {
		int index = (y * width + x) * channels;
		for (int ch = 0; ch < channels; ch++) {
			raster[index++] = color.getValue(ch);
		}
	}

}
