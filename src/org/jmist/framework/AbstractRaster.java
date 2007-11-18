/**
 *
 */
package org.jmist.framework;

import java.util.Arrays;
import java.util.Iterator;

/**
 * An abstract <code>Raster</code> that provides default implementations of the
 * <code>setPixels</code> methods.
 * @see {@link Raster#setPixels(int, int, int, int, Iterable)},
 * 		{@link Raster#setPixels(int, int, int, int, double[][])}.
 * @author bkimmel
 */
public abstract class AbstractRaster implements Raster {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Raster#setPixels(int, int, int, int, double[][])
	 */
	@Override
	public void setPixels(int x0, int y0, int x1, int y1, double[][] pixels) {
		this.setPixels(x0, y0, x1, y1, Arrays.asList(pixels));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Raster#setPixels(int, int, int, int, java.lang.Iterable)
	 */
	@Override
	public void setPixels(int x0, int y0, int x1, int y1, Iterable<double[]> pixels) {

		Iterator<double[]> i = pixels.iterator();

		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				assert(i.hasNext());
				this.setPixel(x, y, i.next());
			}
		}

	}

}
