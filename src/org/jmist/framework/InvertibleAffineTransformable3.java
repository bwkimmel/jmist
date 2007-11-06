/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.AffineMatrix3;
import org.jmist.toolkit.LinearMatrix3;
import org.jmist.toolkit.Vector3;

/**
 * An abstract base class for classes implementing
 * <code>AffineTransformable3</code> that require the inverse of the
 * transformation matrix.
 * @author bkimmel
 */
public abstract class InvertibleAffineTransformable3 extends
		AbstractAffineTransformable3 {

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#rotate(org.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void rotate(Vector3 axis, double angle) {
		super.rotate(axis, angle);
		this.applyInverseTransformation(LinearMatrix3.rotateMatrix(axis, -angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#rotateX(double)
	 */
	@Override
	public void rotateX(double angle) {
		super.rotateX(angle);
		this.applyInverseTransformation(LinearMatrix3.rotateXMatrix(-angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#rotateY(double)
	 */
	@Override
	public void rotateY(double angle) {
		super.rotateY(angle);
		this.applyInverseTransformation(LinearMatrix3.rotateYMatrix(-angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#rotateZ(double)
	 */
	@Override
	public void rotateZ(double angle) {
		super.rotateZ(angle);
		this.applyInverseTransformation(LinearMatrix3.rotateZMatrix(-angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#scale(double)
	 */
	@Override
	public void scale(double c) {
		super.scale(c);
		this.applyInverseTransformation(LinearMatrix3.scaleMatrix(1.0 / c));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#stretch(double, double, double)
	 */
	@Override
	public void stretch(double cx, double cy, double cz) {
		super.stretch(cx, cy, cz);
		this.applyInverseTransformation(LinearMatrix3.stretchMatrix(1.0 / cx,
				1.0 / cy, 1.0 / cz));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#stretch(org.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void stretch(Vector3 axis, double c) {
		super.stretch(axis, c);
		this.applyInverseTransformation(LinearMatrix3.stretchMatrix(axis, 1.0 / c));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#stretchX(double)
	 */
	@Override
	public void stretchX(double cx) {
		super.stretchX(cx);
		this.applyInverseTransformation(LinearMatrix3.stretchXMatrix(1.0 / cx));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#stretchY(double)
	 */
	@Override
	public void stretchY(double cy) {
		super.stretchY(cy);
		this.applyInverseTransformation(LinearMatrix3.stretchYMatrix(1.0 / cy));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#stretchZ(double)
	 */
	@Override
	public void stretchZ(double cz) {
		super.stretchZ(cz);
		this.applyInverseTransformation(LinearMatrix3.stretchZMatrix(1.0 / cz));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#transform(org.jmist.toolkit.AffineMatrix3)
	 */
	@Override
	public void transform(AffineMatrix3 T) {
		super.transform(T);
		this.applyInverseTransformation(T.inverse());
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#transform(org.jmist.toolkit.LinearMatrix3)
	 */
	@Override
	public void transform(LinearMatrix3 T) {
		super.transform(T);
		this.applyInverseTransformation(T.inverse());
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#translate(org.jmist.toolkit.Vector3)
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
	 * Gets the inverse of the transformation matrix.
	 * @return The inverse of the cumulative transformation.
	 */
	protected final AffineMatrix3 getInverseTransformationMatrix() {
		return inverse != null ? inverse : AffineMatrix3.IDENTITY;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AbstractAffineTransformable3#resetTransformation()
	 */
	@Override
	protected void resetTransformation() {
		super.resetTransformation();
		this.inverse = null;
	}

	/** The inverse transformation matrix. */
	private AffineMatrix3 inverse = null;

}
