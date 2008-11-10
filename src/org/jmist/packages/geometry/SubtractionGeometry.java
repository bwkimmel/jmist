/**
 *
 */
package org.jmist.packages.geometry;

import java.util.BitSet;

import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Sphere;

/**
 * A <code>Geometry</code> that is the first component geometry minus the union
 * of all subsequent component geometries.
 * @author bkimmel
 */
public final class SubtractionGeometry extends ConstructiveSolidGeometry {

	/* (non-Javadoc)
	 * @see org.jmist.packages.CsgIntersectionRecorder#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return args.get(0) && args.nextSetBit(1) < 0;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		return this.children().isEmpty()
				? Box3.EMPTY
				: this.children().get(0).boundingBox();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return this.children().isEmpty()
				? Sphere.EMPTY
				: this.children().get(0).boundingSphere();
	}

}
