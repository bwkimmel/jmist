/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * A <code>Geometry</code> that is composed of component geometries.
 * @author Brad Kimmel
 */
public class CompositeGeometry extends AbstractGeometry {

	public CompositeGeometry() {
		offsets.add(0);
	}

	/**
	 * Adds a child <code>Geometry</code> to this
	 * <code>CompositeGeometry</code>.
	 * @param child The child <code>Geometry</code> to add.
	 * @return A reference to this <code>CompositeGeometry</code> so that calls
	 * 		to this method may be chained.
	 */
	public CompositeGeometry addChild(Geometry child) {
		offsets.add(getNumPrimitives() + child.getNumPrimitives());
		children.add(child);
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
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
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		int childIndex = getChildIndex(index);
		int offset = offsets.get(childIndex);
		Geometry child = children.get(childIndex);
		return child.getBoundingBox(index - offset);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		int childIndex = getChildIndex(index);
		int offset = offsets.get(childIndex);
		Geometry child = children.get(childIndex);
		return child.getBoundingSphere(index - offset);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#getNumPrimitives()
	 */
	@Override
	public int getNumPrimitives() {
		return offsets.get(offsets.size() - 1);
	}

	private int getChildIndex(int index) {
		if (index < 0 || index >= getNumPrimitives()) {
			throw new IndexOutOfBoundsException();
		}

		int childIndex = Collections.binarySearch(offsets, index);
		if (childIndex < 0) {
			childIndex = -(childIndex + 1);
		}

		return childIndex;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		int childIndex = getChildIndex(index);
		int offset = offsets.get(childIndex);
		Geometry child = children.get(childIndex);
		child.intersect(index - offset, ray, recorder);
	}

	/** The child geometries. */
	private final List<Geometry> children = new ArrayList<Geometry>();

	private final List<Integer> offsets = new ArrayList<Integer>();

}
