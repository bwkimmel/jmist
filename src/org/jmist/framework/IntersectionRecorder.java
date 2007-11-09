package org.jmist.framework;

import org.jmist.toolkit.Interval;

/**
 * Accepts a series of intersections.
 * @author bkimmel
 */
public interface IntersectionRecorder {

	/**
	 * Record an intersection.
	 * @param intersection The intersection to record.
	 */
	void record(Intersection intersection);

	/**
	 * Gets the <code>Interval</code> that this
	 * <code>IntersectionRecorder</code> expects recorded
	 * <code>Intersection</code>s to fall within.
	 * @return The <code>Interval</code> in which to record intersections.
	 */
	Interval interval();

	/**
	 * Indicates whether all intersections are required or
	 * just the nearest one.
	 * @return A value indicating whether all intersections
	 * 		should be recorded.
	 */
	boolean needAllIntersections();

}
