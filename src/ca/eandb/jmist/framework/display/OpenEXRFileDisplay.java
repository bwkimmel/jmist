/**
 * 
 */
package ca.eandb.jmist.framework.display;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import ca.eandb.jdcp.JdcpUtil;
import ca.eandb.jdcp.job.HostService;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.loader.openexr.OpenEXRImage;
import ca.eandb.jmist.framework.loader.openexr.attribute.Channel;
import ca.eandb.jmist.framework.loader.openexr.attribute.ChannelList;
import ca.eandb.jmist.framework.loader.openexr.attribute.PixelType;
import ca.eandb.util.UnexpectedException;

/**
 * A <code>Display</code> that writes the results to an OpenEXR (.exr) image file.
 * @author Brad Kimmel
 */
public final class OpenEXRFileDisplay implements Display {
		
	/** The name of the file to write. */
	private final String fileName;
	
	/** The <code>PixelType</code> to use for the raw color channel data. */
	private final PixelType rawPixelType;
	
	/** The <code>PixelType</code> to use for the RGB color data. */
	private final PixelType rgbPixelType;
	
	/** The <code>OpenEXRImage</code> being created. */
	private transient OpenEXRImage image;
	
	/** The current <code>ColorModel</code>. */
	private transient ColorModel colorModel;
	
	/**
	 * Creates a new <code>OpenEXRDisplay</code>.
	 * @param fileName The name of the file to write.
	 * @param rawPixelType The <code>PixelType</code> to use for the raw color
	 * 		channel data.  If <code>null</code> then the raw color channel data
	 * 		is not written to the file.  This pixel type must be a floating
	 * 		point type.
	 * @param rgbPixelType The <code>PixelType</code> to use for the RGB color
	 * 		data.  If <code>null</code> then the RGB color data is not written
	 * 		to the file.  This pixel type must be a floating point type.
	 * @throws IllegalArgumentException if <code>rawPixelType</code> or
	 * 		<code>rgbColorType</code> are non-floating-point.
	 * @throws IllegalArgumentException if <code>rawPixelType</code> and
	 * 		<code>rgbColorType</code> are both <code>null</code>.
	 */
	public OpenEXRFileDisplay(String fileName, PixelType rawPixelType, PixelType rgbPixelType) {
		if (rawPixelType == PixelType.UINT) {
			throw new IllegalArgumentException("rawPixelType == UINT");
		}
		if (rgbPixelType == PixelType.UINT) {
			throw new IllegalArgumentException("rgbPixelType == UINT");
		}
		if (rawPixelType == null && rgbPixelType == null) {
			throw new IllegalArgumentException("At least one pixel type must be specified");			
		}
		this.fileName = fileName;
		this.rawPixelType = rawPixelType;
		this.rgbPixelType = rgbPixelType;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
	 */
	@Override
	public void initialize(int w, int h, ColorModel colorModel) {
		this.colorModel = colorModel;
		image = new OpenEXRImage(w, h);
		
		ChannelList chlist = image.getChannelList();
		
		if (rawPixelType != null) {
			for (int i = 0, n = colorModel.getNumChannels(); i < n; i++) {
				String name = colorModel.getChannelName(i);
				chlist.addChannel(new Channel(name, rawPixelType));
			}
		}
		
		if (rgbPixelType != null) {
			chlist.addChannel(new Channel("R", rgbPixelType));
			chlist.addChannel(new Channel("G", rgbPixelType));
			chlist.addChannel(new Channel("B", rgbPixelType));
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public void fill(int x0, int y0, int w, int h, Color color) {
		if (rawPixelType != null) {
			for (int i = 0, n = colorModel.getNumChannels(); i < n; i++) {
				String name = colorModel.getChannelName(i);
				float value = (float) color.getValue(i);
				
				for (int y = y0; y < y0 + h; y++) {
					for (int x = x0; x < x0 + w; x++) {
						image.setFloat(x, y, name, value);
					}
				}
			}
		}
		if (rgbPixelType != null) {
			RGB rgb = color.toRGB();
			for (int y = y0; y < y0 + h; y++) {
				for (int x = x0; x < x0 + w; x++) {
					image.setRGB(x, y, rgb);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public void setPixel(int x, int y, Color pixel) {
		if (rawPixelType != null) {
			for (int i = 0, n = colorModel.getNumChannels(); i < n; i++) {
				String name = colorModel.getChannelName(i);
				float value = (float) pixel.getValue(i);
				image.setFloat(x, y, name, value);
			}
		}
		if (rgbPixelType != null) {
			RGB rgb = pixel.toRGB();
			image.setRGB(x, y, rgb);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
	 */
	@Override
	public void setPixels(int x0, int y0, Raster pixels) {
		int w = pixels.getWidth();
		int h = pixels.getHeight();
		if (rawPixelType != null) {
			int n = colorModel.getNumChannels();	
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					Color color = pixels.getPixel(x, y);
					for (int i = 0; i < n; i++) {
						String name = colorModel.getChannelName(i);
						float value = (float) color.getValue(i);
						image.setFloat(x0 + x, y0 + y, name, value);
					}
				}
			}
		}
		if (rgbPixelType != null) {
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					RGB rgb = pixels.getPixel(x, y).toRGB();
					image.setRGB(x0 + x, y0 + y, rgb);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#finish()
	 */
	@Override
	public void finish() {
		try {
			ImageOutputStream out;
			HostService service = JdcpUtil.getHostService();
			if (service != null) {
				RandomAccessFile file = service.createRandomAccessFile(fileName);
				out = new FileImageOutputStream(file);
			} else {
				out = new FileImageOutputStream(new File(fileName));
			}
			image.write(out);
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

}
