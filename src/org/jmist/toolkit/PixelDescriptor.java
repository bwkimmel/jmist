/**
 *
 */
package org.jmist.toolkit;

/**
 * Describes the channels of a pixel.
 * This class is immutable.
 * @author bkimmel
 */
public final class PixelDescriptor {

	private PixelDescriptor() {

	}

	public static final PixelDescriptor RGB = new PixelDescriptor();
	public static final PixelDescriptor RGBA = new PixelDescriptor();

}
