/**
 *
 */
package ca.eandb.jmist.framework.display;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import ca.eandb.jdcp.JdcpUtil;
import ca.eandb.jdcp.job.HostService;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Tuple3;
import ca.eandb.util.UnexpectedException;
import ca.eandb.util.io.FileUtil;

/**
 * @author Brad Kimmel
 *
 */
public final class ImageFileDisplay implements Display, Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = -5965755570476575580L;

	private static final String DEFAULT_FILENAME = "output.png";

	private static final String DEFAULT_FORMAT = FileUtil.getExtension(DEFAULT_FILENAME);

	private final String fileName;

	private transient BufferedImage image;

	public ImageFileDisplay() {
		this(DEFAULT_FILENAME);
	}

	public ImageFileDisplay(String fileName) {
		this.fileName = fileName;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public void fill(int x, int y, int w, int h, Color color) {
		int rgb = toRGB8(color.toRGB());
		for (int y1 = y + h; y < y1; y++) {
			for (int x1 = x + w; x < x1; x++) {
				image.setRGB(x, y, rgb);
			}
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#finish()
	 */
	public void finish() {
		HostService service = JdcpUtil.getHostService();
		try {
			FileOutputStream os = (service != null) ? service
					.createFileOutputStream(fileName) : new FileOutputStream(
					fileName);

			String formatName = FileUtil.getExtension(fileName);
			if (formatName.equals("")) {
				formatName = DEFAULT_FORMAT;
			}

			ImageIO.write(image, formatName, os);
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
	 */
	public void initialize(int w, int h, ColorModel colorModel) {
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public void setPixel(int x, int y, Color pixel) {
		int rgb = toRGB8(pixel.toRGB());
		image.setRGB(x, y, rgb);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
	 */
	public void setPixels(int x, int y, Raster pixels) {
		int w = pixels.getWidth();
		int h = pixels.getHeight();
		for (int ry = 0; ry < h; ry++) {
			for (int rx = 0; rx < w; rx++) {
				int rgb = toRGB8(pixels.getPixel(rx, ry).toRGB());
				image.setRGB(x + rx, y + ry, rgb);
			}
		}
	}

	private int toRGB8(Tuple3 rgb) {
		return
			(MathUtil.threshold((int) Math.floor(256.0 * rgb.x()), 0, 255) << 16) |
			(MathUtil.threshold((int) Math.floor(256.0 * rgb.y()), 0, 255) << 8) |
			MathUtil.threshold((int) Math.floor(256.0 * rgb.z()), 0, 255);
	}

}
