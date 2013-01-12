/**
 *
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
