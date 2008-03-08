/**
 * 
 */
package org.jmist.packages.geometry.primitive;

import org.jmist.framework.AbstractGeometry;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Sphere;

/**
 * @author bkimmel
 *
 */
public final class PolygonalSurfaceOfRevolutionGeometry extends
		AbstractGeometry {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#isClosed()
	 */
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		// TODO Auto-generated method stub
		return null;
	}

}
