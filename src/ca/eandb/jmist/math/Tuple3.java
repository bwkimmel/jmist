/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A tuple of three values.
 * @author Brad Kimmel
 */
public class Tuple3 implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = 4706855687546418665L;

	/** A <code>Tuple3</code> of all zeros. */
	public static final Tuple3 ZERO = new Tuple3(0.0, 0.0, 0.0);

	/** The components. */
	protected final double x, y, z;

	/**
	 * Initializes the components for the tuple.
	 * @param x The value of the first component.
	 * @param y The value of the second component.
	 * @param z The value of the third component.
	 */
	public Tuple3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	 * Gets the specified component of this tuple.
	 * @param index The index of the component to get.
	 * @return The specified component.
	 * @throws IllegalArgumentException if <code>index</code> is negative or
	 * 		greater than 2.
	 */
	public final double get(int index) {
		switch (index) {
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return z;
		}
		throw new IllegalArgumentException();
	}

}
