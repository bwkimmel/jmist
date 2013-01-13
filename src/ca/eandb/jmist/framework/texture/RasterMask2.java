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
package ca.eandb.jmist.framework.texture;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2</code> that is extrapolated from a <code>Raster</code>
 * image.
 * @author Brad Kimmel
 */
public final class RasterMask2 implements Mask2 {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -2712131011948642676L;

	/**
	 * Creates a new <code>RasterMask2</code>.
	 * @param image The <code>BufferedImage</code> to use as the basis for the
	 * 		new <code>Mask2</code>.
	 */
	public RasterMask2(BufferedImage image) {
		this.image = image;
	}

	public RasterMask2(File file) throws IOException {
		this(ImageIO.read(file));
	}

	public RasterMask2(URL input) throws IOException {
		this(ImageIO.read(input));
	}

	public RasterMask2(ImageInputStream stream) throws IOException {
		this(ImageIO.read(stream));
	}

	public RasterMask2(InputStream input) throws IOException {
		this(ImageIO.read(input));
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		ImageIO.write(image, "png", oos);
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		image = ImageIO.read(ois);
		placeholder = new ThreadLocal<double[]>();
	}

	/*
	 * 
	 */
	public double opacity(Point2 p) {
		
		Raster		raster	= image.getRaster();
		double		u		= p.x() - Math.floor(p.x());
		double		v		= p.y() - Math.floor(p.y());
		int			w		= raster.getWidth();
		int			h		= raster.getHeight();
		int			x		= MathUtil.clamp((int) Math.floor(u * (double) w), 0, w - 1);
		int			y		= MathUtil.clamp((int) Math.floor(v * (double) h), 0, h - 1);
		double[]	pixel	= raster.getPixel(x, y, placeholder.get());

		placeholder.set(pixel);
		
//		if (pixel[0] < 0.5) {System.out.println("YES");}
//		System.out.printf("%f, %f", p.x(), p.y());
//		System.out.println();

		switch (pixel.length) {
		case 1:
			return pixel[0];
		case 3:
		case 4:
			return pixel[1]/255.0;
		default:
			throw new RuntimeException("Raster has unrecognized number of bands.");
		}

	}

	/**
	 * A place holder to hold a double array for the pixel, so that one is not
	 * allocated on every call to {@link #evaluate(Point2, WavelengthPacket)}.
	 */
	private transient ThreadLocal<double[]> placeholder = new ThreadLocal<double[]>();

	/**
	 * The <code>BufferedImage</code> that serves as the basis for this
	 * <code>Texture2</code>.
	 */
	private transient BufferedImage image;

}
