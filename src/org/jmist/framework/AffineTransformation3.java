/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.AffineMatrix3;
import org.jmist.toolkit.LinearMatrix3;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;

/**
 * An abstract base class for classes implementing
 * <code>AffineTransformable3</code>.
 * @author bkimmel
 */
public class AffineTransformation3 implements AffineTransformable3 {

	/* (non-Javadoc)
	 * @see org.jmist.framework.AffineTransformable3#transform(org.jmist.toolkit.AffineMatrix3)
	 */
	@Override
	public void transform(AffineMatrix3 T) {
		this.applyTransformation(T);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.LinearTransformable3#transform(org.jmist.toolkit.LinearMatrix3)
	 */
	@Override
	public void transform(LinearMatrix3 T) {
		this.applyTransformation(T);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotate(org.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void rotate(Vector3 axis, double angle) {
		this.applyTransformation(LinearMatrix3.rotateMatrix(axis, angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateX(double)
	 */
	@Override
	public void rotateX(double angle) {
		this.applyTransformation(LinearMatrix3.rotateXMatrix(angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateY(double)
	 */
	@Override
	public void rotateY(double angle) {
		this.applyTransformation(LinearMatrix3.rotateYMatrix(angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateZ(double)
	 */
	@Override
	public void rotateZ(double angle) {
		this.applyTransformation(LinearMatrix3.rotateZMatrix(angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Translatable3#translate(org.jmist.toolkit.Vector3)
	 */
	@Override
	public void translate(Vector3 v) {
		this.applyTransformation(AffineMatrix3.translateMatrix(v));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Scalable#scale(double)
	 */
	@Override
	public void scale(double c) {
		this.applyTransformation(LinearMatrix3.scaleMatrix(c));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Stretchable3#stretch(org.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void stretch(Vector3 axis, double c) {
		this.applyTransformation(LinearMatrix3.stretchMatrix(axis, c));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretch(double, double, double)
	 */
	@Override
	public void stretch(double cx, double cy, double cz) {
		this.applyTransformation(LinearMatrix3.stretchMatrix(cx, cy, cz));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchX(double)
	 */
	@Override
	public void stretchX(double cx) {
		this.applyTransformation(LinearMatrix3.stretchXMatrix(cx));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchY(double)
	 */
	@Override
	public void stretchY(double cy) {
		this.applyTransformation(LinearMatrix3.stretchYMatrix(cy));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchZ(double)
	 */
	@Override
	public void stretchZ(double cz) {
		this.applyTransformation(LinearMatrix3.stretchZMatrix(cz));
	}

	/**
	 * Applies this <code>AffineTransformation3</code> to another object that
	 * is affine transformable.
	 * @param to The <code>AffineTransformable3</code> object to apply this
	 * 		transformation to.
	 */
	public void apply(AffineTransformable3 to) {
		if (this.matrix != null) {
			to.transform(this.matrix);
		}
	}

	/**
	 * Applies this <code>AffineTransformation3</code> to a
	 * <code>AffineMatrix3</code>.
	 * @param matrix The <code>AffineMatrix3</code> object to apply this
	 * 		transformation to.
	 * @return The transformed <code>AffineMatrix3</code>.
	 */
	public AffineMatrix3 apply(AffineMatrix3 matrix) {
		return this.matrix != null ? this.matrix.times(matrix) : matrix;
	}

	/**
	 * Applies this <code>AffineTransformation3</code> to a
	 * <code>Point3</code>.
	 * @param p The <code>Point3</code> object to apply this transformation to.
	 * @return The transformed <code>Point3</code>.
	 */
	public Point3 apply(Point3 p) {
		return this.matrix != null ? this.matrix.times(p) : p;
	}

	/**
	 * Applies this <code>AffineTransformation3</code> to a
	 * <code>Vector3</code>.
	 * @param v The <code>Vector3</code> object to apply this transformation
	 * 		to.
	 * @return The transformed <code>Vector3</code>.
	 */
	public Vector3 apply(Vector3 v) {
		return this.matrix != null ? this.matrix.times(v) : v;
	}

	/**
	 * Applies this <code>AffineTransformation3</code> to a <code>Ray3</code>.
	 * @param ray The <code>Ray3</code> object to apply this transformation to.
	 * @return The transformed <code>Ray3</code>.
	 */
	public Ray3 apply(Ray3 ray) {
		return this.matrix != null ? ray.transform(this.matrix) : ray;
	}

	/**
	 * Applies the transformation represented by the specified
	 * <code>AffineMatrix3</code> to this <code>AffineTransformation3</code>.
	 * @param T The <code>AffineMatrix3</code> representing the transformation
	 * 		to be applied.
	 */
	protected final void applyTransformation(AffineMatrix3 T) {
		if (this.matrix == null) {
			this.matrix = T;
		} else {
			this.matrix = T.times(this.matrix);
		}
	}

	/**
	 * Applies the transformation represented by the specified
	 * <code>LinearMatrix3</code> to this <code>AffineTransformation3</code>.
	 * @param T The <code>LinearMatrix3</code> representing the transformation
	 * 		to be applied.
	 */
	protected final void applyTransformation(LinearMatrix3 T) {
		this.applyTransformation(new AffineMatrix3(T));
	}

	/**
	 * Gets the cumulative transformation matrix.
	 * @return The cumulative transformation matrix.
	 */
	protected final AffineMatrix3 getTransformationMatrix() {
		return matrix != null ? matrix : AffineMatrix3.IDENTITY;
	}

	/**
	 * Gets a value indicating whether a transformation has been applied.
	 * @return A value indicating whether a transformation has been applied.
	 */
	protected final boolean isTransformed() {
		return matrix != null;
	}

	/**
	 * Resets the transformation to the identity transformation.
	 */
	public void reset() {
		this.matrix = null;
	}

	/** The cumulative transformation matrix. */
	private AffineMatrix3 matrix = null;

}
