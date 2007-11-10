/**
 *
 */
package org.jmist.packages;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;

import org.jmist.framework.Geometry;
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

		Collection<Box3> boxes = new ArrayList<Box3>();

		for (Geometry geometry : this.children()) {
			boxes.add(geometry.boundingBox());
		}

		return Box3.intersection(boxes);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		Box3 boundingBox = this.boundingBox();
		return new Sphere(boundingBox.center(), boundingBox.diagonal() / 2.0);
	}

}
