/**
 *
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.math.MathUtil;

/**
 * A piecewise linear <code>Function1</code>.
 * @author Brad Kimmel
 */
public final class PiecewiseLinearFunction1 implements Function1 {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4156156016195155933L;

	/**
	 * Creates a new <code>PiecewiseLinearFunction1</code>.
	 * @param xs The array of domain points.
	 * @param ys The array of values corresponding to the domain points.
	 */
	public PiecewiseLinearFunction1(double[] xs, double[] ys) {
		this.xs = xs.clone();
		this.ys = ys.clone();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	public double evaluate(double x) {
		return MathUtil.interpolate(xs, ys, x);
	}

	/** The array of domain points. */
	private final double[] xs;

	/** The array of values corresponding to the domain points. */
	private final double[] ys;

}
