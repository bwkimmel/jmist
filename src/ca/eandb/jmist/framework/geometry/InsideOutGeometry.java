/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * A <code>SceneElement</code> decorator that flips another <code>SceneElement</code>
 * inside out.
 * @author Brad Kimmel
 */
public final class InsideOutGeometry extends AbstractGeometry {

	/**
	 * Creates a new <code>InsideOutGeometry</code>.
	 * @param inner The <code>SceneElement</code> to turn inside out.
	 */
	public InsideOutGeometry(SceneElement inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		recorder = new InsideOutIntersectionRecorder(recorder);
		this.inner.intersect(index, ray, recorder);
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		return inner.getBoundingBox(index);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		return inner.getBoundingSphere(index);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	@Override
	public int getNumPrimitives() {
		return inner.getNumPrimitives();
	}

	/**
	 * An <code>IntersectionRecorder</code> decorator that flips the normals,
	 * bases, tangents and the {@link Intersection#isFront()} property of all
	 * <code>Intersection</code>s recorded to it.
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
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#record(ca.eandb.jmist.framework.Intersection)
		 */
		@Override
		public void record(Intersection intersection) {
			this.inner.record(new InsideOutIntersection(intersection));
		}

		/**
		 * An <code>Intersection</code> decorator that flips the normals,
		 * bases, tangents, and the {@link Intersection#isFront()} properties of
		 * the decorated <code>Intersection</code>.
		 * @author Brad Kimmel
		 */
		private static final class InsideOutIntersection extends IntersectionDecorator {

			/**
			 * Creates a new <code>InsideOutIntersection</code>.
			 * @param inner The decorated <code>Intersection</code>.
			 */
			public InsideOutIntersection(Intersection inner) {
				super(inner);
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionDecorator#front()
			 */
			@Override
			public boolean isFront() {
				return !this.inner.isFront();
			}

			@Override
			protected void transformShadingContext(ShadingContext context) {
				context.setBasis(context.getBasis().opposite());
				context.setShadingBasis(context.getShadingBasis().opposite());
			}

		}

	}

	/** The decorated <code>SceneElement</code>. */
	private final SceneElement inner;

}
