/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.geometry.primitive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.eandb.jmist.framework.Bounded3;
import ca.eandb.jmist.framework.BoundingBoxBuilder2;
import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.geometry.AbstractGeometry;
import ca.eandb.jmist.framework.random.CategoricalRandom;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.math.AffineMatrix2;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.GeometryUtil;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.IntegerArray;

/**
 * A polyhedron <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class PolyhedronGeometry extends AbstractGeometry {

	/** Serialization version ID. */
	private static final long serialVersionUID = 262374288661771750L;

	/**
	 * Creates a new <code>PolyhedronGeometry</code>.
	 * @param vertices An array of the vertices of the polyhedron.
	 * @param faces An array of faces.  Each face is an array of indices into
	 * 		the <code>vertices</code> array.  The front side of the face is the
	 * 		side from which the vertices appear in counter-clockwise order.
	 */
	public PolyhedronGeometry(Point3[] vertices, int[][] faces) {
		this.vertices = new ArrayList<Point3>(vertices.length);
		this.vertices.addAll(Arrays.asList(vertices));

		this.texCoords = new ArrayList<Point2>();
		this.normals = new ArrayList<Vector3>();

		for (int i = 0; i < faces.length; i++) {
			this.faces.add(new Face(faces[i], null, null));
		}
	}

	public PolyhedronGeometry(List<Point3> vertices, List<Point2> texCoords, List<Vector3> normals) {
		this.vertices = vertices;
		this.texCoords = texCoords;
		this.normals = normals;
	}

	public PolyhedronGeometry() {
		this(new ArrayList<Point3>(), new ArrayList<Point2>(), new ArrayList<Vector3>());
	}

	public PolyhedronGeometry addVertex(Point3 v) {
		vertices.add(v);
		return this;
	}

	public PolyhedronGeometry addNormal(Vector3 vn) {
		normals.add(vn);
		return this;
	}

	public PolyhedronGeometry addTexCoord(Point2 vt) {
		texCoords.add(vt);
		return this;
	}

	public PolyhedronGeometry addFace(int[] vi) {
		return addFace(vi, null, null);
	}

	public PolyhedronGeometry addFace(int[] vi, int[] vti) {
		return addFace(vi, vti, null);
	}

	public PolyhedronGeometry addFace(int[] vi, int[] vti, int[] vni) {
		faces.add(new Face(vi, vti, vni));
		return this;
	}

	public PolyhedronGeometry setMaximumVertexNormalAngle(double angle) {
		this.minVertexNormalDotProduct = Math.cos(angle);
		return this;
	}

	private transient int triangleLookupGridSize;
	private transient int maximumTrianglesPerFace;
	private transient Map<Integer, IntegerArray> triangleLookup;
	private transient Box2 texBoundingBox = null;

	private synchronized void prepareTriangleLookup() {
		int numTriangles = 0;

		maximumTrianglesPerFace = 0;
		for (Face face : faces) {
			if (face.texIndices != null) {
				face.decompose();

				int ntri = face.decomp.length / 3;
				if (ntri > maximumTrianglesPerFace) {
					maximumTrianglesPerFace = ntri;
				}
				numTriangles += ntri;
			}
		}

		triangleLookupGridSize = Math.max(1, 2 * (int) Math.sqrt(numTriangles));
		triangleLookup = new HashMap<Integer, IntegerArray>();

		BoundingBoxBuilder2 builder = new BoundingBoxBuilder2();

		for (Point2 p : texCoords) {
			builder.add(p);
		}
		texBoundingBox = builder.getBoundingBox();

		AffineMatrix2 T = texBoundingBox.toMatrix().inverse();

		for (int fi = 0, nf = faces.size(); fi < nf; fi++) {
			Face f = faces.get(fi);
			if (f.texIndices != null) {
				for (int tri = 0; tri < f.decomp.length; tri += 3) {
					Point2 a = T.times(texCoords.get(f.texIndices[f.decomp[tri]]));
					Point2 b = T.times(texCoords.get(f.texIndices[f.decomp[tri + 1]]));
					Point2 c = T.times(texCoords.get(f.texIndices[f.decomp[tri + 2]]));

					builder.reset();
					builder.add(a);
					builder.add(b);
					builder.add(c);

					Box2 bound = builder.getBoundingBox();

					int i0 = MathUtil.clamp((int) Math.floor(bound.minimumX() * (double) triangleLookupGridSize), 0, triangleLookupGridSize - 1);
					int i1 = MathUtil.clamp((int) Math.floor(bound.maximumX() * (double) triangleLookupGridSize), 0, triangleLookupGridSize - 1);
					int j0 = MathUtil.clamp((int) Math.floor(bound.minimumY() * (double) triangleLookupGridSize), 0, triangleLookupGridSize - 1);
					int j1 = MathUtil.clamp((int) Math.floor(bound.maximumY() * (double) triangleLookupGridSize), 0, triangleLookupGridSize - 1);

					for (int i = i0; i <= i1; i++) {
						double x0 = (double) i / (double) triangleLookupGridSize;
						double x1 = (double) (i + 1) / (double) triangleLookupGridSize;

						for (int j = j0; j <= j1; j++) {
							double y0 = (double) j / (double) triangleLookupGridSize;
							double y1 = (double) (j + 1) / (double) triangleLookupGridSize;

							Box2 cell = new Box2(x0, y0, x1, y1);

							if (GeometryUtil.boxIntersectsTriangle(cell, a, b, c)) {
								int cellIndex = j * triangleLookupGridSize + i;
								int triIndex = fi * maximumTrianglesPerFace + tri / 3;
								IntegerArray list = triangleLookup.get(cellIndex);
								if (list == null) {
									list = new IntegerArray();
									triangleLookup.put(cellIndex, list);
								}
								list.add(triIndex);
							}
						}
					}
				}
			}
		}

		int count = 0;
		for (IntegerArray list : triangleLookup.values()) {
			int n = list.size();
			for (int i = 1; i < n; i++) {
				int tri1 = list.get(i);
				int fi1 = tri1 / maximumTrianglesPerFace;
				int ti1 = (tri1 % maximumTrianglesPerFace) * 3;
				Face f1 = faces.get(fi1);
				Point2 a1 = texCoords.get(f1.texIndices[f1.decomp[ti1]]);
				Point2 b1 = texCoords.get(f1.texIndices[f1.decomp[ti1 + 1]]);
				Point2 c1 = texCoords.get(f1.texIndices[f1.decomp[ti1 + 2]]);

				for (int j = 0; j < i; j++) {
					int tri2 = list.get(j);
					int fi2 = tri2 / maximumTrianglesPerFace;
					int ti2 = (tri2 % maximumTrianglesPerFace) * 3;
					Face f2 = faces.get(fi2);
					Point2 a2 = texCoords.get(f2.texIndices[f2.decomp[ti2]]);
					Point2 b2 = texCoords.get(f2.texIndices[f2.decomp[ti2 + 1]]);
					Point2 c2 = texCoords.get(f2.texIndices[f2.decomp[ti2 + 2]]);

					if (GeometryUtil.triangleIntersectsTriangle(a1, b1, c1, a2, b2, c2)) {
						System.err.println("WARNING: Triangles intersect -------------------------------");
						System.err.printf("% 5d: (%5.3f, %5.3f) - (%5.3f, %5.3f) - (%5.3f, %5.3f)", tri1,
								a1.x(), a1.y(),
								b1.x(), b1.y(),
								c1.x(), c1.y());
						System.err.println();
						System.err.printf("% 5d: (%5.3f, %5.3f) - (%5.3f, %5.3f) - (%5.3f, %5.3f)", tri2,
								a2.x(), a2.y(),
								b2.x(), b2.y(),
								c2.x(), c2.y());
						System.err.println();
						count++;
					}
				}
			}
		}

		if (count > 0) {
			System.err.printf("WARNING: There are %d pairs of triangles that intersect.", count);
			System.err.println();
		}
	}



	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context, double ru, double rv, double rj) {

		return getSurfacePointFromUV(context, new Point2(ru, rv));
	}

	public double getSurfacePointFromUV(ShadingContext context, Point2 uv) {
		if (triangleLookup == null) {
			synchronized (this) {
				if (triangleLookup == null) {
					prepareTriangleLookup();
				}
			}
		}

		if (!texBoundingBox.contains(uv)) {
			return -1.0;
		}

		double x = (uv.x() - texBoundingBox.minimumX()) / texBoundingBox.lengthX();
		double y = (uv.y() - texBoundingBox.minimumY()) / texBoundingBox.lengthY();

		int i = MathUtil.clamp((int) Math.floor(x * (double) triangleLookupGridSize), 0, triangleLookupGridSize - 1);
		int j = MathUtil.clamp((int) Math.floor(y * (double) triangleLookupGridSize), 0, triangleLookupGridSize - 1);

		int cellIndex = j * triangleLookupGridSize + i;
		IntegerArray list = triangleLookup.get(cellIndex);
		if (list != null) {
			for (int triIndex : list) {
				int fi = triIndex / maximumTrianglesPerFace;
				int ti = triIndex % maximumTrianglesPerFace;

				Face face = faces.get(fi);
				double weight = face.getSurfacePointFromUV(context, uv, ti);
				if (weight > 0.0) {
					context.setPrimitiveIndex(fi);
					return weight;
				}
			}
		}

		return -1.0;
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

		Face			face = this.faces.get(faceIndex);
		double			t = face.plane.intersect(ray);

		if (recorder.interval().contains(t)) {

			Point3		p = ray.pointAt(t);
			Vector3		n = face.plane.normal();
			Vector3		u, v;

			for (int i = 0; i < face.indices.length; i++)
			{
				Point3	a = this.vertices.get(face.indices[i]);
				Point3	b = this.vertices.get(face.indices[(i + 1) % face.indices.length]);
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
		Face face = faces.get(x.getTag());
		Vector3 n = face.plane.normal();
		return Basis3.fromW(n, Basis3.Orientation.RIGHT_HANDED);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getShadingBasis(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Basis3 getShadingBasis(GeometryIntersection x) {
		return Basis3.fromW(getShadingNormal(x));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getShadingNormal(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Vector3 getShadingNormal(GeometryIntersection x) {
		Face face = faces.get(x.getTag());
		return face.getShadingNormal(x.getPosition());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	public Box3 getBoundingBox(int index) {
		return faces.get(index).boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	public Sphere getBoundingSphere(int index) {
		return faces.get(index).boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	public int getNumPrimitives() {
		return getNumFaces();
	}

	/**
	 * Gets the number of vertices in this mesh.
	 * @return The number of vertices in this mesh.
	 */
	public int getNumVertices() {
		return vertices.size();
	}

	/**
	 * Gets the number of faces in this mesh.
	 * @return The number of faces in this mesh.
	 */
	public int getNumFaces() {
		return faces.size();
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
		return Sphere.smallestContaining(this.vertices);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
		Point3 p = faces.get(index).generateRandomSurfacePoint(ru, rv, rj);
		Intersection x = super.newSurfacePoint(p, index).setPrimitiveIndex(index);
		x.prepareShadingContext(context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context, double ru,
			double rv, double rj) {
		double base = 0.0;
		double x = ru * getSurfaceArea();
		for (int i = 0; i < faces.size(); i++) {
			Face face = faces.get(i);
			double area = face.getSurfaceArea();
			if (x < base + area) {
				generateRandomSurfacePoint(i, context, (x - base) / area, rv, rj);
				return;
			}
			base += area;
		}
		super.generateRandomSurfacePoint(context, ru, rv, rj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getSurfaceArea(int)
	 */
	@Override
	public double getSurfaceArea(int index) {
		return faces.get(index).getSurfaceArea();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		if (surfaceArea < 0.0) {
			synchronized (this) {
				double area = 0.0;
				for (Face face : faces) {
					area += face.getSurfaceArea();
				}
				surfaceArea = area;
			}
		}
		return surfaceArea;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getTextureCoordinates(ca.eandb.jmist.framework.geometry.AbstractGeometry.GeometryIntersection)
	 */
	@Override
	protected Point2 getTextureCoordinates(GeometryIntersection x) {
		Face face = faces.get(x.getTag());
		return face.getUV(x.getPosition());
	}

	/**
	 * A face of a polyhedron.
	 * @author Brad Kimmel
	 */
	private final class Face implements Bounded3, Serializable {

		/** Serialization version ID. */
		private static final long serialVersionUID = 5733963094194933946L;

		/**
		 * Creates a new <code>Face</code>.
		 * @param indices The indices into {@link PolyhedronGeometry#vertices}
		 * 		of the vertices of the face.
		 */
		public Face(int[] indices, int[] texIndices, int[] normalIndices) {
			this.indices = indices;
			this.texIndices = texIndices;
			this.normalIndices = normalIndices;
			this.plane = this.computePlane();
		}

		/**
		 * Computes the <code>Plane3</code> in which this <code>Face</code>
		 * lies.
		 * @return The <code>Plane3</code> in which this <code>Face</code>
		 * 		lies.
		 */
		public Plane3 computePlane() {
			return Plane3.throughPoint(vertices.get(indices[0]), this.computeFaceNormal());
		}

		/**
		 * Computes the <code>Vector3</code> perpendicular to this
		 * <code>Face</code>.
		 * @return The <code>Vector3</code> perpendicular to this
		 * 		<code>Face</code>.
		 */
		public Vector3 computeFaceNormal() {

			Vector3	u = vertices.get(indices[0]).vectorTo(vertices.get(indices[1]));
			Vector3	v = vertices.get(indices[0]).vectorTo(vertices.get(indices[indices.length - 1]));

			return u.cross(v).unit();

		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
		 */
		public Box3 boundingBox() {
			BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
			for (int i = 0; i < indices.length; i++) {
				builder.add(vertices.get(indices[i]));
			}
			return builder.getBoundingBox();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
		 */
		public Sphere boundingSphere() {
			List<Point3> verts = new ArrayList<Point3>(indices.length);
			for (int i = 0; i < indices.length; i++) {
				verts.add(vertices.get(indices[i]));
			}
			return Sphere.smallestContaining(verts);
		}

		public double getSurfaceArea() {
			if (area < 0.0) {
				synchronized (this) {
					Vector3 n = computeFaceNormal();
					Vector3 v0 = vertices.get(indices[indices.length - 1]).vectorFromOrigin();
					Vector3 v1;
					Vector3 r = Vector3.ZERO;
					for (int i = 0; i < indices.length; i++) {
						v1 = vertices.get(indices[i]).vectorFromOrigin();
						r = r.plus(v0.cross(v1));
						v0 = v1;
					}
					area = 0.5 * Math.abs(n.dot(r));
				}
			}
			return area;
		}

		public double getSurfacePointFromUV(ShadingContext context, Point2 uv, int tri) {
			Point2 a = texCoords.get(texIndices[decomp[3 * tri]]);
			Point2 b = texCoords.get(texIndices[decomp[3 * tri + 1]]);
			Point2 c = texCoords.get(texIndices[decomp[3 * tri + 2]]);

			if (!GeometryUtil.pointInTriangle(uv, a, b, c)) {
				return -1.0;
			}

			double area = GeometryUtil.areaOfTriangle(a, b, c);
			double A = GeometryUtil.areaOfTriangle(b, c, uv) / area;
			double B = GeometryUtil.areaOfTriangle(c, a, uv) / area;

			assert(A > 0.0 && B > 0.0 && A + B < 1.0);

			double C = 1.0 - A - B;

			Vector3 n;
			if (normalIndices != null) {
				Vector3 na = normals.get(normalIndices[decomp[3 * tri]]);
				Vector3 nb = normals.get(normalIndices[decomp[3 * tri + 1]]);
				Vector3 nc = normals.get(normalIndices[decomp[3 * tri + 2]]);
				n = na.times(A).plus(nb.times(B)).plus(nc.times(C));
			} else {
				n = plane.normal();
			}

			Point3 pa = vertices.get(indices[decomp[3 * tri]]);
			Point3 pb = vertices.get(indices[decomp[3 * tri + 1]]);
			Point3 pc = vertices.get(indices[decomp[3 * tri + 2]]);

			double geomArea = GeometryUtil.areaOfTriangle(pa, pb, pc);

			Point3 p = new Point3(
					pa.x() * A + pb.x() * B + pc.x() * C,
					pa.y() * A + pb.y() * B + pc.y() * C,
					pa.z() * A + pb.z() * B + pc.z() * C);

			context.setPosition(p);
			context.setNormal(plane.normal());
			context.setShadingNormal(n);
			context.setUV(uv);

			return area / geomArea;

		}

		public Point3 generateRandomSurfacePoint(double ru, double rv, double rj) {
			decompose();
			SeedReference vref = new SeedReference(rv);
			int tri = 3 * rnd.next(vref);
			Point3 a = vertices.get(indices[decomp[tri]]);
			Point3 b = vertices.get(indices[decomp[tri + 1]]);
			Point3 c = vertices.get(indices[decomp[tri + 2]]);
			return RandomUtil.uniformOnTriangle(a, b, c, ru, vref.seed);
		}
		
		public Vector3 getShadingNormal(Point3 p) {
			Vector3 nf = plane.normal();
			if (this.normalIndices == null) {
				return nf;
			}

			decompose();

			Vector3 n = plane.normal();
			p = plane.project(p);


			for (int i = 0; i < decomp.length; i += 3) {
				Point3 a = vertices.get(indices[decomp[i]]);
				Point3 b = vertices.get(indices[decomp[i + 1]]);
				Point3 c = vertices.get(indices[decomp[i + 2]]);
				Vector3 ab = a.vectorTo(b);
				Vector3 ac = a.vectorTo(c);
				Vector3 pa = p.vectorTo(a);
				Vector3 pb = p.vectorTo(b);
				Vector3 pc = p.vectorTo(c);

				double area = n.dot(ab.cross(ac));
				double A = n.dot(pb.cross(pc)) / area;
				if (A < 0.0) continue;
				double B = n.dot(pc.cross(pa)) / area;
				if (B < 0.0) continue;
				double C = 1.0 - A - B;
				if (C < 0.0) continue;

				Vector3 na = normals.get(normalIndices[decomp[i]]);
				Vector3 nb = normals.get(normalIndices[decomp[i + 1]]);
				Vector3 nc = normals.get(normalIndices[decomp[i + 2]]);

				if (minVertexNormalDotProduct < Double.POSITIVE_INFINITY) {
					na = nf.dot(na) < minVertexNormalDotProduct ? nf : na;
					nb = nf.dot(nb) < minVertexNormalDotProduct ? nf : nb;
					nc = nf.dot(nc) < minVertexNormalDotProduct ? nf : nc;
				}

				return na.times(A).plus(nb.times(B)).plus(nc.times(C)).unit();
			}

			return nf;
		}

		private Point2 getUV(Point3 p) {
			decompose();

			if (decomp.length > 6 && this.texIndices == null) {
				return Point2.ORIGIN;
			}

			Vector3 n = plane.normal();
			p = plane.project(p);


			for (int i = 0; i < decomp.length; i += 3) {
				Point3 a = vertices.get(indices[decomp[i]]);
				Point3 b = vertices.get(indices[decomp[i + 1]]);
				Point3 c = vertices.get(indices[decomp[i + 2]]);
				Vector3 ab = a.vectorTo(b);
				Vector3 ac = a.vectorTo(c);
				Vector3 pa = p.vectorTo(a);
				Vector3 pb = p.vectorTo(b);
				Vector3 pc = p.vectorTo(c);

				double area = n.dot(ab.cross(ac));
				double A = n.dot(pb.cross(pc)) / area;
				if (A < 0.0) continue;
				double B = n.dot(pc.cross(pa)) / area;
				if (B < 0.0) continue;
				double C = 1.0 - A - B;
				if (C < 0.0) continue;

				Point2 ta = null, tb = null, tc = null;
				if (texIndices != null) {
					ta = texCoords.get(texIndices[decomp[i]]);
					tb = texCoords.get(texIndices[decomp[i + 1]]);
					tc = texCoords.get(texIndices[decomp[i + 2]]);
				} else {
					ta = Point2.ORIGIN;
					if (i == 0) {
						tb = new Point2(1, 0);
						tc = new Point2(1, 1);
					} else {
						tb = new Point2(1, 1);
						tc = new Point2(0, 1);
					}
				}

				return new Point2(ta.x() * A + tb.x() * B + tc.x() * C, ta.y() * A + tb.y() * B + tc.y() * C);
			}

			return Point2.ORIGIN;
		}

		private synchronized void decompose() {
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
				decomp[3 * i] = 0;
				decomp[3 * i + 1] = i + 1;
				decomp[3 * i + 2] = i + 2;
				weight[i] = GeometryUtil.areaOfTriangle(
						vertices.get(indices[0]),
						vertices.get(indices[i + 1]),
						vertices.get(indices[i + 2]));
			}
			rnd = new CategoricalRandom(weight);
		}

		private int[] decomp = null;

		private CategoricalRandom rnd;

		/** The <code>Plane3</code> in which this face lies. */
		public Plane3 plane;

		/**
		 * The indices into {@link PolyhedronGeometry#vertices} of the vertices
		 * of this face.
		 */
		public final int[] indices;

		/**
		 * The indices into {@link PolyhedronGeometry#texCoords} of the
		 * texture vertices of this face.
		 */
		public final int[] texIndices;

		/**
		 * The indices into {@link PolyhedronGeometry#normals} of the normals
		 * for corresponding to the vertices on this face.
		 */
		public final int[] normalIndices;

		/** The area of this face. */
		public double area = -1.0;

	}

	/** The array of the vertices of this polyhedron. */
	private final List<Point3> vertices;

	/**
	 * The array of vertex normals corresponding to the vertices in
	 * {@link #vertices}.
	 */
	private final List<Vector3> normals;

	/** An array of texture coordinates. */
	private final List<Point2> texCoords;

	/** An array of the <code>Face</code>s of this polyhedron. */
	private final List<Face> faces = new ArrayList<Face>();

	/** The surface area of this polyhedron. */
	private double surfaceArea = -1.0;

	/**
	 * The cosine of the maximum angle to accept between a vertex normal and
	 * the corresponding face normal.  Vertex normals that deviate from the
	 * face normal by more than this will be replaced with the face normal for
	 * the purposes of computing the interpolated normal.
	 */
	private double minVertexNormalDotProduct = Double.POSITIVE_INFINITY;

}
