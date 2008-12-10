/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.AffineMatrix3;
import ca.eandb.jmist.toolkit.LinearMatrix3;
import ca.eandb.jmist.toolkit.Point3;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * A class for classes implementing <code>AffineTransformable3</code> that
 * require the inverse of the transformation matrix.
 * @author Brad Kimmel
 */
public class InvertibleAffineTransformation3 extends AffineTransformation3 {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#rotate(ca.eandb.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void rotate(Vector3 axis, double angle) {
		super.rotate(axis, angle);
		this.applyInverseTransformation(LinearMatrix3.rotateMatrix(axis, -angle));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#rotateX(double)
	 */
	@Override
	public void rotateX(double angle) {
		super.rotateX(angle);
		this.applyInverseTransformation(LinearMatrix3.rotateXMatrix(-angle));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#rotateY(double)
	 */
	@Override
	public void rotateY(double angle) {
		super.rotateY(angle);
		this.applyInverseTransformation(LinearMatrix3.rotateYMatrix(-angle));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#rotateZ(double)
	 */
	@Override
	public void rotateZ(double angle) {
		super.rotateZ(angle);
		this.applyInverseTransformation(LinearMatrix3.rotateZMatrix(-angle));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#scale(double)
	 */
	@Override
	public void scale(double c) {
		super.scale(c);
		this.applyInverseTransformation(LinearMatrix3.scaleMatrix(1.0 / c));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#stretch(double, double, double)
	 */
	@Override
	public void stretch(double cx, double cy, double cz) {
		super.stretch(cx, cy, cz);
		this.applyInverseTransformation(LinearMatrix3.stretchMatrix(1.0 / cx,
				1.0 / cy, 1.0 / cz));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#stretch(ca.eandb.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void stretch(Vector3 axis, double c) {
		super.stretch(axis, c);
		this.applyInverseTransformation(LinearMatrix3.stretchMatrix(axis, 1.0 / c));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#stretchX(double)
	 */
	@Override
	public void stretchX(double cx) {
		super.stretchX(cx);
		this.applyInverseTransformation(LinearMatrix3.stretchXMatrix(1.0 / cx));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#stretchY(double)
	 */
	@Override
	public void stretchY(double cy) {
		super.stretchY(cy);
		this.applyInverseTransformation(LinearMatrix3.stretchYMatrix(1.0 / cy));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#stretchZ(double)
	 */
	@Override
	public void stretchZ(double cz) {
		super.stretchZ(cz);
		this.applyInverseTransformation(LinearMatrix3.stretchZMatrix(1.0 / cz));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#transform(ca.eandb.jmist.toolkit.AffineMatrix3)
	 */
	@Override
	public void transform(AffineMatrix3 T) {
		super.transform(T);
		this.applyInverseTransformation(T.inverse());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#transform(ca.eandb.jmist.toolkit.LinearMatrix3)
	 */
	@Override
	public void transform(LinearMatrix3 T) {
		super.transform(T);
		this.applyInverseTransformation(T.inverse());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#translate(ca.eandb.jmist.toolkit.Vector3)
	 */
	@Override
	public void translate(Vector3 v) {
		super.translate(v);
		this.applyInverseTransformation(AffineMatrix3.translateMatrix(v
				.opposite()));
	}

	/**
	 * Applies the specified inverse transformation matrix to the current
	 * inverse transformation.
	 * @param Tinv The inverse of the <code>AffineMatrix3</code> that is being
	 * 		applied.
	 */
	private void applyInverseTransformation(AffineMatrix3 Tinv) {
		if (this.inverse == null) {
			this.inverse = Tinv;
		} else {
			this.inverse = this.inverse.times(Tinv);
		}
	}

	/**
	 * Applies the specified inverse transformation matrix to the current
	 * inverse transformation.
	 * @param Tinv The inverse of the <code>LinearMatrix3</code> that is being
	 * 		applied.
	 */
	private void applyInverseTransformation(LinearMatrix3 Tinv) {
		this.applyInverseTransformation(new AffineMatrix3(Tinv));
	}

	/**
	 * Gets the inverse transformation matrix.
	 * @return The <code>AffineMatrix3</code> representing the inverse of this
	 * 		transformation.
	 */
	protected AffineMatrix3 getInverseTransformationMatrix() {
		return this.inverse != null ? this.inverse : AffineMatrix3.IDENTITY;
	}

	/**
	 * Applies this transformation to the specified
	 * <code>InvertibleAffineTransformation3</code>.
	 * @param trans The <code>InvertibleAffineTransformation3</code> to apply
	 * 		this transformation to.
	 */
	public void apply(InvertibleAffineTransformation3 trans) {
		if (this.isTransformed()) {
			trans.applyTransformation(super.getTransformationMatrix());
			trans.applyInverseTransformation(this.inverse);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#apply(ca.eandb.jmist.framework.AffineTransformable3)
	 */
	@Override
	public void apply(AffineTransformable3 to) {
		if (to instanceof InvertibleAffineTransformation3) {
			this.apply((InvertibleAffineTransformation3) to);
		} else {
			super.apply(to);
		}
	}

	/**
	 * Applies the inverse of this transformation to the specified
	 * <code>InvertibleAffineTransformation3</code>.
	 * @param trans The <code>InvertibleAffineTransformation3</code> to apply
	 * 		the inverse of this transformation to.
	 */
	public void applyInverse(InvertibleAffineTransformation3 trans) {
		if (this.isTransformed()) {
			trans.applyTransformation(this.inverse);
			trans.applyInverseTransformation(super.getTransformationMatrix());
		}
	}

	/**
	 * Applies the inverse of this <code>AffineTransformation3</code> to
	 * another object that is affine transformable.
	 * @param to The <code>AffineTransformable3</code> object to apply the
	 * 		inverse of this transformation to.
	 */
	public void applyInverse(AffineTransformable3 to) {
		if (to instanceof InvertibleAffineTransformation3) {
			this.applyInverse((InvertibleAffineTransformation3) to);
		} else if (this.inverse != null) {
			to.transform(this.inverse);
		}
	}

	/**
	 * Applies the inverse of this <code>AffineTransformation3</code> to a
	 * <code>AffineMatrix3</code>.
	 * @param matrix The <code>AffineMatrix3</code> object to apply the inverse
	 * 		of this transformation to.
	 * @return The transformed <code>AffineMatrix3</code>.
	 */
	public AffineMatrix3 applyInverse(AffineMatrix3 matrix) {
		return this.inverse != null ? this.inverse.times(matrix) : matrix;
	}

	/**
	 * Applies the inverse of this <code>AffineTransformation3</code> to a
	 * <code>Point3</code>.
	 * @param p The <code>Point3</code> object to apply the inverse of this
	 * 		transformation to.
	 * @return The transformed <code>Point3</code>.
	 */
	public Point3 applyInverse(Point3 p) {
		return this.inverse != null ? this.inverse.times(p) : p;
	}

	/**
	 * Applies the inverse of this <code>AffineTransformation3</code> to a
	 * <code>Vector3</code>.
	 * @param v The <code>Vector3</code> object to apply the inverse of this
	 * 		transformation to.
	 * @return The transformed <code>Vector3</code>.
	 */
	public Vector3 applyInverse(Vector3 v) {
		return this.inverse != null ? this.inverse.times(v) : v;
	}

	/**
	 * Applies the inverse of this <code>AffineTransformation3</code> to a
	 * <code>Ray3</code>.
	 * @param ray The <code>Ray3</code> object to apply the inverse of this
	 * 		transformation to.
	 * @return The transformed <code>Ray3</code>.
	 */
	public Ray3 applyInverse(Ray3 ray) {
		return this.inverse != null ? ray.transform(this.inverse) : ray;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformation3#resetTransformation()
	 */
	@Override
	public void reset() {
		super.reset();
		this.inverse = null;
	}

	/** The inverse transformation matrix. */
	private AffineMatrix3 inverse = null;

}
