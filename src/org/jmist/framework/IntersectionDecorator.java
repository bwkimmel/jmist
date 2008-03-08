/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Vector3;

/**
 * An abstract <code>Intersection</code> that decorates another (e.g., by
 * applying a transformation).  Default implementations are provided that call
 * the corresponding methods on the decorated <code>Intersection</code>.  It is
 * up to the derived class to override whichever methods are necessary to
 * provide the specific decoration required.
 * @author bkimmel
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
	 * @see org.jmist.framework.Intersection#distance()
	 */
	public double distance() {
		return this.inner.distance();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Intersection#front()
	 */
	public boolean front() {
		return this.inner.front();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Intersection#incident()
	 */
	public Vector3 incident() {
		return this.inner.incident();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#ambientMedium()
	 */
	public Medium ambientMedium() {
		return this.inner.ambientMedium();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#basis()
	 */
	public Basis3 basis() {
		return this.inner.basis();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#closed()
	 */
	public boolean closed() {
		return this.inner.closed();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#location()
	 */
	public Point3 location() {
		return this.inner.location();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#material()
	 */
	public Material material() {
		return this.inner.material();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#microfacetBasis()
	 */
	public Basis3 microfacetBasis() {
		return this.inner.microfacetBasis();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#microfacetNormal()
	 */
	public Vector3 microfacetNormal() {
		return this.inner.microfacetNormal();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#normal()
	 */
	public Vector3 normal() {
		return this.inner.normal();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#tangent()
	 */
	public Vector3 tangent() {
		return this.inner.tangent();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.SurfacePoint#textureCoordinates()
	 */
	public Point2 textureCoordinates() {
		return this.inner.textureCoordinates();
	}

	/** The decorated <code>Intersection</code>. */
	protected final Intersection inner;

}
