/**
 *
 */
package ca.eandb.jmist.packages.geometry.primitive;

import java.util.Arrays;

import ca.eandb.jmist.framework.AbstractGeometry;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.toolkit.Basis3;
import ca.eandb.jmist.toolkit.BoundingBoxBuilder3;
import ca.eandb.jmist.toolkit.Box3;
import ca.eandb.jmist.toolkit.Plane3;
import ca.eandb.jmist.toolkit.Point2;
import ca.eandb.jmist.toolkit.Point3;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Sphere;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * A polyhedron <code>Geometry</code>.
 * @author Brad Kimmel
 */
public final class PolyhedronGeometry extends AbstractGeometry {

	/**
	 * Creates a new <code>PolyhedronGeometry</code>.
	 * @param vertices An array of the vertices of the polyhedron.
	 * @param faces An array of faces.  Each face is an array of indices into
	 * 		the <code>vertices</code> array.  The front side of the face is the
	 * 		side from which the vertices appear in counter-clockwise order.
	 * @param material The material to apply to the polyhedron.
	 */
	public PolyhedronGeometry(Point3[] vertices, int[][] faces, Material material) {

		this.vertices = vertices;
		this.faces = new Face[faces.length];

		for (int i = 0; i < this.faces.length; i++) {
			this.faces[i] = new Face(faces[i], material);
		}

		this.closed = true;

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersect(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		for (int i = 0; i < this.faces.length; i++) {
			this.intersectFace(i, ray, recorder);
		}
	}

	/**
	 * Intersects a <code>Ray3</code> with a face.
	 * @param faceIndex The index of the <code>Face</code> to intersect.
	 * @param ray The <code>Ray3</code> to intersect with the indicated
	 * 		<code>Face</code>.
	 * @param recorder The <code>IntersectionRecorder</code> to record
	 * 		intersections to.
	 */
	private void intersectFace(int faceIndex, Ray3 ray, IntersectionRecorder recorder) {

		Face			face = this.faces[faceIndex];
		double			t = face.plane.intersect(ray);

		if (recorder.interval().contains(t)) {

			Point3		p = ray.pointAt(t);
			Vector3		n = face.plane.normal();
			Vector3		u, v;

			for (int i = 0; i < face.indices.length; i++)
			{
				Point3	a = this.vertices[face.indices[i]];
				Point3	b = this.vertices[face.indices[(i + 1) % face.indices.length]];
				Vector3	ab = a.vectorTo(b);

				u = n.cross(ab);
				v = p.vectorFrom(a);

				if (u.dot(v) < 0.0) {
					return;
				}
			}

			Intersection x = super.newIntersection(ray, t, ray.direction().dot(n) < 0.0, faceIndex, face.material)
				.setLocation(p)
				.setBasis(Basis3.fromW(n, Basis3.Orientation.RIGHT_HANDED));

			recorder.record(x);

		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#isClosed()
	 */
	public boolean isClosed() {
		return this.closed;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {

		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();

		for (Point3 vertex : this.vertices) {
			builder.add(vertex);
		}

		return builder.getBoundingBox();

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return Sphere.smallestContaining(Arrays.asList(this.vertices));
	}

	/**
	 * A face of a polyhedron.
	 * @author Brad Kimmel
	 */
	private final class Face {

		/**
		 * Creates a new <code>Face</code>.
		 * @param indices The indices into {@link PolyhedronGeometry#vertices}
		 * 		of the vertices of the face.
		 * @param material The <code>Material</code> to apply to the face.
		 */
		public Face(int[] indices, Material material) {
			this.indices = indices;
			this.plane = this.computePlane();
			this.material = material;
		}

		/**
		 * Computes the <code>Plane3</code> in which this <code>Face</code>
		 * lies.
		 * @return The <code>Plane3</code> in which this <code>Face</code>
		 * 		lies.
		 */
		public Plane3 computePlane() {
			return new Plane3(vertices[indices[0]], this.computeFaceNormal());
		}

		/**
		 * Computes the <code>Vector3</code> perpendicular to this
		 * <code>Face</code>.
		 * @return The <code>Vector3</code> perpendicular to this
		 * 		<code>Face</code>.
		 */
		public Vector3 computeFaceNormal() {

			Vector3	u = vertices[indices[0]].vectorTo(vertices[indices[1]]);
			Vector3	v = vertices[indices[0]].vectorTo(vertices[indices[indices.length - 1]]);

			return u.cross(v).unit();

		}

		/** The <code>Plane3</code> in which this face lies. */
		public Plane3 plane;

		/** The <code>Material</code> applied to this face. */
		public Material material;

		/**
		 * The indices into {@link PolyhedronGeometry#vertices} of the vertices
		 * of this face.
		 */
		public int[] indices;

		/**
		 * The indices into {@link PolyhedronGeometry#textureVertices} of the
		 * texture vertices of this face.
		 */
		public int[] textureIndices;

	}

	/** The array of the vertices of this polyhedron. */
	private Point3[] vertices;

	/**
	 * The array of vertex normals corresponding to the vertices in
	 * {@link #vertices}.
	 */
	private Vector3[] normals;

	/** An array of texture coordinates. */
	private Point2[] textureVertices;

	/** An array of the <code>Face</code>s of this polyhedron. */
	private Face[] faces;

	/** A value indicating whether this polyhedron is closed. */
	private boolean closed;

}
