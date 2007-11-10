/**
 *
 */
package org.jmist.packages;

import java.util.BitSet;

import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Sphere;

/**
 * A <code>Geometry</code> that it the intersection of its component
 * geometries.
 * @author bkimmel
 */
public final class IntersectionGeometry extends ConstructiveSolidGeometry {

	/* (non-Javadoc)
	 * @see org.jmist.packages.ConstructiveSolidGeometry#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return args.nextClearBit(0) >= nArgs;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		// TODO Auto-generated method stub
		return null;
	}

}
