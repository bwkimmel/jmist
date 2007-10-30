/**
 *
 */
package org.jmist.framework;

import java.util.Arrays;
import java.util.Iterator;

import org.jmist.toolkit.Pixel;

/**
 * An abstract <code>Raster</code> that provides default implementations of the
 * <code>setPixels</code> methods.
 * @see {@link Raster#setPixels(int, int, int, int, Iterable)},
 * 		{@link Raster#setPixels(int, int, int, int, Pixel[])}.
 * @author bkimmel
 */
public abstract class AbstractRaster implements Raster {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Raster#setPixels(int, int, int, int, org.jmist.toolkit.Pixel[])
	 */
	@Override
	public void setPixels(int x0, int y0, int x1, int y1, Pixel[] pixels) {
		this.setPixels(x0, y0, x1, y1, Arrays.asList(pixels));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Raster#setPixels(int, int, int, int, java.lang.Iterable)
	 */
	@Override
	public void setPixels(int x0, int y0, int x1, int y1, Iterable<Pixel> pixels) {

		Iterator<Pixel> i = pixels.iterator();

		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				assert(i.hasNext());
				this.setPixel(x, y, i.next());
			}
		}

	}

}
