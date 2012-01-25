/**
 * 
 */
package ca.eandb.jmist.framework.display;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import ca.eandb.jdcp.JdcpUtil;
import ca.eandb.jdcp.job.HostService;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.loader.radiance.RadiancePicture;
import ca.eandb.jmist.framework.loader.radiance.RadiancePicture.Format;
import ca.eandb.util.UnexpectedException;

/**
 * A <code>Display</code> that writes a radiance HDR image file using the RGBE
 * pixel format.
 * @author Brad Kimmel
 */
public final class RGBERadianceFileDisplay implements Display, Serializable {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -8735951495492670231L;

	/** The name of the file to write. */
	private final String fileName;
	
	/** The <code>RadiancePicture</code> image to write. */
	private transient RadiancePicture picture;
	
	/**
	 * Creates a new <code>RGBERadianceFileDisplay</code>.
	 * @param fileName The name of the file to write.
	 */
	public RGBERadianceFileDisplay(String fileName, RadiancePicture.Format format) {
		this.fileName = fileName;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
	 */
	@Override
	public void initialize(int w, int h, ColorModel colorModel) {
		picture = new RadiancePicture(w, h, Format.RGBE);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public void fill(int x, int y, int w, int h, Color color) {
		RGB rgb = color.toRGB();
		for (int j = y; j < y + h; j++) {
			for (int i = x; i < x + w; i++) {
				picture.setPixelRGB(i, j, rgb);
			}
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public void setPixel(int x, int y, Color pixel) {
		picture.setPixelRGB(x, y, pixel.toRGB());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
	 */
	@Override
	public void setPixels(int x, int y, Raster pixels) {
		for (int j = 0, h = pixels.getHeight(); j < h; j++) {
			for (int i = 0, w = pixels.getWidth(); i < w; i++) {
				picture.setPixelRGB(x + i, y + j, pixels.getPixel(i, j).toRGB());
			}
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#finish()
	 */
	@Override
	public void finish() {
		HostService service = JdcpUtil.getHostService();
		try {
			FileOutputStream os = (service != null) ? service
					.createFileOutputStream(fileName) : new FileOutputStream(
					fileName);
					
			picture.write(os);
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

}
