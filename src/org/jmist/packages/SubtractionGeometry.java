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
public final class SubtractionGeometry extends ConstructiveSolidGeometry {

	/* (non-Javadoc)
	 * @see org.jmist.packages.CsgIntersectionRecorder#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return args.get(0) && args.nextSetBit(1) < 0;
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
