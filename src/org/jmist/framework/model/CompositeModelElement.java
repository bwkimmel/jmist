/**
 *
 */
package org.jmist.framework.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jmist.framework.IIntersection;
import org.jmist.toolkit.BoundingBoxBuilder3;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;

/**
 * @author bkimmel
 *
 */
public class CompositeModelElement extends ModelElement {

	/**
	 * Initializes the name of this model element.
	 * @param name The name of this model element.
	 */
	public CompositeModelElement(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.ModelElement#addChild(org.jmist.framework.model.IModelElement)
	 */
	@Override
	public void addChild(IModelElement child) throws UnsupportedOperationException {
		children.add(child);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.ModelElement#children()
	 */
	@Override
	public Iterable<IModelElement> children() {
		return children;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.ModelElement#findChild(java.lang.String)
	 */
	@Override
	public IModelElement findChild(String name) {
		for (IModelElement child : this.children()) {
			if (child.getName().equals(name)) {
				return child;
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.ModelElement#getBoundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();

		for (IModelElement child : this.children()) {
			builder.add(child.boundingBox());
		}

		return builder.getBoundingBox();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.ModelElement#getBoundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		// TODO Create a bounding sphere builder and use that instead.
		Box3 boundingBox = this.boundingBox();
		return new Sphere(boundingBox.center(), boundingBox.diagonal() / 2.0);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.ModelElement#removeChild(java.lang.String)
	 */
	@Override
	public void removeChild(String name) {
		for (Iterator<IModelElement> iter = children.iterator(); iter.hasNext();) {
			if (iter.next().getName().equals(name)) {
				iter.remove();
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.ModelElement#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {
		for (IModelElement child : this.children()) {
			if (!child.visibility(ray, I)) {
				return false;
			}
		}

		return true;
	}

	private List<IModelElement> children = new ArrayList<IModelElement>();

}
