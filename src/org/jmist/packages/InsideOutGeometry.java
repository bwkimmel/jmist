/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.AbstractGeometry;
import org.jmist.framework.Geometry;
import org.jmist.framework.Intersection;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.Material;
import org.jmist.framework.Medium;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;
import org.jmist.toolkit.Vector3;

/**
 * A <code>Geometry</code> decorator that flips another <code>Geometry</code>
 * inside out.
 * @author bkimmel
 */
public final class InsideOutGeometry extends AbstractGeometry {

	/**
	 * Creates a new <code>InsideOutGeometry</code>.
	 * @param inner The <code>Geometry</code> to turn inside out.
	 */
	public InsideOutGeometry(Geometry inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		recorder = new InsideOutIntersectionRecorder(recorder);
		this.inner.intersect(ray, recorder);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return this.inner.isClosed();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		return this.inner.boundingBox();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return this.inner.boundingSphere();
	}

	/**
	 * An <code>IntersectionRecorder</code> decorator that flips the normals,
	 * bases, tangents and the {@link Intersection#front()} property of all
	 * <code>Intersection</code>s recorded to it.
	 * @author bkimmel
	 */
	private static final class InsideOutIntersectionRecorder implements IntersectionRecorder {

		/**
		 * Creates a new <code>InsideOutIntersectionRecorder</code>.
		 * @param inner The <code>IntersectionRecorder</code> to decorate.
		 */
		public InsideOutIntersectionRecorder(IntersectionRecorder inner) {
			this.inner = inner;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IntersectionRecorder#interval()
		 */
		@Override
		public Interval interval() {
			return this.inner.interval();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IntersectionRecorder#needAllIntersections()
		 */
		@Override
		public boolean needAllIntersections() {
			return this.inner.needAllIntersections();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IntersectionRecorder#record(org.jmist.framework.Intersection)
		 */
		@Override
		public void record(Intersection intersection) {
			this.inner.record(new InsideOutIntersection(intersection));
		}

		/**
		 * An <code>Intersection</code> decorator that flips the normals,
		 * bases, tangents, and the {@link Intersection#front()} properties of
		 * the decorated <code>Intersection</code>.
		 * @author bkimmel
		 */
		private static final class InsideOutIntersection implements Intersection {

			/**
			 * Creates a new <code>InsideOutIntersection</code>.
			 * @param inner The decorated <code>Intersection</code>.
			 */
			public InsideOutIntersection(Intersection inner) {
				this.inner = inner;
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.Intersection#distance()
			 */
			@Override
			public double distance() {
				return this.inner.distance();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.Intersection#front()
			 */
			@Override
			public boolean front() {
				return !this.inner.front();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.Intersection#incident()
			 */
			@Override
			public Vector3 incident() {
				return this.inner.incident();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#ambientMedium()
			 */
			@Override
			public Medium ambientMedium() {
				return this.inner.ambientMedium();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#basis()
			 */
			@Override
			public Basis3 basis() {
				return this.inner.basis().opposite();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#closed()
			 */
			@Override
			public boolean closed() {
				return this.inner.closed();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#illuminate(org.jmist.toolkit.Vector3, org.jmist.framework.Spectrum)
			 */
			@Override
			public void illuminate(Vector3 from, Spectrum irradiance) {
				// TODO Auto-generated method stub

			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#location()
			 */
			@Override
			public Point3 location() {
				return this.inner.location();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#material()
			 */
			@Override
			public Material material() {
				return this.inner.material();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#microfacetBasis()
			 */
			@Override
			public Basis3 microfacetBasis() {
				return this.inner.microfacetBasis().opposite();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#microfacetNormal()
			 */
			@Override
			public Vector3 microfacetNormal() {
				return this.inner.microfacetNormal().opposite();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#normal()
			 */
			@Override
			public Vector3 normal() {
				return this.inner.normal().opposite();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#tangent()
			 */
			@Override
			public Vector3 tangent() {
				return this.inner.tangent().opposite();
			}

			/* (non-Javadoc)
			 * @see org.jmist.framework.SurfacePoint#textureCoordinates()
			 */
			@Override
			public Point2 textureCoordinates() {
				return this.inner.textureCoordinates();
			}

			/** The decorated <code>Intersection</code>. */
			private final Intersection inner;

		}

		/** The decorated <code>IntersectionRecorder</code>. */
		private final IntersectionRecorder inner;

	}

	/** The decorated <code>Geometry</code>. */
	private final Geometry inner;

}
