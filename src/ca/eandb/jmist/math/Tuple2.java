/**
 *
 */
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A tuple of two values.
 * @author Brad Kimmel
 */
public class Tuple2 implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = -8925784794460530216L;

	/** A <code>Tuple3</code> of all zeros. */
	public static final Tuple2 ZERO = new Tuple2(0.0, 0.0);

	/** The components. */
	protected final double x, y;

	/**
	 * Initializes the components for the tuple.
	 * @param x The value of the first component.
	 * @param y The value of the second component.
	 */
	public Tuple2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the value of the first component.
	 * @return The value of the first component.
	 */
	public final double x() {
		return x;
	}

	/**
	 * Gets the value of the second component.
	 * @return The value of the second component.
	 */
	public final double y() {
		return y;
	}

	/**
	 * Gets the specified component of this tuple.
	 * @param index The index of the component to get.
	 * @return The specified component.
	 * @throws IllegalArgumentException if <code>index</code> is negative or
	 * 		greater than 1.
	 */
	public final double get(int index) {
		switch (index) {
		case 0:
			return x;
		case 1:
			return y;
		}
		throw new IllegalArgumentException();
	}

}
