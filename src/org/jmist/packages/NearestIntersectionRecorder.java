/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Geometry;
import org.jmist.framework.Intersection;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Ray3;
import org.jmist.util.MathUtil;

/**
 * An intersection recorder that only keeps the nearest intersection
 * recorded.
 * @author bkimmel
 */
public final class NearestIntersectionRecorder implements IntersectionRecorder {

	/**
	 * Creates a new <code>NearestIntersectionRecorder</code> that records
	 * <code>Intersection</code>s with a non-negligible positive distance.
	 */
	public NearestIntersectionRecorder() {
		this.interval = new Interval(MathUtil.EPSILON, Double.POSITIVE_INFINITY);
	}

	/**
	 * Creates a new <code>NearestIntersectionRecorder</code> that records
	 * <code>Intersection</code>s with a distance greater than that specified.
	 * @param epsilon The minimum distance to accept.
	 */
	public NearestIntersectionRecorder(double epsilon) {
		this.interval = new Interval(epsilon, Double.POSITIVE_INFINITY);
	}

	/**
	 * Creates a new <code>NearestIntersectionRecorder</code> that records
	 * <code>Intersection</code>s within the specified <code>Interval</code>.
	 * @param interval The <code>Interval</code> within which to accept
	 * 		<code>Intersection</code>s.
	 */
	public NearestIntersectionRecorder(Interval interval) {
		this.interval = interval;
	}

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
		return this.interval;
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

	/**
	 * Computes the nearest intersection of a <code>Ray3</code> with a
	 * <code>Geometry</code>.
	 * @param ray The <code>Ray3</code> to intersect with.
	 * @param geometry The <code>Geometry</code> to test for an intersection
	 * 		with.
	 * @return The nearest <code>Intersection</code>, or <code>null</code> if
	 * 		none exists.
	 */
	public static Intersection computeNearestIntersection(Ray3 ray, Geometry geometry) {
		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder();
		geometry.intersect(ray, recorder);
		return recorder.nearestIntersection();
	}

	/** The nearest intersection that has been recorded so far. */
	private Intersection nearest = null;

	/**
	 * The <code>Interval</code> within which to accept
	 * <code>Intersection</code>s.
	 */
	private final Interval interval;

}
