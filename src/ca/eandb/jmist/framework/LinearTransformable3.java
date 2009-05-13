/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.toolkit.*;

/**
 * Represents a three dimensional object to which linear transformations can be applied.
 * @author Brad Kimmel
 */
public interface LinearTransformable3 extends Rotatable3 {

	/**
	 * Transforms this object according to the specified linear transformation.
	 * @param T The matrix representing the linear transformation to apply.
	 */
	void transform(LinearMatrix3 T);

}
