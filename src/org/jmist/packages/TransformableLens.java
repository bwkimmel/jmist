/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.AbstractAffineTransformable3;
import org.jmist.framework.Lens;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Ray3;

/**
 * A <code>Lens</code> to which affine transformations may be applied.
 * @author bkimmel
 */
public abstract class TransformableLens extends AbstractAffineTransformable3
		implements Lens {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Lens#rayAt(org.jmist.toolkit.Point2)
	 */
	@Override
	public final Ray3 rayAt(Point2 p) {

		Ray3 ray = this.viewRayAt(p);

		if (this.isTransformed()) {
			ray = ray.transform(this.getTransformationMatrix());
		}

		return ray;

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

}
