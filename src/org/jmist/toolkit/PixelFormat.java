/**
 *
 */
package org.jmist.toolkit;

/**
 * Describes the channels of a pixel.
 * This class is immutable.
 * @author bkimmel
 */
public final class PixelFormat {

	private PixelFormat() {

	}

	public static final PixelFormat RGB = new PixelFormat();
	public static final PixelFormat RGBA = new PixelFormat();

}
