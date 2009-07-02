/**
 *
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;

/**
 * A <code>Function1</code> that differs from another by a constant factor.
 * @author Brad Kimmel
 */
public final class ScaledFunction1 implements Function1 {

	/**
	 * Creates a new <code>ScaledFunction1</code>.
	 * @param factor The factor by which to multiply the decorated
	 * 		<code>Function1</code>.
	 * @param inner The <code>Function1</code> to be multiplied by a constant
	 * 		factor.
	 */
	public ScaledFunction1(double factor, Function1 inner) {

		/* Combine successive ScaledFunction1 instances into one. */
		while (inner instanceof ScaledFunction1) {
			ScaledFunction1 f = (ScaledFunction1) inner;
			factor *= f.factor;
			inner = f.inner;
		}

		this.factor = factor;
		this.inner = inner;

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	@Override
	public double evaluate(double x) {
		return this.factor * this.inner.evaluate(x);
	}

	/** The factor by which to multiply the decorated <code>Function1</code>. */
	private final double factor;

	/** The <code>Function1</code> to be multiplied by a constant factor. */
	private final Function1 inner;

}
