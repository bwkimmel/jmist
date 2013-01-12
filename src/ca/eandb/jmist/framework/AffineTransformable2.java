/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.AffineMatrix2;


/**
 * A two dimensional object that can be transformed according to an affine transformation
 * (linear transformations plus translations).
 * @author Brad Kimmel
 */
public interface AffineTransformable2 extends LinearTransformable2, ShapePreservingTransformable2, Stretchable2 {

	/**
	 * Transforms this object according to the specified affine transformation.
	 * @param T The matrix representing the affine transformation to apply.
	 */
	void transform(AffineMatrix2 T);

}
