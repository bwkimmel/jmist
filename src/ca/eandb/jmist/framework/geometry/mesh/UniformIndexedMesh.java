/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.util.List;

import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public abstract class UniformIndexedMesh extends AbstractMesh {

	/** Serialization version ID. */
	private static final long serialVersionUID = -7092983965565012640L;

	/**
	 * @param vertices
	 * @param normals
	 * @param textureCoordinates
	 */
	public UniformIndexedMesh(List<Point3> vertices, List<Vector3> normals,
			List<Point2> textureCoordinates) {
		super(vertices, normals, textureCoordinates);
	}

	/**
	 * @param vertices
	 */
	public UniformIndexedMesh(List<Point3> vertices) {
		super(vertices);
	}

	private final class Vertex implements MeshVertex {

		private final int index;
		
		public Vertex(int index) {
			this.index = index;
		}
		
		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.geometry.mesh.MeshVertex#position()
		 */
		@Override
		public Point3 position() {
			return vertices.get(index);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.geometry.mesh.MeshVertex#normal()
		 */
		@Override
		public Vector3 normal() {
			return normals != null ? normals.get(index) : null;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.geometry.mesh.MeshVertex#textureCoordinates()
		 */
		@Override
		public Point2 textureCoordinates() {
			return textureCoordinates != null ? textureCoordinates.get(index) : null;
		}
		
	}
	
	protected final MeshVertex vertex(int index) {
		return new Vertex(index);
	}
	
}
