/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.color.luminance;

import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * A <code>WavelengthPacket</code> for a <code>Color</code> associated with the
 * <code>LuminanceColorModel</code>.
 * @author Brad Kimmel
 */
/* package */ final class LuminanceWavelengthPacket implements WavelengthPacket {

	/** The wavelength for this packet (in meters). */
	private final double wavelength;

	/**
	 * Creates a new <code>LuminanceWavelengthPacket</code>.
	 * @param wavelength The wavelength for this packet (in meters).
	 */
	public LuminanceWavelengthPacket(double wavelength) {
		this.wavelength = wavelength;
	}

	/**
	 * Gets the wavelength for this packet (in meters)
	 * @return The wavelength for this packet (in m).
	 */
	public double getWavelength() {
		return wavelength;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.WavelengthPacket#getColorModel()
	 */
	public ColorModel getColorModel() {
		return LuminanceColorModel.getInstance();
	}

}
