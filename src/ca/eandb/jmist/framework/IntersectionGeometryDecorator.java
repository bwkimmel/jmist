/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * An abstract <code>IntersectionGeometry</code> that decorates another (e.g., by
 * applying a transformation).  Default implementations are provided that call
 * the corresponding methods on the decorated <code>IntersectionGeometry</code>.  It is
 * up to the derived class to override whichever methods are necessary to
 * provide the specific decoration required.
 * @author Brad Kimmel
 */
public abstract class IntersectionGeometryDecorator implements IntersectionGeometry {

	/**
	 * Initializes an <code>IntersectionGeometryDecorator</code>.
	 * @param inner The <code>IntersectionGeometry</code> to be decorated.
	 */
	protected IntersectionGeometryDecorator(IntersectionGeometry inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionGeometry#distance()
	 */
	public double getDistance() {
		return this.inner.getDistance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionGeometry#front()
	 */
	public boolean isFront() {
		return this.inner.isFront();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionGeometry#incident()
	 */
	public Vector3 getIncident() {
		return this.inner.getIncident();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#basis()
	 */
	public Basis3 getBasis() {
		return this.inner.getBasis();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#closed()
	 */
	public boolean isSurfaceClosed() {
		return this.inner.isSurfaceClosed();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#location()
	 */
	public Point3 getPosition() {
		return this.inner.getPosition();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#shadingBasis()
	 */
	public Basis3 getShadingBasis() {
		return this.inner.getShadingBasis();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#shadingNormal()
	 */
	public Vector3 getShadingNormal() {
		return this.inner.getShadingNormal();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#normal()
	 */
	public Vector3 getNormal() {
		return this.inner.getNormal();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#tangent()
	 */
	public Vector3 getTangent() {
		return this.inner.getTangent();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#textureCoordinates()
	 */
	public Point2 getUV() {
		return this.inner.getUV();
	}

	/** The decorated <code>IntersectionGeometry</code>. */
	protected final IntersectionGeometry inner;

}
