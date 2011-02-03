/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.util.AbstractList;
import java.util.List;

import ca.eandb.jmist.math.Point3;

/**
 * @author Brad
 *
 */
public final class PolygonMesh extends UniformIndexedMesh implements MeshFace {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 4891169967984052365L;

	private final class VertexList extends AbstractList<MeshVertex> {

		/* (non-Javadoc)
		 * @see java.util.AbstractList#get(int)
		 */
		@Override
		public MeshVertex get(int index) {
			return vertex(index);
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return vertices.size();
		}
		
	}

	public PolygonMesh() {
		super(new Point3Array());
	}
	
	public void addVertex(Point3 vertex) {
		vertices.add(vertex);
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.mesh.Mesh#triangulate(int)
	 */
	@Override
	public Mesh triangulate(int maximumVerticesPerFace) {
		return (vertices.size() > maximumVerticesPerFace) ? new TriangleFanMesh(vertices) : this;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public MeshFace get(int index) {
		if (index != 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.mesh.MeshFace#vertices()
	 */
	@Override
	public List<? extends MeshVertex> vertices() {
		return new VertexList();
	}

}
