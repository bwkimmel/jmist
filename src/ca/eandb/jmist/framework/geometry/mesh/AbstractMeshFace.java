/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.util.AbstractList;
import java.util.List;

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Geometry3Util;
import ca.eandb.jmist.math.Sphere;


/**
 * @author Brad
 *
 */
public abstract class AbstractMeshFace extends AbstractList<MeshVertex> implements MeshFace {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.mesh.Mesh.Face#vertices()
	 */
	@Override
	public List<? extends MeshVertex> vertices() {
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		Point3Array coords = new Point3Array();
		for (MeshVertex vertex : vertices()) {
			coords.add(vertex.position());
		}
		return Geometry3Util.boundingSphere(coords);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
		for (MeshVertex vertex : vertices()) {
			builder.add(vertex.position());
		}
		return builder.getBoundingBox();
	}
	
	

}
