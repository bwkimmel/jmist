/**
 *
 */
package ca.eandb.jmist.packages.geometry;

import java.util.BitSet;


/**
 * A <code>Geometry</code> that is the union of all its component geometries.
 * @author Brad Kimmel
 */
public final class UnionGeometry extends ConstructiveSolidGeometry {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.ConstructiveSolidGeometry#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return !args.isEmpty();
	}

}
