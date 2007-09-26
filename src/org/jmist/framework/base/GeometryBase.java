/**
 *
 */
package org.jmist.framework.base;

import org.jmist.framework.IGeometry;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;
import org.jmist.packages.NearestIntersectionRecorder;

/**
 * Base class for geometry classes.
 * @author bkimmel
 */
public abstract class GeometryBase implements IGeometry {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IVisibilityFunction3#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	public boolean visibility(Ray3 ray, Interval I) {

		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder();

		this.intersect(ray, I, recorder);

		return recorder.isEmpty() || !I.contains(recorder.nearestIntersection().rayParameter());

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IVisibilityFunction3#visibility(org.jmist.toolkit.Point3, org.jmist.toolkit.Point3)
	 */
	public boolean visibility(Point3 p, Point3 q) {

		/*
		 * Determine the visibility in terms of the other overloaded
		 * method.
		 */
		Vector3		d		= p.vectorTo(q);
		Ray3		ray		= new Ray3(p, d.unit());
		Interval	I		= new Interval(0.0, d.length());

		return this.visibility(ray, I);

	}

}
