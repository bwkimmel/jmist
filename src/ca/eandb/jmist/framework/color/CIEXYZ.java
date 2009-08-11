/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.Tuple3;

/**
 * @author Brad
 *
 */
public class CIEXYZ extends Tuple3 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -3369371416389668782L;

	public static final CIEXYZ ZERO = new CIEXYZ(0.0, 0.0, 0.0);

	/**
	 * @param X
	 * @param Y
	 * @param Z
	 */
	public CIEXYZ(double X, double Y, double Z) {
		super(X, Y, Z);
	}

	public final double X() {
		return x;
	}

	public final double Y() {
		return y;
	}

	public final double Z() {
		return z;
	}

	public final CIEXYZ plus(CIEXYZ other) {
		return new CIEXYZ(x + other.x, y + other.y, z + other.z);
	}

	public final CIEXYZ minus(CIEXYZ other) {
		return new CIEXYZ(x - other.x, y - other.y, z - other.z);
	}

	public final CIEXYZ divide(CIEXYZ other) {
		return new CIEXYZ(x / other.x, y / other.y, z / other.z);
	}

	public final CIEXYZ divide(double c) {
		return new CIEXYZ(x / c, y / c, z / c);
	}

	public final CIEXYZ times(CIEXYZ other) {
		return new CIEXYZ(x * other.x, y * other.y, z * other.z);
	}

	public final CIEXYZ times(double c) {
		return new CIEXYZ(x * c, y * c, z * c);
	}

}
