/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.AffineTransformable3;
import ca.eandb.jmist.framework.InvertibleAffineTransformation3;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> to which affine transformations may be applied.
 * @author Brad Kimmel
 */
public final class TransformableLens extends AbstractLens implements
		AffineTransformable3 {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 6810985479571317184L;

	/**
	 * The <code>Lens</code> to be transformed.
	 */
	private final Lens inner;

	/**
	 * The transformation to apply to the ray in the view coordinate system to
	 * obtain the ray in the world coordinate system.
	 */
	private final InvertibleAffineTransformation3 view = new InvertibleAffineTransformation3();

	/**
	 * Creates a new <code>TransformableLens</code>.
	 * @param inner The <code>Lens</code> to transform.
	 */
	public TransformableLens(Lens inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.toolkit.Point2)
	 */
	public final Ray3 rayAt(Point2 p) {
		Ray3 ray = inner.rayAt(p);
		return ray != null ? this.view.apply(ray) : null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Point3)
	 */
	public final Projection project(Point3 p) {
		final Projection viewProj = inner.project(view.applyInverse(p));
		return viewProj != null ? new TransformedProjection(viewProj) : null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.lens.AbstractLens#project(ca.eandb.jmist.math.Vector3)
	 */
	public final Projection project(Vector3 v) {
		final Projection viewProj = inner.project(view.applyInverse(v));
		return viewProj != null ? new TransformedProjection(viewProj) : null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable3#rotate(ca.eandb.jmist.toolkit.Vector3, double)
	 */
	public void rotate(Vector3 axis, double angle) {
		view.rotate(axis, angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable3#rotateX(double)
	 */
	public void rotateX(double angle) {
		view.rotateX(angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable3#rotateY(double)
	 */
	public void rotateY(double angle) {
		view.rotateY(angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable3#rotateZ(double)
	 */
	public void rotateZ(double angle) {
		view.rotateZ(angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scalable#scale(double)
	 */
	public void scale(double c) {
		view.scale(c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable3#stretch(double, double, double)
	 */
	public void stretch(double cx, double cy, double cz) {
		view.stretch(cx, cy, cz);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Stretchable3#stretch(ca.eandb.jmist.toolkit.Vector3, double)
	 */
	public void stretch(Vector3 axis, double c) {
		view.stretch(axis, c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable3#stretchX(double)
	 */
	public void stretchX(double cx) {
		view.stretchX(cx);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable3#stretchY(double)
	 */
	public void stretchY(double cy) {
		view.stretchY(cy);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable3#stretchZ(double)
	 */
	public void stretchZ(double cz) {
		view.stretchZ(cz);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformable3#transform(ca.eandb.jmist.toolkit.AffineMatrix3)
	 */
	public void transform(AffineMatrix3 T) {
		view.transform(T);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.LinearTransformable3#transform(ca.eandb.jmist.toolkit.LinearMatrix3)
	 */
	public void transform(LinearMatrix3 T) {
		view.transform(T);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Translatable3#translate(ca.eandb.jmist.toolkit.Vector3)
	 */
	public void translate(Vector3 v) {
		view.translate(v);
	}

	/**
	 * A <code>Projection</code> decorator that transforms its results to
	 * world coordinates.
	 * @author Brad Kimmel
	 */
	private final class TransformedProjection implements Projection {

		/** The <code>Projection</code> being decorated. */
		private final Projection inner;

		/**
		 * Creates a new <code>TransformedProjection</code>.
		 * @param inner The <code>Projection</code> to be transformed.
		 */
		private TransformedProjection(Projection inner) {
			this.inner = inner;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Lens.Projection#pointOnImagePlane()
		 */
		public Point2 pointOnImagePlane() {
			return inner.pointOnImagePlane();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Lens.Projection#pointOnLens()
		 */
		public Point3 pointOnLens() {
			return view.apply(inner.pointOnLens());
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Lens.Projection#importance()
		 */
		public double importance() {
			return inner.importance();
		}

	}

}
