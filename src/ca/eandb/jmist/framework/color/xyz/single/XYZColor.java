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

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
public final class XYZColor implements SingleXYZColor, Spectrum {
	
	/** Serialization version ID */
	private static final long serialVersionUID = 4075320463440427747L;

	private final double x;
	
	private final double y;
	
	private final double z;
	
	public XYZColor(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Spectrum#sample(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color sample(WavelengthPacket lambda) {
		throw new UnimplementedException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getWavelengthPacket()
	 */
	@Override
	public WavelengthPacket getWavelengthPacket() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getColorModel()
	 */
	@Override
	public ColorModel getColorModel() {
		return SingleXYZColorModel.getInstance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#times(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public XYZColor times(Color other) {
		return times(((SingleXYZColor) other).toXYZColor());
	}
	
	public XYZColor times(XYZColor other) {
		return new XYZColor(x * other.x, y * other.y, z * other.z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#times(double)
	 */
	@Override
	public XYZColor times(double c) {
		return new XYZColor(x * c, y * c, z * c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#divide(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color divide(Color other) {
		return divide(((SingleXYZColor) other).toXYZColor());
	}
	
	public XYZColor divide(XYZColor other) {
		return new XYZColor(x / other.x, y / other.y, z / other.z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#divide(double)
	 */
	@Override
	public Color divide(double c) {
		return new XYZColor(x / c, y / c, z / c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#plus(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color plus(Color other) {
		return plus(((SingleXYZColor) other).toXYZColor());
	}
	
	public XYZColor plus(XYZColor other) {
		return new XYZColor(x + other.x, y + other.y, z + other.z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#minus(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color minus(Color other) {
		return minus(((SingleXYZColor) other).toXYZColor());
	}
	
	public XYZColor minus(XYZColor other) {
		return new XYZColor(x - other.x, y - other.y, z - other.z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#sqrt()
	 */
	@Override
	public Color sqrt() {
		return new XYZColor(Math.sqrt(x), Math.sqrt(y), Math.sqrt(z));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#exp()
	 */
	@Override
	public Color exp() {
		return new XYZColor(Math.exp(x), Math.exp(y), Math.exp(z));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#invert()
	 */
	@Override
	public Color invert() {
		return new XYZColor(1.0 / x, 1.0 / y, 1.0 / z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#negative()
	 */
	@Override
	public Color negative() {
		return new XYZColor(-x, -y, -z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#abs()
	 */
	@Override
	public Color abs() {
		if (x >= 0 && y >= 0 && z >= 0) {
			return this;
		} else {
			return new XYZColor(Math.abs(x), Math.abs(y), Math.abs(z));
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#pow(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color pow(Color other) {
		return pow(((SingleXYZColor) other).toXYZColor());
	}
	
	public XYZColor pow(XYZColor other) {
		return new XYZColor(
				Math.pow(x, other.x), 
				Math.pow(y, other.y),
				Math.pow(z, other.z));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#pow(double)
	 */
	@Override
	public Color pow(double e) {
		return new XYZColor(Math.pow(x, e), Math.pow(y, e), Math.pow(z, e));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#clamp(double)
	 */
	@Override
	public Color clamp(double max) {
		return new XYZColor(
				x < max ? x : max,
				y < max ? y : max,
				z < max ? z : max);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#clamp(double, double)
	 */
	@Override
	public Color clamp(double min, double max) {
		return new XYZColor(
				MathUtil.clamp(x, min, max),
				MathUtil.clamp(y, min, max),
				MathUtil.clamp(z, min, max));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getValue(int)
	 */
	@Override
	public double getValue(int channel) {
		switch (channel) {
		case 0: return x;
		case 1: return y;
		case 2: return z;
		default: throw new IndexOutOfBoundsException("channel must be between 0 and 2 inclusive");
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#disperse(int)
	 */
	@Override
	public Color disperse(int channel) {
		switch (channel) {
		case 0: return new XYZColor(x, 0, 0);
		case 1: return new XYZColor(0, y, 0);
		case 2: return new XYZColor(0, 0, z);
		default: throw new IndexOutOfBoundsException("channel must be between 0 and 2 inclusive");
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#luminance()
	 */
	@Override
	public double luminance() {
		return y;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toArray()
	 */
	@Override
	public double[] toArray() {
		return new double[]{ x, y, z };
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toXYZ()
	 */
	@Override
	public CIEXYZ toXYZ() {
		return new CIEXYZ(x, y, z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toRGB()
	 */
	@Override
	public RGB toRGB() {
		return ColorUtil.convertXYZ2RGB(x, y, z);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.xyz.single.SingleXYZColor#toXYZColor()
	 */
	@Override
	public XYZColor toXYZColor() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.xyz.single.SingleXYZColor#asSample()
	 */
	@Override
	public XYZSample asSample() {
		throw new UnsupportedOperationException();
	}

}
