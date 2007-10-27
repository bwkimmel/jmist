/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.RasterWriter;
import org.jmist.toolkit.Pixel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @author bkimmel
 *
 */
public final class ImageRasterWriter implements RasterWriter {

	/**
	 * Initializes the dimensions of the raster image.
	 * @param width The width of the image.
	 * @param height The height of the image.
	 */
	public ImageRasterWriter(int width, int height) {
		this.width		= width;
		this.height		= height;
		this.image		= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RasterWriter#done()
	 */
	public boolean done() {
		return this.y >= this.height;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RasterWriter#height()
	 */
	public int height() {
		return this.height;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RasterWriter#width()
	 */
	public int width() {
		return this.width;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RasterWriter#writePixel(org.jmist.toolkit.Pixel)
	 */
	public void writePixel(Pixel pixel) {

		assert(!this.done());

		int rgb = 0;
		this.image.setRGB(this.x++, this.y, rgb);

		if (this.x >= this.width) {
			this.x = 0;
			this.y++;
		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RasterWriter#x()
	 */
	public int x() {
		return this.x;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RasterWriter#y()
	 */
	public int y() {
		return this.y;
	}

	/**
	 * Saves the rendered image to a file.
	 * @param filename The name of the file to save the image to.
	 * @param formatName The type of file to save.
	 * @throws IOException
	 */
	public void save(String filename, String formatName) throws IOException {
		ImageIO.write(this.image, formatName, new File(filename));
	}

	/** The x-coordinate of the next pixel to be written. */
	private int x = 0;

	/** The y-coordinate of the next pixel to be written. */
	private int y = 0;

	/** The width of the image. */
	private final int width;

	/** The height of the image. */
	private final int height;

	/** The image to be written. */
	private final BufferedImage image;

}
