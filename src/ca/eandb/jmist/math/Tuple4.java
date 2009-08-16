/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A tuple of four values.
 * @author Brad Kimmel
 */
public class Tuple4 implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = -8356723610623735296L;

	/** A <code>Tuple4</code> of all zeros. */
	public static final Tuple4 ZERO = new Tuple4(0.0, 0.0, 0.0, 0.0);

	/** The components. */
	protected final double x, y, z, w;

	/**
	 * Initializes the components for the tuple.
	 * @param x The value of the first component.
	 * @param y The value of the second component.
	 * @param z The value of the third component.
	 * @param w The value of the fourth component.
	 */
	public Tuple4(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Gets the value of the first component.
	 * @return The value of the first component.
	 */
	public final double first() {
		return x;
	}

	/**
	 * Gets the value of the second component.
	 * @return The value of the second component.
	 */
	public final double second() {
		return y;
	}

	/**
	 * Gets the value of the third component.
	 * @return The value of the third component.
	 */
	public final double third() {
		return z;
	}

	/**
	 * Gets the value of the fourth component.
	 * @return The value of the fourth component.
	 */
	public final double fourth() {
		return w;
	}

	/**
	 * Gets the specified component of this tuple.
	 * @param index The index of the component to get.
	 * @return The specified component.
	 * @throws IllegalArgumentException if <code>index</code> is negative or
	 * 		greater than 3.
	 */
	public final double get(int index) {
		switch (index) {
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return z;
		case 3:
			return w;
		}
		throw new IllegalArgumentException();
	}

}
