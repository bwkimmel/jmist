/**
 *
 */
package org.jmist.framework;

import java.io.OutputStream;

/**
 * @author bkimmel
 *
 */
public interface Raster {

	double[] getPixel(int x, int y);

	void setPixel(int x, int y, double[] pixel);

	void setPixels(int x0, int y0, int x1, int y1, double[][] pixels);

	void setPixels(int x0, int y0, int x1, int y1, Iterable<double[]> pixels);

	int width();
	int height();

	void write(OutputStream stream);

}
