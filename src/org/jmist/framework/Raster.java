/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Pixel;

/**
 * @author bkimmel
 *
 */
public interface Raster {

	Pixel getPixel(int x, int y);

	void setPixel(int x, int y, Pixel pixel);

	void setPixels(int x0, int y0, int x1, int y1, Pixel[] pixels);

	void setPixels(int x0, int y0, int x1, int y1, Iterable<Pixel> pixels);

	int width();
	int height();

}
