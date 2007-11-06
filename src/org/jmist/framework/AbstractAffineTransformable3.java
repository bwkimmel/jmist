/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.AffineMatrix3;
import org.jmist.toolkit.LinearMatrix3;
import org.jmist.toolkit.Vector3;

/**
 * An abstract base class for classes implementing
 * <code>AffineTransformable3</code>.
 * @author bkimmel
 */
public abstract class AbstractAffineTransformable3 implements
		AffineTransformable3 {

	/* (non-Javadoc)
	 * @see org.jmist.framework.AffineTransformable3#transform(org.jmist.toolkit.AffineMatrix3)
	 */
	@Override
	public void transform(AffineMatrix3 T) {
		if (this.matrix == null) {
			this.matrix = T;
		} else {
			this.matrix = T.times(this.matrix);
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.LinearTransformable3#transform(org.jmist.toolkit.LinearMatrix3)
	 */
	@Override
	public void transform(LinearMatrix3 T) {
		this.transform(new AffineMatrix3(T));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotate(org.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void rotate(Vector3 axis, double angle) {
		this.transform(LinearMatrix3.rotateMatrix(axis, angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateX(double)
	 */
	@Override
	public void rotateX(double angle) {
		this.transform(LinearMatrix3.rotateXMatrix(angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateY(double)
	 */
	@Override
	public void rotateY(double angle) {
		this.transform(LinearMatrix3.rotateYMatrix(angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Rotatable3#rotateZ(double)
	 */
	@Override
	public void rotateZ(double angle) {
		this.transform(LinearMatrix3.rotateZMatrix(angle));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Translatable3#translate(org.jmist.toolkit.Vector3)
	 */
	@Override
	public void translate(Vector3 v) {
		this.transform(AffineMatrix3.translateMatrix(v));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Scalable#scale(double)
	 */
	@Override
	public void scale(double c) {
		this.transform(LinearMatrix3.scaleMatrix(c));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Stretchable3#stretch(org.jmist.toolkit.Vector3, double)
	 */
	@Override
	public void stretch(Vector3 axis, double c) {
		this.transform(LinearMatrix3.stretchMatrix(axis, c));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretch(double, double, double)
	 */
	@Override
	public void stretch(double cx, double cy, double cz) {
		this.transform(LinearMatrix3.stretchMatrix(cx, cy, cz));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchX(double)
	 */
	@Override
	public void stretchX(double cx) {
		this.transform(LinearMatrix3.stretchXMatrix(cx));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchY(double)
	 */
	@Override
	public void stretchY(double cy) {
		this.transform(LinearMatrix3.stretchYMatrix(cy));
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.AxisStretchable3#stretchZ(double)
	 */
	@Override
	public void stretchZ(double cz) {
		this.transform(LinearMatrix3.stretchZMatrix(cz));
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
	protected void resetTransformation() {
		this.matrix = null;
	}

	/** The cumulative transformation matrix. */
	private AffineMatrix3 matrix = null;

}
