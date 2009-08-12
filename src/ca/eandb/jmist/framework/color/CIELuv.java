/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.Tuple3;

/**
 * @author Brad
 *
 */
public final class CIELuv extends Tuple3 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -6643876217801917072L;

	public static final CIELuv ZERO = new CIELuv(0.0, 0.0, 0.0);

	/**
	 * @param L
	 * @param u
	 * @param v
	 */
	public CIELuv(double L, double u, double v) {
		super(L, u, v);
	}

	public double L() {
		return x;
	}

	public double u() {
		return y;
	}

	public double v() {
		return z;
	}

	public CIELuv times(double c) {
		return new CIELuv(x * c, y, z);
	}

	public CIELuv divide(double c) {
		return new CIELuv(x / c, y, z);
	}

	public CIEXYZ toXYZ(CIEXYZ ref) {
		return ColorUtil.convertLuv2XYZ(this, ref);
	}

	public static CIELuv fromXYZ(double X, double Y, double Z, CIEXYZ ref) {
		return ColorUtil.convertXYZ2Luv(X, Y, Z, ref);
	}

	public static CIELuv fromXYZ(CIEXYZ xyz, CIEXYZ ref) {
		return ColorUtil.convertXYZ2Luv(xyz, ref);
	}

}
