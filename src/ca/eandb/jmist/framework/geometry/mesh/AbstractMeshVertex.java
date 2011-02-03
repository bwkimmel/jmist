/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public abstract class AbstractMeshVertex implements MeshVertex {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.mesh.Mesh.Vertex#normal()
	 */
	@Override
	public Vector3 normal() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.mesh.Mesh.Vertex#textureCoordinates()
	 */
	@Override
	public Point2 textureCoordinates() {
		return null;
	}

}
