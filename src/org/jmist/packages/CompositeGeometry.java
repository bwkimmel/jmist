/**
 *
 */
package org.jmist.packages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jmist.framework.AbstractGeometry;
import org.jmist.framework.Geometry;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Sphere;

/**
 * A <code>Geometry</code> that is composed of component geometries.
 * @author bkimmel
 */
public abstract class CompositeGeometry extends AbstractGeometry {

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

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#isClosed()
	 */
	@Override
	public boolean isClosed() {
		for (Geometry child : this.children) {
			if (!child.isClosed()) {
				return false;
			}
		}
		return true;
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
