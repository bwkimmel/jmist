/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.color.xyz.single;

import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * A <code>WavelengthPacket</code> representing a single wavelength sampled
 * by the <code>SingleXYZColorModel</code> and distributed according to the
 * luminous efficacy function.
 * 
 * @see http://en.wikipedia.org/wiki/Luminous_efficacy
 */
public final class SingleXYZWavelengthPacket implements WavelengthPacket {
	
	/** Wavelength sampled in meters. */
	private final double lambda;
	
	/**
	 * Creates a new <code>XYZWavelength</code>.
	 * @param lambda The wavelength, in meters.
	 */
	public SingleXYZWavelengthPacket(double lambda) {
		this.lambda = lambda;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.WavelengthPacket#getColorModel()
	 */
	@Override
	public ColorModel getColorModel() {
		return SingleXYZColorModel.getInstance();
	}
	
	/** Gets the wavelength for this sample, in meters. */
	public double getWavelength() {
		return lambda;
	}

}
