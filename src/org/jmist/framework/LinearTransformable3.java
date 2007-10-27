/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * Represents a three dimensional object to which linear transformations can be applied.
 * @author brad
 */
public interface LinearTransformable3 extends Rotatable3 {

	/**
	 * Transforms this object according to the specified linear transformation.
	 * @param T The matrix representing the linear transformation to apply.
	 */
	void transform(LinearMatrix3 T);

}
