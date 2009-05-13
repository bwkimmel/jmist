/**
 *
 */
package ca.eandb.jmist.framework;

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
	public double distance() {
		return this.inner.distance();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Intersection#front()
	 */
	public boolean front() {
		return this.inner.front();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Intersection#incident()
	 */
	public Vector3 incident() {
		return this.inner.incident();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#ambientMedium()
	 */
	public Medium ambientMedium() {
		return this.inner.ambientMedium();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#basis()
	 */
	public Basis3 basis() {
		return this.inner.basis();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#closed()
	 */
	public boolean closed() {
		return this.inner.closed();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#location()
	 */
	public Point3 location() {
		return this.inner.location();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#material()
	 */
	public Material material() {
		return this.inner.material();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#microfacetBasis()
	 */
	public Basis3 microfacetBasis() {
		return this.inner.microfacetBasis();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#microfacetNormal()
	 */
	public Vector3 microfacetNormal() {
		return this.inner.microfacetNormal();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#normal()
	 */
	public Vector3 normal() {
		return this.inner.normal();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#tangent()
	 */
	public Vector3 tangent() {
		return this.inner.tangent();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#textureCoordinates()
	 */
	public Point2 textureCoordinates() {
		return this.inner.textureCoordinates();
	}

	/** The decorated <code>Intersection</code>. */
	protected final Intersection inner;

}
