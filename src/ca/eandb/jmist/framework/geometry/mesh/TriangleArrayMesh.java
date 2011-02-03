/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;


/**
 * @author Brad
 *
 */
public final class TriangleArrayMesh extends UniformIndexedMesh {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 2777358454904089410L;
		
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
			if (index < 0 || index >= 3) {
				throw new IndexOutOfBoundsException();
			}
			return vertex(offset + index);
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return 3;
		}
		
	}
	
	public TriangleArrayMesh() {
		super(new Point3Array());
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
	public Face get(int index) {
		return new Face(index * 3);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return vertices.size() / 3;
	}

}
