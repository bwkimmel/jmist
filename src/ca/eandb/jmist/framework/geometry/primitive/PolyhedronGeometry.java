/**
 *
 */
package ca.eandb.jmist.framework.geometry.primitive;

import java.util.Arrays;

import ca.eandb.jmist.framework.Bounded3;
import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.geometry.AbstractGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.CategoricalRandom;
import ca.eandb.jmist.math.GeometryUtil;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.RandomUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * A polyhedron <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class PolyhedronGeometry extends AbstractGeometry {

	/**
	 * Creates a new <code>PolyhedronGeometry</code>.
	 * @param vertices An array of the vertices of the polyhedron.
	 * @param faces An array of faces.  Each face is an array of indices into
	 * 		the <code>vertices</code> array.  The front side of the face is the
	 * 		side from which the vertices appear in counter-clockwise order.
	 */
	public PolyhedronGeometry(Point3[] vertices, int[][] faces) {

		this.vertices = vertices;
		this.faces = new Face[faces.length];

		for (int i = 0; i < this.faces.length; i++) {
			this.faces[i] = new Face(faces[i]);
		}

		this.closed = true;

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		intersectFace(index, ray, recorder);
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

			Intersection x = super.newIntersection(ray, t, ray.direction().dot(n) < 0.0, faceIndex)
				.setLocation(p)
				.setPrimitiveIndex(faceIndex);

			recorder.record(x);

		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getBasis(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getBasis(GeometryIntersection x) {
		Face face = faces[x.getTag()];
		Vector3 n = face.plane.normal();
		return Basis3.fromW(n, Basis3.Orientation.RIGHT_HANDED);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		return faces[index].boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		return faces[index].boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	@Override
	public int getNumPrimitives() {
		return faces.length;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#isClosed()
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context) {
		Point3 p = faces[index].generateRandomSurfacePoint();
		Intersection x = super.newSurfacePoint(p, index).setPrimitiveIndex(index);
		x.prepareShadingContext(context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getSurfaceArea(int)
	 */
	@Override
	public double getSurfaceArea(int index) {
		return faces[index].getSurfaceArea();
	}

	/**
	 * A face of a polyhedron.
	 * @author Brad Kimmel
	 */
	private final class Face implements Bounded3 {

		/**
		 * Creates a new <code>Face</code>.
		 * @param indices The indices into {@link PolyhedronGeometry#vertices}
		 * 		of the vertices of the face.
		 */
		public Face(int[] indices) {
			this.indices = indices;
			this.plane = this.computePlane();
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

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
		 */
		public Box3 boundingBox() {
			BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
			for (int i = 0; i < indices.length; i++) {
				builder.add(vertices[indices[i]]);
			}
			return builder.getBoundingBox();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
		 */
		public Sphere boundingSphere() {
			Point3[] verts = new Point3[indices.length];
			for (int i = 0; i < indices.length; i++) {
				verts[i] = vertices[indices[i]];
			}
			return Sphere.smallestContaining(Arrays.asList(verts));
		}

		public double getSurfaceArea() {
			Vector3 n = computeFaceNormal();
			Vector3 v0 = vertices[indices[0]].vectorFromOrigin();
			Vector3 v1;
			Vector3 r = Vector3.ZERO;
			for (int i = 1; i < indices.length; i++) {
				v1 = vertices[indices[i]].vectorFromOrigin();
				r = r.plus(v0.cross(v1));
				v0 = v1;
			}
			return 0.5 * n.dot(r);
		}

		public Point3 generateRandomSurfacePoint() {
			decompose();
			int tri = 3 * rnd.next();
			Point3 a = vertices[decomp[tri]];
			Point3 b = vertices[decomp[tri + 1]];
			Point3 c = vertices[decomp[tri + 2]];
			return RandomUtil.uniformOnTriangle(a, b, c);
		}

		private void decompose() {
			if (decomp != null) {
				return;
			}
			decomp = new int[3 * (indices.length - 2)];
			double[] weight = new double[indices.length - 2];

			// FIXME This does not work for a general polygon.  It will work
			// for all convex polygons (or polygons where the lines between
			// the first vertex and each other vertex are contained inside the
			// polygon).
			for (int i = 0; i < indices.length - 2; i++) {
				decomp[3 * i] = indices[0];
				decomp[3 * i + 1] = indices[i + 1];
				decomp[3 * i + 2] = indices[i + 2];
				weight[i] = GeometryUtil.areaOfTriangle(
						vertices[decomp[3 * i]],
						vertices[decomp[3 * i + 1]],
						vertices[decomp[3 * i + 2]]);
			}
			rnd = new CategoricalRandom(weight);
		}

		private int[] decomp;

		private CategoricalRandom rnd;

		/** The <code>Plane3</code> in which this face lies. */
		public Plane3 plane;

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
