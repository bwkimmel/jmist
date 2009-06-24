/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * An abstract <code>Intersection</code> that decorates another (e.g., by
 * applying a transformation).  Default implementations are provided that call
 * the corresponding methods on the decorated <code>Intersection</code>.  It is
 * up to the derived class to override whichever methods are necessary to
 * provide the specific decoration required.
 * @author Brad Kimmel
 */
public abstract class IntersectionDecorator implements Intersection {

	/**
	 * Initializes an <code>IntersectionDecorator</code>.
	 * @param inner The <code>Intersection</code> to be decorated.
	 */
	protected IntersectionDecorator(Intersection inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Intersection#distance()
	 */
	public double getDistance() {
		return this.inner.getDistance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Intersection#front()
	 */
	public boolean isFront() {
		return this.inner.isFront();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Intersection#incident()
	 */
	public Vector3 getIncident() {
		return this.inner.getIncident();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#basis()
	 */
	public Basis3 getBasis() {
		return this.inner.getBasis();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#location()
	 */
	public Point3 getPosition() {
		return this.inner.getPosition();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#shadingBasis()
	 */
	public Basis3 getShadingBasis() {
		return this.inner.getShadingBasis();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#shadingNormal()
	 */
	public Vector3 getShadingNormal() {
		return this.inner.getShadingNormal();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#normal()
	 */
	public Vector3 getNormal() {
		return this.inner.getNormal();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#tangent()
	 */
	public Vector3 getTangent() {
		return this.inner.getTangent();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#textureCoordinates()
	 */
	public Point2 getUV() {
		return this.inner.getUV();
	}

	/** The decorated <code>Intersection</code>. */
	protected final Intersection inner;

}
