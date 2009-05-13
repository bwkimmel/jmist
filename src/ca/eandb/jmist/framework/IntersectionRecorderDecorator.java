/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Interval;

/**
 * An abstract <code>IntersectionRecorder</code> that decorates another.  This
 * will typically be used to decorate an <code>Intesection</code> prior to
 * recording it to the decorated <code>IntersectionRecorder</code>.
 * @author Brad Kimmel
 */
public abstract class IntersectionRecorderDecorator implements
		IntersectionRecorder {

	/**
	 * Initializes an <code>IntersectionRecorderDecorator</code>.
	 * @param inner The <code>IntersectionRecorder</code> to be decorated.
	 */
	protected IntersectionRecorderDecorator(IntersectionRecorder inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionRecorder#interval()
	 */
	public Interval interval() {
		return this.inner.interval();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionRecorder#needAllIntersections()
	 */
	public boolean needAllIntersections() {
		return this.inner.needAllIntersections();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionRecorder#isEmpty()
	 */
	public boolean isEmpty() {
		return this.inner.isEmpty();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionRecorder#record(ca.eandb.jmist.framework.Intersection)
	 */
	public abstract void record(Intersection intersection);

	/** The decorated <code>IntersectionRecorder</code>. */
	protected final IntersectionRecorder inner;

}
