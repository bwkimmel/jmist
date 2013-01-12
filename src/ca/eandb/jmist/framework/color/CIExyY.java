/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.Tuple3;

/**
 * A color represented in CIE xyY color space.
 * @see <a href="http://en.wikipedia.org/wiki/XyY#The_CIE_xy_chromaticity_diagram_and_the_CIE_xyY_color_space">CIE xyY color space</a>
 * @author Brad Kimmel
 */
public final class CIExyY extends Tuple3 {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6709679926070449354L;

	public static final CIExyY ZERO = new CIExyY(0.0, 0.0, 0.0);
	public static final CIExyY A = CIExyY.fromXYZ(CIEXYZ.A);
	public static final CIExyY B = CIExyY.fromXYZ(CIEXYZ.B);
	public static final CIExyY C = CIExyY.fromXYZ(CIEXYZ.C);
	public static final CIExyY D50 = CIExyY.fromXYZ(CIEXYZ.D50);
	public static final CIExyY D55 = CIExyY.fromXYZ(CIEXYZ.D55);
	public static final CIExyY D65 = CIExyY.fromXYZ(CIEXYZ.D65);
	public static final CIExyY D75 = CIExyY.fromXYZ(CIEXYZ.D75);
	public static final CIExyY E = CIExyY.fromXYZ(CIEXYZ.E);
	public static final CIExyY F2 = CIExyY.fromXYZ(CIEXYZ.F2);
	public static final CIExyY F7 = CIExyY.fromXYZ(CIEXYZ.F7);
	public static final CIExyY F11 = CIExyY.fromXYZ(CIEXYZ.F11);

	/**
	 * @param x
	 * @param y
	 * @param Y
	 */
	public CIExyY(double x, double y, double Y) {
		super(x, y, Y);
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double Y() {
		return z;
	}

	public CIExyY times(double c) {
		return new CIExyY(x, y, z * c);
	}

	public CIExyY divide(double c) {
		return new CIExyY(x, y, z / c);
	}

	public CIEXYZ toXYZ() {
		return ColorUtil.convertxyY2XYZ(this);
	}

	public static CIExyY fromXYZ(double X, double Y, double Z) {
		return ColorUtil.convertXYZ2xyY(X, Y, Z);
	}

	public static CIExyY fromXYZ(CIEXYZ xyz) {
		return ColorUtil.convertXYZ2xyY(xyz);
	}

}
