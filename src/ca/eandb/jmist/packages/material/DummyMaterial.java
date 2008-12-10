/**
 *
 */
package ca.eandb.jmist.packages.material;

import java.io.Serializable;

import ca.eandb.jmist.framework.OpaqueMaterial;

/**
 * An <code>OpaqueMaterial</code> that neither reflects nor emits any light.
 * @author Brad Kimmel
 */
public final class DummyMaterial extends OpaqueMaterial implements Serializable {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -6242548015786444882L;

}
