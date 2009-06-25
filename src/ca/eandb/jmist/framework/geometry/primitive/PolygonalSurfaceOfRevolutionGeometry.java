/**
 *
 */
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.geometry.AbstractGeometry;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author Brad Kimmel
 *
 */
public final class PolygonalSurfaceOfRevolutionGeometry extends
		AbstractGeometry {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#isClosed()
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	public Box3 getBoundingBox(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	public Sphere getBoundingSphere(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	public int getNumPrimitives() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		// TODO Auto-generated method stub

	}

	@Override
	public SurfacePoint generateRandomSurfacePoint() {
		// TODO Auto-generated method stub
		return null;
	}

}
