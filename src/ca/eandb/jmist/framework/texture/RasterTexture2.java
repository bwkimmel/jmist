/**
 *
 */
package ca.eandb.jmist.framework.texture;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import ca.eandb.jmist.framework.PixelSpectrumFactory;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2</code> that is extrapolated from a <code>Raster</code>
 * image.
 * @author Brad Kimmel
 */
public final class RasterTexture2 implements Texture2 {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -2712131011948642676L;

	/**
	 * Creates a new <code>RasterTexture2</code>.
	 * @param raster The <code>BufferedImage</code> to use as the basis for the
	 * 		new <code>Texture2</code>.
	 * @param factory The <code>PixelSpectrumFactory</code> to use to create
	 * 		spectra from <code>Pixel</code>s.
	 */
	public RasterTexture2(BufferedImage image, PixelSpectrumFactory factory) {
		this.image = image;
		this.factory = factory;
	}

	public RasterTexture2(BufferedImage image) {
		this(image, null);
	}

	public RasterTexture2(File file) throws IOException {
		this(ImageIO.read(file));
	}

	public RasterTexture2(URL input) throws IOException {
		this(ImageIO.read(input));
	}

	public RasterTexture2(ImageInputStream stream) throws IOException {
		this(ImageIO.read(stream));
	}

	public RasterTexture2(InputStream input) throws IOException {
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color evaluate(Point2 p, WavelengthPacket lambda) {
		
		Raster		raster	= image.getRaster();
		double		u		= p.x() - Math.floor(p.x());
		double		v		= p.y() - Math.floor(p.y());
		int			w		= raster.getWidth();
		int			h		= raster.getHeight();
		int			x		= MathUtil.threshold((int) Math.floor(u * (double) w), 0, w - 1);
		int			y		= MathUtil.threshold((int) Math.floor(v * (double) h), 0, h - 1);
		double[]	pixel	= raster.getPixel(x, y, placeholder.get());
		ColorModel	cm		= lambda.getColorModel();

		placeholder.set(pixel);

		if (factory != null) {
			return cm.getContinuous(factory.createSpectrum(pixel)).sample(lambda);
		} else {
			switch (pixel.length) {
			case 1:
				return cm.getGray(pixel[0], lambda);
			case 3:
				return cm.fromRGB(pixel[0]/255.0, pixel[1]/255.0, pixel[2]/255.0).sample(lambda);
			default:
				throw new RuntimeException("Raster has unrecognized number of bands.");
			}
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

	/**
	 * The <code>PixelSpectrumFactory</code> to use to extrapolate
	 * <code>Pixel</code>s.
	 */
	private final PixelSpectrumFactory factory;

}
