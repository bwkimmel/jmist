/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.Tuple3;

/**
 * @author Brad
 *
 */
public final class CIExyY extends Tuple3 {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6709679926070449354L;

	public static final CIExyY ZERO = new CIExyY(0.0, 0.0, 0.0);

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
