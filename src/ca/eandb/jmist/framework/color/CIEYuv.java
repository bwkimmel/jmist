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
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.Tuple3;

/**
 * A color in CIE Yuv color space.
 * @see <a href="http://en.wikipedia.org/wiki/CIE_1960_color_space">CIE 1960 color space</a>
 * @author Brad Kimmel
 */
public final class CIEYuv extends Tuple3 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -6643876217801917072L;

	public static final CIEYuv ZERO = new CIEYuv(0.0, 0.0, 0.0);

	/**
	 * @param Y
	 * @param u
	 * @param v
	 */
	public CIEYuv(double Y, double u, double v) {
		super(Y, u, v);
	}

	public double Y() {
		return x;
	}

	public double u() {
		return y;
	}

	public double v() {
		return z;
	}

	public CIEYuv times(double c) {
		return new CIEYuv(x * c, y, z);
	}

	public CIEYuv divide(double c) {
		return new CIEYuv(x / c, y, z);
	}

	public CIEXYZ toXYZ() {
		return ColorUtil.convertYuv2XYZ(this);
	}

	public static CIEYuv fromXYZ(double X, double Y, double Z) {
		return ColorUtil.convertXYZ2Yuv(X, Y, Z);
	}

	public static CIEYuv fromXYZ(CIEXYZ xyz) {
		return ColorUtil.convertXYZ2Yuv(xyz);
	}

}
