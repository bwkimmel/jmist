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
import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.jmist.framework.tone.ToneMapperFactory;
import ca.eandb.jmist.math.Array2;
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

	private final ToneMapperFactory toneMapperFactory;

	private transient Array2<CIEXYZ> image;

	public ImageFileDisplay() {
		this(DEFAULT_FILENAME, ToneMapperFactory.IDENTITY_FACTORY);
	}

	public ImageFileDisplay(String fileName) {
		this(fileName, ToneMapperFactory.IDENTITY_FACTORY);
	}

	public ImageFileDisplay(ToneMapperFactory toneMapperFactory) {
		this(DEFAULT_FILENAME, toneMapperFactory);
	}

	public ImageFileDisplay(String fileName, ToneMapperFactory toneMapperFactory) {
		this.fileName = fileName;
		this.toneMapperFactory = toneMapperFactory;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public void fill(int x, int y, int w, int h, Color color) {
		CIEXYZ xyz = color.toXYZ();
		image.slice(x, y, w, h).setAll(xyz);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#finish()
	 */
	public void finish() {
		HostService service = JdcpUtil.getHostService();
		try {
			int width = image.rows();
			int height = image.columns();
			BufferedImage bi = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			FileOutputStream os = (service != null) ? service
					.createFileOutputStream(fileName) : new FileOutputStream(
					fileName);

			String formatName = FileUtil.getExtension(fileName);
			if (formatName.equals("")) {
				formatName = DEFAULT_FORMAT;
			}

			ToneMapper toneMapper = toneMapperFactory.createToneMapper(image);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					CIEXYZ xyz = toneMapper.apply(image.get(x, y));
					int rgb = xyz.toRGB().toR8G8B8();
					bi.setRGB(x, y, rgb);
				}
			}

			ImageIO.write(bi, formatName, os);
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
	 */
	public void initialize(int w, int h, ColorModel colorModel) {
		image = new Array2<CIEXYZ>(w, h);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	public void setPixel(int x, int y, Color pixel) {
		image.set(x, y, pixel.toXYZ());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
	 */
	public void setPixels(int x, int y, Raster pixels) {
		int w = pixels.getWidth();
		int h = pixels.getHeight();
		Array2<CIEXYZ> tile = image.slice(x, y, w, h);
		for (int ry = 0; ry < h; ry++) {
			for (int rx = 0; rx < w; rx++) {
				CIEXYZ xyz = pixels.getPixel(rx, ry).toXYZ();
				tile.set(rx, ry, xyz);
			}
		}
	}

}
