/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.IntersectionGeometry;
import ca.eandb.jmist.framework.IntersectionGeometryDecorator;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Geometry</code> decorator that flips another <code>Geometry</code>
 * inside out.
 * @author Brad Kimmel
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
	 * @see ca.eandb.jmist.framework.Geometry#intersect(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		recorder = new InsideOutIntersectionRecorder(recorder);
		this.inner.intersect(ray, recorder);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#isClosed()
	 */
	public boolean isClosed() {
		return this.inner.isClosed();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		return this.inner.boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return this.inner.boundingSphere();
	}

	/**
	 * An <code>IntersectionRecorder</code> decorator that flips the normals,
	 * bases, tangents and the {@link IntersectionGeometry#front()} property of all
	 * <code>IntersectionGeometry</code>s recorded to it.
	 * @author Brad Kimmel
	 */
	private static final class InsideOutIntersectionRecorder extends
			IntersectionRecorderDecorator {

		/**
		 * Creates a new <code>InsideOutIntersectionRecorder</code>.
		 * @param inner The <code>IntersectionRecorder</code> to decorate.
		 */
		public InsideOutIntersectionRecorder(IntersectionRecorder inner) {
			super(inner);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#record(ca.eandb.jmist.framework.IntersectionGeometry)
		 */
		@Override
		public void record(IntersectionGeometry intersection) {
			this.inner.record(new InsideOutIntersection(intersection));
		}

		/**
		 * An <code>IntersectionGeometry</code> decorator that flips the normals,
		 * bases, tangents, and the {@link IntersectionGeometry#front()} properties of
		 * the decorated <code>IntersectionGeometry</code>.
		 * @author Brad Kimmel
		 */
		private static final class InsideOutIntersection extends IntersectionGeometryDecorator {

			/**
			 * Creates a new <code>InsideOutIntersection</code>.
			 * @param inner The decorated <code>IntersectionGeometry</code>.
			 */
			public InsideOutIntersection(IntersectionGeometry inner) {
				super(inner);
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#front()
			 */
			@Override
			public boolean front() {
				return !this.inner.front();
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#basis()
			 */
			@Override
			public Basis3 basis() {
				return this.inner.basis().opposite();
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#shadingBasis()
			 */
			@Override
			public Basis3 shadingBasis() {
				return this.inner.shadingBasis().opposite();
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#shadingNormal()
			 */
			@Override
			public Vector3 shadingNormal() {
				return this.inner.shadingNormal().opposite();
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#normal()
			 */
			@Override
			public Vector3 normal() {
				return this.inner.normal().opposite();
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#tangent()
			 */
			@Override
			public Vector3 tangent() {
				return this.inner.tangent().opposite();
			}

		}

	}

	/** The decorated <code>Geometry</code>. */
	private final Geometry inner;

}
