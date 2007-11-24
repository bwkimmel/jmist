/**
 * 
 */
package org.jmist.packages.geometry.primitive;

import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.Material;
import org.jmist.framework.SingleMaterialGeometry;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;

/**
 * @author bkimmel
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
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#isClosed()
	 */
	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
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
