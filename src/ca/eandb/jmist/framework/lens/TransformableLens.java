/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.AffineTransformable3;
import ca.eandb.jmist.framework.InvertibleAffineTransformation3;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> to which affine transformations may be applied.
 * @author Brad Kimmel
 */
public abstract class TransformableLens implements Lens, AffineTransformable3 {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.toolkit.Point2)
	 */
	public final Ray3 rayAt(Point2 p) {
		Ray3 viewRay = this.viewRayAt(p);
		return viewRay != null ? this.view.apply(viewRay) : null;
	}

	public final Point2 project(Point3 p) {
		return projectInViewSpace(view.applyInverse(p));
	}

	/**
	 * Gets a ray indicating from which point and direction the camera is
	 * sensitive to incoming light at the specified point on its image plane.
	 * This will correspond to the direction to cast a ray in order to shade
	 * the specified point on the image plane.  This method must return the
	 * ray in view coordinate space:  The center of the image plane
	 * (corresponding to the point (0.5, 0.5)) should map to a ray originating
	 * at the origin looking along the negative z-axis.  The positive y-axis
	 * should be the up direction and the positive x-axis should be to the
	 * right.
	 * @param p The point on the image plane in normalized device coordinates
	 * 		(must fall within {@code Box2.UNIT}).
	 * @return The ray to cast for ray shading.
	 * @see {@link Box2#UNIT}
	 */
	protected abstract Ray3 viewRayAt(Point2 p);

	/**
	 * Gets the normalized device coordinates (two dimensional) corresponding
	 * to the projection of the three dimensional point <code>p</code> onto the
	 * image plane.
	 * @param p The <code>Point3</code> to project onto the image plane.
	 * @return The <code>Point2</code> indicating the projected coordinates, or
	 * 		<code>null</code> if <code>p</code> does not project onto the image
	 * 		plane.
	 */
	protected abstract Point2 projectInViewSpace(Point3 p);

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
	 * The transformation to apply to the ray in the view coordinate system to
	 * obtain the ray in the world coordinate system.
	 */
	private final InvertibleAffineTransformation3 view = new InvertibleAffineTransformation3();

}
