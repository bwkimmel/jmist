/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IIntersection;
import org.jmist.framework.IIntersectionRecorder;

/**
 * An intersection recorder that only keeps the nearest intersection
 * recorded.
 * @author bkimmel
 */
public final class NearestIntersectionRecorder implements IIntersectionRecorder {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IIntersectionRecorder#needAllIntersections()
	 */
	public boolean needAllIntersections() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IIntersectionRecorder#record(org.jmist.framework.IIntersection)
	 */
	public void record(IIntersection intersection) {
		if (this.nearest == null || intersection.rayParameter() < this.nearest.rayParameter()) {
			this.nearest = intersection;
		}
	}

	/**
	 * Determines if this intersection recorder is empty.
	 * @return A value indicating if this intersection recorder is
	 * 		empty.
	 */
	public boolean isEmpty() {
		return (this.nearest == null);
	}

	/**
	 * Gets the intersection with the smallest ray parameter that has
	 * been recorded.
	 * @return The nearest intersection that has been recorded.
	 */
	public IIntersection nearestIntersection() {
		return this.nearest;
	}

	/** The nearest intersection that has been recorded so far. */
	private IIntersection nearest = null;

}