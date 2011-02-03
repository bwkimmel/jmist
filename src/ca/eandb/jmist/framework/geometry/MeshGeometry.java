/**
 * 
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.mesh.Mesh;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author Brad
 *
 */
public final class MeshGeometry extends AbstractGeometry {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 8458104400650788391L;
	
	private final Mesh mesh;

	/**
	 * @param mesh
	 */
	public MeshGeometry(Mesh mesh) {
		this.mesh = mesh.triangulate(4);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	@Override
	public int getNumPrimitives() {
		return mesh.faces().size();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		return mesh.faces().get(index).boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		return mesh.faces().get(index).boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return mesh.boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		return mesh.boundingBox();
	}

}
