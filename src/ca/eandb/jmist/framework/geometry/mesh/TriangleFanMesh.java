/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.util.List;

import ca.eandb.jmist.math.Point3;

/**
 * @author Brad Kimmel
 */
public final class TriangleFanMesh extends UniformIndexedMesh {
		
	/** Serialization version ID. */
	private static final long serialVersionUID = -8367934061018680314L;

	private final class Face extends AbstractMeshFace {
		
		private final int offset;
		
		public Face(int offset) {
			this.offset = offset;
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractList#get(int)
		 */
		@Override
		public MeshVertex get(int index) {
			switch (index) {
			case 0: return vertex(offset);
			case 1: return vertex(offset + (((offset & 0x1) != 0) ? 2 : 1));
			case 2: return vertex(offset + (((offset & 0x1) != 0) ? 1 : 2));
			default:
				throw new IllegalArgumentException("index out of bounds");
			}
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return 3;
		}
		
	}

	public TriangleFanMesh() {
		super(new Point3Array());
	}
	
	public TriangleFanMesh(List<Point3> vertices) {
		super(vertices);
	}
	
	public void addVertex(Point3 vertex) {
		vertices.add(vertex);
	}
	
	public void addVertices(List<Point3> verts) {
		for (Point3 vertex : verts) {
			vertices.add(vertex);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.mesh.Mesh#triangulate(int)
	 */
	@Override
	public Mesh triangulate(int maximumVerticesPerFace) {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public MeshFace get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		return new Face(index + 1);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return vertices.size() - 2;
	}

}
