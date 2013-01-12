/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
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
import ca.eandb.jmist.util.ArrayUtil;

/**
 * A <code>Raster</code> backed by an array of doubles.
 * @author Brad Kimmel
 */
public abstract class DoubleRaster implements Raster {

	/** Serialization version ID. */
	private static final long serialVersionUID = 3651188089171016945L;

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
	public final Color getPixel(int x, int y) {
		int index = (y * width + x) * channels;
		return getPixel(raster, index);
	}

	protected abstract Color getPixel(double[] raster, int index);

	protected void addPixel(double[] raster, int index, Color pixel) {
		for (int ch = 0; ch < channels; ch++) {
			raster[index++] += pixel.getValue(ch);
		}
	}

	protected void setPixel(double[] raster, int index, Color pixel) {
		for (int ch = 0; ch < channels; ch++) {
			raster[index++] = pixel.getValue(ch);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Raster#addPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public final void addPixel(int x, int y, Color pixel) {
		int index = (y * width + x) * channels;
		addPixel(raster, index, pixel);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RasterWriter#getHeight()
	 */
	public final int getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RasterWriter#getWidth()
	 */
	public final int getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RasterWriter#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public final void setPixel(int x, int y, Color color) {
		int index = (y * width + x) * channels;
		setPixel(raster, index, color);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RasterWriter#clear()
	 */
	public final void clear() {
		ArrayUtil.setAll(raster, 0.0);
	}

}
