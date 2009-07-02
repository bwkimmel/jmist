/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.function.ConstantFunction1;

/**
 * Represents a unary function.
 * @author Brad Kimmel
 */
public interface Function1 {

	/**
	 * Evaluates the function at the specified point in its domain.
	 * @param x The value at which to evaluate the function.
	 * @return The value of the function (units defined
	 * 		by the concrete class).
	 */
	double evaluate(double x);

	/**
	 * The identity <code>Function1</code>.
	 */
	public static final Function1 IDENTITY = new Function1() {
		public double evaluate(double x) {
			return x;
		}
	};

	/**
	 * A <code>Function1</code> that evaluates to zero everywhere.
	 */
	public static final Function1 ZERO = new ConstantFunction1(0.0);

	/**
	 * A <code>Function1</code> that evaluates to one everywhere.
	 */
	public static final Function1 ONE = new ConstantFunction1(1.0);

	/**
	 * A <code>Function1</code> whose value is <code>POSITIVE_INFINITY</code>
	 * everywhere.
	 * @see Double#POSITIVE_INFINITY
	 */
	public static final Function1 POSITIVE_INFINITY = new ConstantFunction1(Double.POSITIVE_INFINITY);

	/**
	 * A <code>Function1</code> whose value is <code>NEGATIVE_INFINITY</code>
	 * at all wavelengths.
	 * @see Double#NEGATIVE_INFINITY
	 */
	public static final Function1 NEGATIVE_INFINITY = new ConstantFunction1(Double.NEGATIVE_INFINITY);

}
