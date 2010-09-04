/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.LinearMatrix2;

/**
 * Represents a two dimensional object to which linear transformations can be applied.
 * @author Brad Kimmel
 */
public interface LinearTransformable2 extends Rotatable2 {

	/**
	 * Transforms this object according to the specified linear transformation.
	 * @param T The matrix representing the linear transformation to apply.
	 */
	void transform(LinearMatrix2 T);

}
