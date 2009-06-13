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
	public double distance() {
		return this.inner.distance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionGeometry#front()
	 */
	public boolean front() {
		return this.inner.front();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.IntersectionGeometry#incident()
	 */
	public Vector3 incident() {
		return this.inner.incident();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#basis()
	 */
	public Basis3 basis() {
		return this.inner.basis();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#closed()
	 */
	public boolean closed() {
		return this.inner.closed();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#location()
	 */
	public Point3 location() {
		return this.inner.location();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#shadingBasis()
	 */
	public Basis3 shadingBasis() {
		return this.inner.shadingBasis();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#shadingNormal()
	 */
	public Vector3 shadingNormal() {
		return this.inner.shadingNormal();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#normal()
	 */
	public Vector3 normal() {
		return this.inner.normal();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#tangent()
	 */
	public Vector3 tangent() {
		return this.inner.tangent();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePointGeometry#textureCoordinates()
	 */
	public Point2 textureCoordinates() {
		return this.inner.textureCoordinates();
	}

	/** The decorated <code>IntersectionGeometry</code>. */
	protected final IntersectionGeometry inner;

}
