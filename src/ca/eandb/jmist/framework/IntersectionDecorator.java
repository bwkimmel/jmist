/**
 *
 */
package ca.eandb.jmist.framework;


/**
 * An abstract <code>Intersection</code> that decorates another (e.g., by
 * applying a transformation).  Default implementations are provided that call
 * the corresponding methods on the decorated <code>Intersection</code>.  It is
 * up to the derived class to override whichever methods are necessary to
 * provide the specific decoration required.
 * @author Brad Kimmel
 */
public abstract class IntersectionDecorator implements Intersection {

	/**
	 * Initializes an <code>IntersectionDecorator</code>.
	 * @param inner The <code>Intersection</code> to be decorated.
	 */
	protected IntersectionDecorator(Intersection inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Intersection#distance()
	 */
	public double getDistance() {
		return this.inner.getDistance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Intersection#front()
	 */
	public boolean isFront() {
		return this.inner.isFront();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Intersection#prepareShadingContext(ca.eandb.jmist.framework.ShadingContext)
	 */
	public final void prepareShadingContext(ShadingContext context) {
		inner.prepareShadingContext(context);
		transformShadingContext(context);
	}

	protected abstract void transformShadingContext(ShadingContext context);

	/** The decorated <code>Intersection</code>. */
	protected final Intersection inner;

}
