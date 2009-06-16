/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionGeometry;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.SceneObject;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class StandardSceneObject implements SceneObject {

	private final Geometry geometry;

	private final Material material;

	private final Shader shader;

	private final Medium ambientMedium;

	private final double epsilon;

	/**
	 * @param geometry
	 * @param material
	 * @param shader
	 * @param ambientMedium
	 * @param epsilon
	 */
	public StandardSceneObject(Geometry geometry, Material material,
			Shader shader, Medium ambientMedium, double epsilon) {
		this.geometry = geometry;
		this.material = material;
		this.shader = shader;
		this.ambientMedium = ambientMedium;
		this.epsilon = epsilon;
	}

	/**
	 * @param geometry
	 * @param material
	 * @param shader
	 * @param ambientMedium
	 */
	public StandardSceneObject(Geometry geometry, Material material,
			Shader shader, Medium ambientMedium) {
		this(geometry, material, shader, ambientMedium, MathUtil.EPSILON);
	}

	/**
	 * @param geometry
	 * @param material
	 * @param shader
	 * @param ambientMedium
	 */
	public StandardSceneObject(Geometry geometry, Material material,
			Shader shader) {
		this(geometry, material, shader, null);
	}

	private class StandardSurfacePoint implements SurfacePoint {

		protected final SurfacePointGeometry geom;

		public StandardSurfacePoint(SurfacePointGeometry geom) {
			this.geom = geom;
		}

		@Override
		public Medium ambientMedium() {
			return ambientMedium;
		}

		@Override
		public Material material() {
			return material;
		}

		@Override
		public SceneObject sceneObject() {
			return StandardSceneObject.this;
		}

		@Override
		public Shader shader() {
			return shader;
		}

		@Override
		public Basis3 getBasis() {
			return geom.getBasis();
		}

		@Override
		public boolean isSurfaceClosed() {
			return geom.isSurfaceClosed();
		}

		@Override
		public Point3 getPosition() {
			return geom.getPosition();
		}

		@Override
		public Vector3 getNormal() {
			return geom.getNormal();
		}

		@Override
		public Basis3 getShadingBasis() {
			return geom.getShadingBasis();
		}

		@Override
		public Vector3 getShadingNormal() {
			return geom.getShadingNormal();
		}

		@Override
		public Vector3 getTangent() {
			return geom.getTangent();
		}

		@Override
		public Point2 getUV() {
			return geom.getUV();
		}

	}

	private class StandardIntersection extends StandardSurfacePoint implements Intersection {

		public StandardIntersection(IntersectionGeometry geom) {
			super(geom);
		}

		@Override
		public double getDistance() {
			return ((IntersectionGeometry) geom).getDistance();
		}

		@Override
		public boolean isFront() {
			return ((IntersectionGeometry) geom).isFront();
		}

		@Override
		public Vector3 getIncident() {
			return ((IntersectionGeometry) geom).getIncident();
		}

	}


	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneObject#intersect(ca.eandb.jmist.math.Ray3, double)
	 */
	@Override
	public Intersection intersect(Ray3 ray, double distance) {
		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(new Interval(epsilon, distance));
		geometry.intersect(ray, recorder);
		if (!recorder.isEmpty()) {
			return new StandardIntersection(recorder.nearestIntersection());
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneObject#intersect(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public Intersection intersect(Ray3 ray) {
		return intersect(ray, Double.POSITIVE_INFINITY);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		return geometry.boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return geometry.boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.math.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {
		return geometry.visibility(ray, I);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(Ray3 ray) {
		return geometry.visibility(ray);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Point3, ca.eandb.jmist.math.Point3)
	 */
	@Override
	public boolean visibility(Point3 p, Point3 q) {
		return geometry.visibility(p, q);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneObject#generateRandomSurfacePoint()
	 */
	@Override
	public SurfacePoint generateRandomSurfacePoint() {
		SurfacePointGeometry x = geometry.generateRandomSurfacePoint();
		return new StandardSurfacePoint(x);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneObject#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		return geometry.getSurfaceArea();
	}

}
