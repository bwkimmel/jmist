/**
 *
 */
package ca.eandb.jmist.framework.model;

import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * Abstract base class for model elements.
 * @author Brad Kimmel
 */
public abstract class AbstractModelElement implements ModelElement {

	/**
	 * Initializes the name of this model element.
	 * @param name The name of this model element.
	 */
	public AbstractModelElement(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.model.ModelElement#addChild(ca.eandb.jmist.framework.model.ModelElement)
	 */
	public void addChild(ModelElement child) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Model element may not contain children.");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.model.ModelElement#children()
	 */
	public Iterable<ModelElement> children() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.model.ModelElement#contains(ca.eandb.jmist.toolkit.Point3)
	 */
	public boolean contains(Point3 p) {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.model.ModelElement#findChild(java.lang.String)
	 */
	public ModelElement findChild(String name) {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.model.ModelElement#generateRandomSurfacePoint()
	 */
	public SurfacePoint generateRandomSurfacePoint() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.model.ModelElement#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.model.ModelElement#removeChild(java.lang.String)
	 */
	public void removeChild(String name) {
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.toolkit.Interval)
	 */
	public boolean visibility(Ray3 ray, Interval I) {
		return true;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
	 */
	public boolean visibility(Ray3 ray) {
		return true;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.toolkit.Point3, ca.eandb.jmist.toolkit.Point3)
	 */
	public boolean visibility(Point3 p, Point3 q) {
		return visibility(new Ray3(p, p.vectorTo(q)), Interval.UNIT);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#getBoundingBox()
	 */
	public Box3 boundingBox() {
		return Box3.EMPTY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#getBoundingSphere()
	 */
	public Sphere boundingSphere() {
		return Sphere.EMPTY;
	}

	/**
	 * The name of this model element.
	 */
	private final String name;

}
