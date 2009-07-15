/**
 *
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;



/**
 * A <code>Function1</code> that has the same value everywhere.  This
 * class is immutable.
 * @author Brad Kimmel
 */
public class ConstantFunction1 implements Function1 {

	/**
	 * Creates a new <code>ConstantFunction1</code>.
	 * @param value The value of the <code>Function1</code> everywhere.
	 */
	public ConstantFunction1(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	public double evaluate(double x) {
		return this.value;
	}

	/** The value of this <code>Function1</code> everywhere. */
	private final double value;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 8993721217448172058L;

}
