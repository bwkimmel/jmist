/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import java.util.BitSet;


/**
 * A <code>SceneElement</code> that is the union of all its component geometries.
 * @author Brad Kimmel
 */
public final class UnionGeometry extends ConstructiveSolidGeometry {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 6087149571439018520L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.ConstructiveSolidGeometry#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return !args.isEmpty();
	}

}
