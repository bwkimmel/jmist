/**
 *
 */
package org.jmist.packages.material;

import java.io.Serializable;

import org.jmist.framework.OpaqueMaterial;

/**
 * An <code>OpaqueMaterial</code> that neither reflects nor emits any light.
 * @author bkimmel
 */
public final class DummyMaterial extends OpaqueMaterial implements Serializable {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -6242548015786444882L;

}
