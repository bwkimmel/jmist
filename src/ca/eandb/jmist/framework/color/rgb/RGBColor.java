/**
 *
 */
package ca.eandb.jmist.framework.color.rgb;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Tuple3;


/**
 * @author Brad
 *
 */
public final class RGBColor implements Color, Spectrum {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -1599907078097936372L;

	public static final RGBColor BLACK = new RGBColor(0, 0, 0);

	public static final RGBColor WHITE = new RGBColor(1, 1, 1);

	private static final WavelengthPacket WAVELENGTH_PACKET = new WavelengthPacket() {
		public ColorModel getColorModel() {
			return RGBColorModel.getInstance();
		}
	};

	private final double r;

	private final double g;

	private final double b;

	/**
	 * @param r
	 * @param g
	 * @param b
	 */
	public RGBColor(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toXYZ()
	 */
	public Tuple3 toXYZ() {
		return ColorUtil.convertRGB2XYZ(r, g, b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toRGB()
	 */
	public Tuple3 toRGB() {
		return new Tuple3(r, g, b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#divide(ca.eandb.jmist.framework.color.Color)
	 */
	public Color divide(Color other) {
		return divide((RGBColor) other);
	}

	public RGBColor divide(RGBColor other) {
		return new RGBColor(r / other.r, g / other.g, b / other.b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#divide(double)
	 */
	public Color divide(double c) {
		return new RGBColor(r / c, g / c, b / c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#exp()
	 */
	public Color exp() {
		return new RGBColor(Math.exp(r), Math.exp(g), Math.exp(b));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#invert()
	 */
	public Color invert() {
		return new RGBColor(1.0 / r, 1.0 / g, 1.0 / b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#minus(ca.eandb.jmist.framework.color.Color)
	 */
	public Color minus(Color other) {
		return minus((RGBColor) other);
	}

	public RGBColor minus(RGBColor other) {
		return new RGBColor(r - other.r, g - other.g, b - other.b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#negative()
	 */
	public Color negative() {
		return new RGBColor(-r, -g, -b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#plus(ca.eandb.jmist.framework.color.Color)
	 */
	public Color plus(Color other) {
		return plus((RGBColor) other);
	}

	public RGBColor plus(RGBColor other) {
		return new RGBColor(r + other.r, g + other.g, b + other.b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#pow(ca.eandb.jmist.framework.color.Color)
	 */
	public Color pow(Color other) {
		return pow((RGBColor) other);
	}

	public RGBColor pow(RGBColor other) {
		return new RGBColor(Math.pow(r, other.r), Math.pow(g, other.g), Math.pow(b, other.b));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#pow(double)
	 */
	public Color pow(double e) {
		return new RGBColor(Math.pow(r, e), Math.pow(g, e), Math.pow(b, e));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#sqrt()
	 */
	public Color sqrt() {
		return new RGBColor(Math.sqrt(r), Math.sqrt(g), Math.sqrt(b));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#times(ca.eandb.jmist.framework.color.Color)
	 */
	public Color times(Color other) {
		return times((RGBColor) other);
	}

	public RGBColor times(RGBColor other) {
		return new RGBColor(r * other.r, g * other.g, b * other.b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#times(double)
	 */
	public Color times(double c) {
		return new RGBColor(r * c, g * c, b * c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#clamp(double, double)
	 */
	public Color clamp(double min, double max) {
		return new RGBColor(
				MathUtil.threshold(r, min, max),
				MathUtil.threshold(g, min, max),
				MathUtil.threshold(b, min, max));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#clamp(double)
	 */
	public Color clamp(double max) {
		return new RGBColor(Math.min(r, max), Math.min(g, max), Math.min(b, max));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#disperse(int)
	 */
	public Color disperse(int channel) {
		switch (channel) {
		case 0:
			return new RGBColor(r, 0, 0);
		case 1:
			return new RGBColor(0, g, 0);
		case 2:
			return new RGBColor(0, 0, b);
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getColorModel()
	 */
	public ColorModel getColorModel() {
		return RGBColorModel.getInstance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getValue(int)
	 */
	public double getValue(int channel) {
		switch (channel) {
		case 0:
			return r;
		case 1:
			return g;
		case 2:
			return b;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#toArray()
	 */
	public double[] toArray() {
		return new double[]{ r, g, b };
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getWavelengthPacket()
	 */
	public WavelengthPacket getWavelengthPacket() {
		return WAVELENGTH_PACKET;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Spectrum#sample(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color sample(WavelengthPacket lambda) {
		return this;
	}

}
