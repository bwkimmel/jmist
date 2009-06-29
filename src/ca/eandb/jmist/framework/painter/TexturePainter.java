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

package ca.eandb.jmist.framework.painter;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.math.Point2;

/**
 * @author brad
 *
 */
public final class TexturePainter implements Painter {

	private final Raster texture;

	private transient double[] pixel = null;

	/**
	 * @param texture
	 */
	public TexturePainter(Raster texture) {
		this.texture = texture;
	}

	public TexturePainter(BufferedImage image) {
		this(image.getRaster());
	}

	public TexturePainter(File file) throws IOException {
		this(ImageIO.read(file));
	}

	public TexturePainter(URL input) throws IOException {
		this(ImageIO.read(input));
	}

	public TexturePainter(ImageInputStream stream) throws IOException {
		this(ImageIO.read(stream));
	}

	public TexturePainter(InputStream input) throws IOException {
		this(ImageIO.read(input));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Painter#getColor(ca.eandb.jmist.framework.SurfacePoint)
	 */
	@Override
	public Spectrum getColor(SurfacePoint p) {
		Point2 uv = p.getUV();
		int x = (int) Math.floor(uv.x() * texture.getWidth());
		int y = (int) Math.floor(uv.y() * texture.getHeight());
		return getPixel(x, y);
	}

	private synchronized Spectrum getPixel(int x, int y) {
		pixel = texture.getPixel(x, y, pixel);

		// FIXME: Maximum channel value should not be hard coded.
		switch (pixel.length) {
		case 1:
			return ColorModel.getInstance().getGray(pixel[0]/255.0);
		case 3:
			return ColorModel.getInstance().fromRGB(pixel[0]/255.0, pixel[1]/255.0, pixel[2]/255.0);
		default:
			throw new UnsupportedOperationException();
		}
	}



}
