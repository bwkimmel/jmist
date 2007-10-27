/**
 *
 */
package org.jmist.framework.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jmist.framework.Intersection;
import org.jmist.toolkit.BoundingBoxBuilder3;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;

/**
 * @author bkimmel
 *
 */
public class CompositeModelElement extends AbstractModelElement {

	/**
	 * Initializes the name of this model element.
	 * @param name The name of this model element.
	 */
	public CompositeModelElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.AbstractModelElement#addChild(org.jmist.framework.model.ModelElement)
	 */
	@Override
	public void addChild(ModelElement child) throws UnsupportedOperationException {
		children.add(child);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.ModelElement#removeChild(org.jmist.framework.model.ModelElement)
	 */
	@Override
	public void removeChild(ModelElement child) {

		Iterator<ModelElement> i = this.children.iterator();

		while (i.hasNext()) {
			if (i.next() == child) {
				i.remove();
				break;
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.AbstractModelElement#children()
	 */
	@Override
	public Iterable<ModelElement> children() {
		return children;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.AbstractModelElement#getBoundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();

		for (ModelElement child : this.children()) {
			builder.add(child.boundingBox());
		}

		return builder.getBoundingBox();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.AbstractModelElement#getBoundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		// TODO Create a bounding sphere builder and use that instead.
		Box3 boundingBox = this.boundingBox();
		return new Sphere(boundingBox.center(), boundingBox.diagonal() / 2.0);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.AbstractModelElement#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {
		for (ModelElement child : this.children()) {
			if (!child.visibility(ray, I)) {
				return false;
			}
		}

		return true;
	}

	private List<ModelElement> children = new ArrayList<ModelElement>();

}
