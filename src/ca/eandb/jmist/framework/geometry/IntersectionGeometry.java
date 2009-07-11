/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Sphere;

/**
 * A <code>SceneElement</code> that it the intersection of its component
 * geometries.
 * @author Brad Kimmel
 */
public final class IntersectionGeometry extends ConstructiveSolidGeometry {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -91390515433295892L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.ConstructiveSolidGeometry#isInside(int, java.util.BitSet)
	 */
	@Override
	protected boolean isInside(int nArgs, BitSet args) {
		return args.nextClearBit(0) >= nArgs;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {

		Collection<Box3> boxes = new ArrayList<Box3>();

		for (SceneElement geometry : this.children()) {
			boxes.add(geometry.boundingBox());
		}

		return Box3.intersection(boxes);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		Box3 boundingBox = this.boundingBox();
		return new Sphere(boundingBox.center(), boundingBox.diagonal() / 2.0);
	}

}
