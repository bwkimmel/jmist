/**
 *
 */
package org.jmist.toolkit;

import java.io.Serializable;

/**
 * Describes the channels of a pixel.
 * This class is immutable.
 * @author bkimmel
 */
public final class PixelFormat implements Serializable {

	private PixelFormat() {

	}

	public static final PixelFormat RGB = new PixelFormat();
	public static final PixelFormat RGBA = new PixelFormat();

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -2651551384031016203L;

}
