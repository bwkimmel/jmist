/**
 * 
 */
package ca.eandb.jmist.packages.geometry.primitive;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.SingleMaterialGeometry;
import ca.eandb.jmist.toolkit.Box3;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Sphere;

/**
 * @author Brad Kimmel
 *
 */
public final class ConeGeometry extends SingleMaterialGeometry {

	/**
	 * @param material
	 */
	public ConeGeometry(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersect(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#isClosed()
	 */
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		// TODO Auto-generated method stub
		return null;
	}

}
