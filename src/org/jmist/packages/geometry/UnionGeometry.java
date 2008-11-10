/**
 *
 */
package org.jmist.packages.geometry;

import java.util.BitSet;


/**
 * A <code>Geometry</code> that is the union of all its component geometries.
 * @author bkimmel
 */
public final class UnionGeometry extends ConstructiveSolidGeometry {

	/* (non-Javadoc)
	 * @see org.jmist.packages.ConstructiveSolidGeometry#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return !args.isEmpty();
	}

}
