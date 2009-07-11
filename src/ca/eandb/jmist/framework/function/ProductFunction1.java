/**
 *
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;

/**
 * A <code>Function1</code> that is the product of other functions.
 * @author Brad Kimmel
 */
public final class ProductFunction1 extends CompositeFunction1 {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 3613122771597657293L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.function.CompositeFunction1#addChild(ca.eandb.jmist.framework.Function1)
	 */
	@Override
	public ProductFunction1 addChild(Function1 child) {
		if (child instanceof ProductFunction1) {
			ProductFunction1 product = (ProductFunction1) child;
			for (Function1 grandChild : product.children()) {
				this.addChild(grandChild);
			}
		} else {
			super.addChild(child);
		}
		return this;
	}

	/**
	 * Creates a new <code>ProductFunction1</code>.
	 */
	public ProductFunction1() {
		/* do nothing */
	}

	/**
	 * Creates a new <code>ProductFunction1</code>.
	 * @param a The first <code>Function1</code>.
	 * @param b The second <code>Function1</code>.
	 */
	public ProductFunction1(Function1 a, Function1 b) {
		this.addChild(a).addChild(b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Function1#evaluate(double)
	 */
	@Override
	public double evaluate(double x) {

		double product = 1.0;

		for (Function1 child : this.children()) {
			product *= child.evaluate(x);
		}

		return product;

	}

}
