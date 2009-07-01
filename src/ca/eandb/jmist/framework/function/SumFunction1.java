/**
 *
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;

/**
 * A <code>Function1</code> that is the sum of other functions.
 * @author Brad Kimmel
 */
public final class SumFunction1 extends CompositeFunction1 {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.function.CompositeFunction1#addChild(ca.eandb.jmist.framework.Function1)
	 */
	@Override
	public SumFunction1 addChild(Function1 child) {
		if (child instanceof SumFunction1) {
			SumFunction1 sum = (SumFunction1) child;
			for (Function1 grandChild : sum.children()) {
				this.addChild(grandChild);
			}
		} else {
			super.addChild(child);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	@Override
	public double evaluate(double x) {

		double sum = 0.0;

		for (Function1 component : this.children()) {
			sum += component.evaluate(x);
		}

		return sum;

	}

}
