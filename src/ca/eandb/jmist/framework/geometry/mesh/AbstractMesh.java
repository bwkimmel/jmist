/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.util.AbstractList;
import java.util.List;

import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Geometry3Util;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;



/**
 * @author Brad
 *
 */
public abstract class AbstractMesh extends AbstractList<MeshFace> implements Mesh {

	/** Serialization version ID. */
	private static final long serialVersionUID = 8871695318088807972L;
	
	protected final List<Point3> vertices;
	
	protected final List<Vector3> normals;
	
	protected final List<Point2> textureCoordinates;
	
	protected AbstractMesh(List<Point3> vertices) {
		this(vertices, null, null);
	}
	
	protected AbstractMesh(List<Point3> vertices, List<Vector3> normals, List<Point2> textureCoordinates) {
		this.vertices = vertices;
		this.normals = normals;
		this.textureCoordinates = textureCoordinates;
	} 

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.mesh.Mesh#triangulate()
	 */
	@Override
	public Mesh triangulate() {
		return triangulate(3);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.mesh.Mesh#faces()
	 */
	@Override
	public List<? extends MeshFace> faces() {
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return Geometry3Util.boundingSphere(vertices);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		return Geometry3Util.boundingBox(vertices);
	}

}
