/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * Represents a three dimensional object to which linear transformations can be applied.
 * @author brad
 */
public interface ILinearTransformable3 extends IRotatable3 {

	/**
	 * Transforms this object according to the specified linear transformation.
	 * @param T The matrix representing the linear transformation to apply.
	 */
	public void transform(LinearMatrix3 T);

}
