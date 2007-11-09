/**
 *
 */
package org.jmist.packages;

import java.util.BitSet;

import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Sphere;

/**
 * @author bkimmel
 *
 */
public final class UnionGeometry extends ConstructiveSolidGeometry {

	/* (non-Javadoc)
	 * @see org.jmist.packages.ConstructiveSolidGeometry#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return !args.isEmpty();
	}

	@Override
	public Box3 boundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sphere boundingSphere() {
		// TODO Auto-generated method stub
		return null;
	}

}
