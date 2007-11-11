/**
 *
 */
package org.jmist.packages;

import java.util.ArrayList;
import java.util.List;

import org.jmist.framework.AffineTransformable3;
import org.jmist.framework.Geometry;
import org.jmist.framework.Intersection;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.InvertibleAffineTransformation3;
import org.jmist.framework.Material;
import org.jmist.framework.Medium;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.AffineMatrix3;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.LinearMatrix3;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;
import org.jmist.toolkit.Vector3;

/**
 * A <code>CompositeGeometry</code> that may be transformed.
 * @author bkimmel
 */
public final class TransformableGeometry extends CompositeGeometry implements
		AffineTransformable3 {

	/**
	 * Creates a new <code>TransformableGeometry</code>.
	 */
	public TransformableGeometry() {
		/* do nothing */
	}

	/**
	 * Creates a new <code>TransformableGeometry</code>.
	 * @param geometry The <code>Geometry</code> to make transformable.
	 */
	public TransformableGeometry(Geometry geometry) {
		this.addChild(geometry);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		ray			= this.model.applyInverse(ray);
		recorder	= new TransformedIntersectionRecorder(recorder);

		for (Geometry geometry : this.children()) {
			geometry.intersect(ray, recorder);
		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#isClosed()
	 */
	@Override
	public boolean isClosed() {
		for (Geometry geometry : this.children()) {
			if (!geometry.isClosed()) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {

		List<Point3> corners = new ArrayList<Point3>(8 * this.children().size());

		for (Geometry geometry : this.children()) {
			Box3 childBoundingBox = geometry.boundingBox();
			for (int i = 0; i < 8; i++) {
				corners.add(this.model.apply(childBoundingBox.corner(i)));
			}
		}

		return Box3.smallestContainingPoints(corners);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {

		List<Point3> corners = new ArrayList<Point3>(8 * this.children().size());

		for (Geometry geometry : this.children()) {
			Box3 childBoundingBox = geometry.boundingBox();
			for (int i = 0; i < 8; i++) {
				corners.add(this.model.apply(childBoundingBox.corner(i)));
			}
		}

		return Sphere.smallestContaining(corners);

	}

	/**
	 * An <code>IntersectionRecorder</code> that transforms the recorded
	 * intersections according to the transformation for this
	 * <code>TransformableGeometry</code>.
	 * @author bkimmel
	 */
	private final class TransformedIntersectionRecorder implements IntersectionRecorder {

		/**
		 * Creates a new <code>TransformedIntersectionRecorder</code>.
		 * @param inner The <code>IntersectionRecorder</code> to record
		 * 		intersections to.
		 */
		public TransformedIntersectionRecorder(IntersectionRecorder inner) {
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
			this.inner.record(new TransformedIntersection(intersection));
		}

		/** The <code>IntersectionRecorder</code> to record intersections to. */
		private final IntersectionRecorder inner;

	}

	/**
	 * An <code>Intersection</code> that has been transformed according to the
	 * transformation applied to this <code>TransformableGeometry</code>.
	 * @author bkimmel
	 */
	private final class TransformedIntersection implements Intersection {

		/**
		 * Creates a new <code>TransformedIntersection</code>.
		 * @param local The <code>Intersection</code> in local coordinate
		 * 		space.
		 */
		public TransformedIntersection(Intersection local) {
			this.local = local;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Intersection#distance()
		 */
		@Override
		public double distance() {
			return this.local.distance();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Intersection#front()
		 */
		@Override
		public boolean front() {
			return this.local.front();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Intersection#incident()
		 */
		@Override
		public Vector3 incident() {
			return model.apply(this.local.incident());
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#ambientMedium()
		 */
		@Override
		public Medium ambientMedium() {
			return this.local.ambientMedium();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#basis()
		 */
		@Override
		public Basis3 basis() {
			Basis3 localBasis = this.local.basis();
			return Basis3.fromUV(
					model.apply(localBasis.u()),
					model.apply(localBasis.v()),
					localBasis.orientation()
			);
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#closed()
		 */
		@Override
		public boolean closed() {
			return this.local.closed();
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
			return model.apply(this.local.location());
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#material()
		 */
		@Override
		public Material material() {
			return this.local.material();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#microfacetBasis()
		 */
		@Override
		public Basis3 microfacetBasis() {
			Basis3 localMicrofacetBasis = this.local.microfacetBasis();
			return Basis3.fromUV(
					model.apply(localMicrofacetBasis.u()),
					model.apply(localMicrofacetBasis.v()),
					localMicrofacetBasis.orientation()
			);
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#microfacetNormal()
		 */
		@Override
		public Vector3 microfacetNormal() {
			// TODO implement this directly for better performance.
			return this.microfacetBasis().w();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#normal()
		 */
		@Override
		public Vector3 normal() {
			// TODO implement this directly for better performance.
			return this.basis().w();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#tangent()
		 */
		@Override
		public Vector3 tangent() {
			return model.apply(this.local.tangent());
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#textureCoordinates()
		 */
		@Override
		public Point2 textureCoordinates() {
			return this.local.textureCoordinates();
		}

		/** The local <code>Intersection</code>. */
		private final Intersection local;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AffineTransformable3#transform(org.jmist.toolkit.AffineMatrix3)
	 */
	@Override
	public void transform(AffineMatrix3 T) {
		this.model.transform(T);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.LinearTransformable3#transform(org.jmist.toolkit.LinearMatrix3)
	 */
	@Override
	public void transform(LinearMatrix3 T) {
		this.model.transform(T);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotate(org.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void rotate(Vector3 axis, double angle) {
		this.model.rotate(axis, angle);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateX(double)
	 */
	@Override
	public void rotateX(double angle) {
		this.model.rotateX(angle);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateY(double)
	 */
	@Override
	public void rotateY(double angle) {
		this.model.rotateY(angle);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateZ(double)
	 */
	@Override
	public void rotateZ(double angle) {
		this.model.rotateZ(angle);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Translatable3#translate(org.jmist.toolkit.Vector3)
	 */
	@Override
	public void translate(Vector3 v) {
		this.model.translate(v);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Scalable#scale(double)
	 */
	@Override
	public void scale(double c) {
		this.model.scale(c);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Stretchable3#stretch(org.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void stretch(Vector3 axis, double c) {
		this.model.stretch(axis, c);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretch(double, double, double)
	 */
	@Override
	public void stretch(double cx, double cy, double cz) {
		this.model.stretch(cx, cy, cz);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchX(double)
	 */
	@Override
	public void stretchX(double cx) {
		this.model.stretchX(cx);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchY(double)
	 */
	@Override
	public void stretchY(double cy) {
		this.model.stretchY(cy);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchZ(double)
	 */
	@Override
	public void stretchZ(double cz) {
		this.model.stretchZ(cz);
	}

	/** The transformation to apply to this <code>Geometry</code>. */
	private final InvertibleAffineTransformation3 model = new InvertibleAffineTransformation3();

}
