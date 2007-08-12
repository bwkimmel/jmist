/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Pixel;

/**
 * Something that can generate empty pixels.
 * @author bkimmel
 */
public interface IPixelFactory {

	/**
	 * Creates a new pixel.
	 * @return The new pixel.
	 */
	Pixel createPixel();

}
