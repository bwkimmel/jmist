/**
 *
 */
package org.jmist.framework.model;

import org.jmist.framework.IIntersection;
import org.jmist.framework.ISurfacePoint;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;

/**
 * Abstract base class for model elements.
 * @author bkimmel
 */
public abstract class ModelElement implements IModelElement {

	/**
	 * Initializes the name of this model element.
	 * @param name The name of this model element.
	 */
	public ModelElement(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.IModelElement#addChild(org.jmist.framework.model.IModelElement)
	 */
	public void addChild(IModelElement child) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Model element may not contain children.");
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.IModelElement#children()
	 */
	public Iterable<IModelElement> children() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.IModelElement#contains(org.jmist.toolkit.Point3)
	 */
	public boolean contains(Point3 p) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.IModelElement#findChild(java.lang.String)
	 */
	public IModelElement findChild(String name) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.IModelElement#generateRandomSurfacePoint()
	 */
	public ISurfacePoint generateRandomSurfacePoint() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.IModelElement#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.model.IModelElement#removeChild(java.lang.String)
	 */
	public void removeChild(String name) {
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IVisibilityFunction3#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	public boolean visibility(Ray3 ray, Interval I) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IVisibilityFunction3#visibility(org.jmist.toolkit.Point3, org.jmist.toolkit.Point3)
	 */
	public boolean visibility(Point3 p, Point3 q) {
		return visibility(new Ray3(p, p.vectorTo(q)), Interval.UNIT);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IBounded3#getBoundingBox()
	 */
	public Box3 getBoundingBox() {
		return Box3.EMPTY;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IBounded3#getBoundingSphere()
	 */
	public Sphere getBoundingSphere() {
		return Sphere.EMPTY;
	}

	/**
	 * The name of this model element.
	 */
	private final String name;

}
