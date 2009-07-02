/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import java.util.BitSet;

import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Sphere;

/**
 * A <code>SceneElement</code> that is the first component geometry minus the union
 * of all subsequent component geometries.
 * @author Brad Kimmel
 */
public final class SubtractionGeometry extends ConstructiveSolidGeometry {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.CsgIntersectionRecorder#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return args.get(0) && args.nextSetBit(1) < 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		return this.children().isEmpty()
				? Box3.EMPTY
				: this.children().get(0).boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return this.children().isEmpty()
				? Sphere.EMPTY
				: this.children().get(0).boundingSphere();
	}

}
