/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.MathUtil;


/**
 * @author Brad
 *
 */
public final class RGBColor implements Color {

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
	 * @see ca.eandb.jmist.framework.color.Color#divide(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color divide(Color other) {
		return divide((RGBColor) other);
	}

	public RGBColor divide(RGBColor other) {
		return new RGBColor(r / other.r, g / other.g, b / other.b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#divide(double)
	 */
	@Override
	public Color divide(double c) {
		return new RGBColor(r / c, g / c, b / c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#exp()
	 */
	@Override
	public Color exp() {
		return new RGBColor(Math.exp(r), Math.exp(g), Math.exp(b));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#invert()
	 */
	@Override
	public Color invert() {
		return new RGBColor(1.0 / r, 1.0 / g, 1.0 / b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#minus(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color minus(Color other) {
		return minus((RGBColor) other);
	}

	public RGBColor minus(RGBColor other) {
		return new RGBColor(r - other.r, g - other.g, b - other.b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#negative()
	 */
	@Override
	public Color negative() {
		return new RGBColor(-r, -g, -b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#plus(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color plus(Color other) {
		return plus((RGBColor) other);
	}

	public RGBColor plus(RGBColor other) {
		return new RGBColor(r + other.r, g + other.g, b + other.b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#pow(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color pow(Color other) {
		return pow((RGBColor) other);
	}

	public RGBColor pow(RGBColor other) {
		return new RGBColor(Math.pow(r, other.r), Math.pow(g, other.g), Math.pow(b, other.b));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#pow(double)
	 */
	@Override
	public Color pow(double e) {
		return new RGBColor(Math.pow(r, e), Math.pow(g, e), Math.pow(b, e));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#sqrt()
	 */
	@Override
	public Color sqrt() {
		return new RGBColor(Math.sqrt(r), Math.sqrt(g), Math.sqrt(b));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#times(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public Color times(Color other) {
		return times((RGBColor) other);
	}

	public RGBColor times(RGBColor other) {
		return new RGBColor(r * other.r, g * other.g, b * other.b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#times(double)
	 */
	@Override
	public Color times(double c) {
		return new RGBColor(r * c, g * c, b * c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#clamp(double, double)
	 */
	@Override
	public Color clamp(double min, double max) {
		return new RGBColor(
				MathUtil.threshold(r, min, max),
				MathUtil.threshold(g, min, max),
				MathUtil.threshold(b, min, max));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#clamp(double)
	 */
	@Override
	public Color clamp(double max) {
		return new RGBColor(Math.min(r, max), Math.min(g, max), Math.min(b, max));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#disperse(int)
	 */
	@Override
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
	@Override
	public ColorModel getColorModel() {
		return RGBColorModel.getInstance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Color#getValue(int)
	 */
	@Override
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
	@Override
	public double[] toArray() {
		return new double[]{ r, g, b };
	}

	public static final RGBColor BLACK = new RGBColor(0, 0, 0);

	public static final RGBColor WHITE = new RGBColor(1, 1, 1);

}
