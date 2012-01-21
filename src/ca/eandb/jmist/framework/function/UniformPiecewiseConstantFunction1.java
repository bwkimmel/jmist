/**
 * 
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;

/**
 * A piecewise constant <code>Function1</code> with equally spaced bins within
 * its domain.
 * 
 * @author Brad Kimmel
 */
public final class UniformPiecewiseConstantFunction1 implements Function1 {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 816595096646372284L;

	/** The <code>Interval</code> within which values are specified. */
	private final Interval domain;
	
	/** The array of values for each bin. */
	private final double[] values;
	
	/**
	 * Creates a new <code>UniformPiecewiseConstantFunction1</code>.
	 * @param domain The <code>Interval</code> within which values are
	 * 		specified.  It must be non-empty and finite.
	 * @param values The array of function values.  There must be at least one
	 * 		value.
	 * @throws IllegalArgumentException if <code>domain</code> is empty or
	 * 		infinite, or if <code>values.length &lt; 1</code>.
	 * @see Interval#isEmpty()
	 * @see Interval#isInfinite()
	 */
	public UniformPiecewiseConstantFunction1(Interval domain, double[] values) {
		if (values.length < 1) {
			throw new IllegalArgumentException("values.length must be at least 1");
		}
		if (domain.isEmpty() || domain.isInfinite()) {
			throw new IllegalArgumentException("domain must be finite and non-empty");
		}
		this.domain = domain;
		this.values = values.clone();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	@Override
	public double evaluate(double x) {
		if (domain.contains(x)) {
			int bin = MathUtil.clamp((int) Math.floor(values.length * (x - domain.minimum()) / domain.length()), 0, values.length - 1);
			return values[bin];
		} else {
			return 0.0;
		}
	}

}
