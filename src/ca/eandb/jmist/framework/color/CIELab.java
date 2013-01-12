/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.Tuple3;

/**
 * A color in CIE Lab color space.
 * @see <a href="http://en.wikipedia.org/wiki/CIE_Lab">CIE Lab color space</a>
 * @author Brad Kimmel
 */
public final class CIELab extends Tuple3 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -6643876217801917072L;

	public static final CIELab ZERO = new CIELab(0.0, 0.0, 0.0);

	/**
	 * @param L
	 * @param a
	 * @param b
	 */
	public CIELab(double L, double a, double b) {
		super(L, a, b);
	}

	public static double deltaE(CIELab p, CIELab q) {
		double dL = p.x - q.x;
		double da = p.y - q.y;
		double db = p.z - q.z;
		return Math.sqrt(dL * dL + da * da + db * db);
	}

	public double L() {
		return x;
	}

	public double a() {
		return y;
	}

	public double b() {
		return z;
	}

	public CIELab times(double c) {
		return new CIELab(x * c, y, z);
	}

	public CIELab divide(double c) {
		return new CIELab(x / c, y, z);
	}

	public CIEXYZ toXYZ(CIEXYZ ref) {
		return ColorUtil.convertLab2XYZ(this, ref);
	}

	public static CIELab fromXYZ(double X, double Y, double Z, CIEXYZ ref) {
		return ColorUtil.convertXYZ2Lab(X, Y, Z, ref);
	}

	public static CIELab fromXYZ(CIEXYZ xyz, CIEXYZ ref) {
		return ColorUtil.convertXYZ2Lab(xyz, ref);
	}

}
