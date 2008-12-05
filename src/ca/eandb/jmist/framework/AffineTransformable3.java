/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.*;


/**
 * A three dimensional object that can be transformed according to an affine transformation
 * (linear transformations plus translations).
 * @author brad
 *
 */
public interface AffineTransformable3 extends LinearTransformable3, ShapePreservingTransformable3, Stretchable3 {

	/**
	 * Transforms this object according to the specified affine transformation.
	 * @param T The matrix representing the affine transformation to apply.
	 */
	void transform(AffineMatrix3 T);

}
