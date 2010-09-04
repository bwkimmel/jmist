/**
 * 
 */
package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Mask2</code> representing a checkboard pattern.
 * 
 * @author Brad Kimmel
 */
public final class CheckerMask2 implements Mask2 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -1602845395527865956L;

	/** The number of squares per unit in the x direction. */
	private final double nx;
	
	/** The number of squares per unit in the y direction. */
	private final double ny;
	
	/**
	 * Creates a new <code>CheckerMask2</code>.
	 * @param nx The number of squares per unit in the x direction.
	 * @param ny The number of squares per unit in the y direction.
	 */
	public CheckerMask2(double nx, double ny) {
		this.nx = nx;
		this.ny = ny;
	}
	
	/**
	 * Creates a new <code>CheckerMask2</code>.
	 * @param n The number of squares per unit in either direction.
	 */
	public CheckerMask2(double n) {
		this(n, n);
	}

	/**
	 * Creates a new <code>CheckerMask2</code> with a 2x2 checkerboard pattern.
	 */
	public CheckerMask2() {
		this(2);
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Mask2#opacity(ca.eandb.jmist.math.Point2)
	 */
	@Override
	public double opacity(Point2 p) {
		int j = (int) (Math.floor(p.x() * nx) + Math.floor(p.y() * ny));
		return ((j & 0x1) != 0) ? 1.0 : 0.0;
	}

}
