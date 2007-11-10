/**
 *
 */
package org.jmist.packages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jmist.framework.Geometry;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;
import org.jmist.toolkit.Vector3;

/**
 * A <code>Geometry</code> that is composed of component geometries.
 * @author bkimmel
 */
public abstract class CompositeGeometry implements Geometry {

	/**
	 * Adds a child <code>Geometry</code> to this
	 * <code>CompositeGeometry</code>.
	 * @param child The child <code>Geometry</code> to add.
	 * @return A reference to this <code>CompositeGeometry</code> so that calls
	 * 		to this method may be chained.
	 */
	public CompositeGeometry addChild(Geometry child) {
		this.children.add(child);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.VisibilityFunction3#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {

		/* Compute the visibility function in terms of the intersect method. */
		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(I);

		this.intersect(ray, recorder);

		return recorder.isEmpty() || !I.contains(recorder.nearestIntersection().distance());

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.VisibilityFunction3#visibility(org.jmist.toolkit.Point3, org.jmist.toolkit.Point3)
	 */
	@Override
	public boolean visibility(Point3 p, Point3 q) {

		/* Determine the visibility in terms of the other overloaded
		 * method.
		 */
		Vector3		d		= p.vectorTo(q);
		Ray3		ray		= new Ray3(p, d.unit());
		Interval	I		= new Interval(0.0, d.length());

		return this.visibility(ray, I);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {

		/* The default behavior is pessimistic (i.e., the result is the union
		 * of the bounding boxes for each of the children.
		 */
		Collection<Box3> boxes = new ArrayList<Box3>();

		for (Geometry child : this.children) {
			boxes.add(child.boundingBox());
		}

		return Box3.smallestContaining(boxes);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {

		/* The default behavior is to return the sphere that bounds the
		 * bounding box.
		 */
		Box3 boundingBox = this.boundingBox();

		return new Sphere(boundingBox.center(), boundingBox.diagonal() / 2.0);

	}

	/**
	 * Gets the list of child geometries.
	 * @return The <code>List</code> of child geometries.
	 */
	protected final List<Geometry> children() {
		return this.children;
	}

	/** The child geometries. */
	private final List<Geometry> children = new ArrayList<Geometry>();

}
