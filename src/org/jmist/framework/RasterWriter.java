/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Pixel;

/**
 * @author bkimmel
 *
 */
public interface RasterWriter {

	void writePixel(Pixel pixel);

	int x();
	int y();

	int width();
	int height();

	boolean done();

}
