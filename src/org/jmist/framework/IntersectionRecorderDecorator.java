/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Interval;

/**
 * An abstract <code>IntersectionRecorder</code> that decorates another.  This
 * will typically be used to decorate an <code>Intesection</code> prior to
 * recording it to the decorated <code>IntersectionRecorder</code>.
 * @author bkimmel
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
	 * @see org.jmist.framework.IntersectionRecorder#interval()
	 */
	@Override
	public Interval interval() {
		return this.inner.interval();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IntersectionRecorder#needAllIntersections()
	 */
	@Override
	public boolean needAllIntersections() {
		return this.inner.needAllIntersections();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IntersectionRecorder#record(org.jmist.framework.Intersection)
	 */
	@Override
	public abstract void record(Intersection intersection);

	/** The decorated <code>IntersectionRecorder</code>. */
	protected final IntersectionRecorder inner;

}
