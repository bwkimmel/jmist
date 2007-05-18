/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;


/**
 * A three dimensional object that can be transformed according to an affine transformation
 * (linear transformations plus translations).
 * @author brad
 *
 */
public interface IAffineTransformable3 extends ILinearTransformable3, IShapePreservingTransformable3, IStretchable3 {

	/**
	 * Transforms this object according to the specified affine transformation.
	 * @param T The matrix representing the affine transformation to apply.
	 */
	public void transform(AffineMatrix3 T);

}
