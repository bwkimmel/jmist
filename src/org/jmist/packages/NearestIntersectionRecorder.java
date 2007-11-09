/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Intersection;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.toolkit.Interval;

/**
 * An intersection recorder that only keeps the nearest intersection
 * recorded.
 * @author bkimmel
 */
public final class NearestIntersectionRecorder implements IntersectionRecorder {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IntersectionRecorder#needAllIntersections()
	 */
	@Override
	public boolean needAllIntersections() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IntersectionRecorder#interval()
	 */
	@Override
	public Interval interval() {
		return Interval.POSITIVE;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IntersectionRecorder#record(org.jmist.framework.Intersection)
	 */
	@Override
	public void record(Intersection intersection) {
		if (this.interval().contains(intersection.distance())) {
			if (this.nearest == null || intersection.distance() < this.nearest.distance()) {
				this.nearest = intersection;
			}
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
	public Intersection nearestIntersection() {
		return this.nearest;
	}

	/** The nearest intersection that has been recorded so far. */
	private Intersection nearest = null;

}
