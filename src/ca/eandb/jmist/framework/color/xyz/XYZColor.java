/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

package ca.eandb.jmist.framework.color.xyz;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author brad
 *
 */
/* package */ final class XYZColor implements Color, Spectrum {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -5361524897380680027L;

	public static final XYZColor BLACK = new XYZColor(0, 0, 0, null);

	public static final XYZColor WHITE = new XYZColor(1, 1, 1, null);

	private final double x;

	private final double y;

	private final double z;

	private final XYZWavelengthPacket lambda;

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public XYZColor(double x, double y, double z, XYZWavelengthPacket lambda) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.lambda = lambda;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toXYZ()
	 */
	public CIEXYZ toXYZ() {
		return new CIEXYZ(x, y, z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toRGB()
	 */
	public RGB toRGB() {
		return ColorUtil.convertXYZ2RGB(x, y, z);
	}

	private XYZColor create(double x, double y, double z, Color compat) {
		return new XYZColor(x, y, z, (lambda == compat.getWavelengthPacket()) ? lambda : null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#clamp(double)
	 */
	public Color clamp(double max) {
		return new XYZColor(Math.min(x, max), Math.min(y, max), Math.min(z, max), lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#clamp(double, double)
	 */
	public Color clamp(double min, double max) {
		return new XYZColor(
				Math.min(Math.max(x, min), max),
				Math.min(Math.max(y, min), max),
				Math.min(Math.max(z, min), max),
				lambda
				);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#disperse(int)
	 */
	public Color disperse(int channel) {
		switch (channel) {
		case 0:
			return new XYZColor(x, 0, 0, lambda);
		case 1:
			return new XYZColor(0, y, 0, lambda);
		case 2:
			return new XYZColor(0, 0, z, lambda);
		}
		throw new IllegalArgumentException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#divide(ca.eandb.jmist.framework.color.Color)
	 */
	public Color divide(Color other) {
		return divide((XYZColor) other);
	}

	public XYZColor divide(XYZColor other) {
		return create(x / other.x, y / other.y, z / other.z, other);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#divide(double)
	 */
	public Color divide(double c) {
		return new XYZColor(x / c, y / c, z / c, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#exp()
	 */
	public Color exp() {
		return new XYZColor(Math.exp(x), Math.exp(y), Math.exp(z), lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getColorModel()
	 */
	public ColorModel getColorModel() {
		return XYZColorModel.getInstance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getValue(int)
	 */
	public double getValue(int channel) {
		switch (channel) {
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return z;
		}
		throw new IllegalArgumentException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getWavelengthPacket()
	 */
	public WavelengthPacket getWavelengthPacket() {
		return lambda;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#invert()
	 */
	public Color invert() {
		return new XYZColor(1.0 / x, 1.0 / y, 1.0 / z, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#luminance()
	 */
	public double luminance() {
		return ColorUtil.convertXYZ2Luminance(x, y, z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#minus(ca.eandb.jmist.framework.color.Color)
	 */
	public Color minus(Color other) {
		return minus((XYZColor) other);
	}

	public XYZColor minus(XYZColor other) {
		return create(x - other.x, y - other.y, z - other.z, other);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#negative()
	 */
	public Color negative() {
		return new XYZColor(-x, -y, -z, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#plus(ca.eandb.jmist.framework.color.Color)
	 */
	public Color plus(Color other) {
		return plus((XYZColor) other);
	}

	public XYZColor plus(XYZColor other) {
		return create(x + other.x, y + other.y, z + other.z, other);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#pow(ca.eandb.jmist.framework.color.Color)
	 */
	public Color pow(Color other) {
		return pow((XYZColor) other);
	}

	public XYZColor pow(XYZColor other) {
		return create(Math.pow(x, other.x), Math.pow(y, other.y), Math.pow(z, other.z), other);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#pow(double)
	 */
	public Color pow(double e) {
		return new XYZColor(Math.pow(x, e), Math.pow(y, e), Math.pow(z, e), lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#sqrt()
	 */
	public Color sqrt() {
		return new XYZColor(Math.sqrt(x), Math.sqrt(y), Math.sqrt(z), lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#times(ca.eandb.jmist.framework.color.Color)
	 */
	public Color times(Color other) {
		return times((XYZColor) other);
	}

	public XYZColor times(XYZColor other) {
		return create(x * other.x, y * other.y, z * other.z, other);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#times(double)
	 */
	public Color times(double c) {
		return new XYZColor(x * c, y * c, z * c, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toArray()
	 */
	public double[] toArray() {
		return new double[]{ x, y, z };
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Spectrum#sample(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color sample(WavelengthPacket lambda) {
		return sample((XYZWavelengthPacket) lambda);
	}

	public XYZColor sample(XYZWavelengthPacket lambda) {
		return new XYZColor(x, y, z, lambda);
	}

}
