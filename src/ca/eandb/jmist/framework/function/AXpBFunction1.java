/**
 *
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;

/**
 * A <code>Function1</code> that adjusts another function (<code>x</code>)
 * according to <code>a*x + b</code>.
 * @author Brad Kimmel
 */
public final class AXpBFunction1 implements Function1 {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6722994298922073207L;

	/**
	 * Creates a new <code>ScaledFunction1</code>.
	 * @param a The factor by which to multiply the decorated
	 * 		<code>Function1</code>.
	 * @param b The value to add to the product of <code>a</code> and
	 * 		<code>inner</code>
	 * @param inner The <code>Function1</code> to be adjusted.
	 */
	public AXpBFunction1(double a, double b, Function1 inner) {
		this.a = a;
		this.b = b;
		this.inner = inner;

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	public double evaluate(double x) {
		return a * this.inner.evaluate(x) + b;
	}

	/** The factor by which to multiply the decorated <code>Function1</code>. */
	private final double a;

	/**
	 * The value to add to the product of <code>a</code> and the decorated
	 * <code>Function1</code>.
	 */
	private final double b;

	/** The <code>Function1</code> to be adjusted. */
	private final Function1 inner;

}