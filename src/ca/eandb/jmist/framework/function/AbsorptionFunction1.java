/**
 *
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;

/**
 * A <code>Function1</code> representing the absorption spectrum corresponding
 * to a given extinction index spectrum.
 * @author Brad Kimmel
 */
public final class AbsorptionFunction1 implements Function1 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -1219682001000227971L;

	/** The extinction index to convert to an absorption spectrum. */
	private final Function1 k;

	/**
	 * Creates a new <code>AbsorptionFunction1</code>.
	 * @param k The <code>Function1</code> representing the extinction index
	 * 		spectrum.
	 */
	public AbsorptionFunction1(Function1 k) {
		this.k = k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	public double evaluate(double x) {
		return 4.0 * Math.PI * k.evaluate(x) / x;
	}

}
