/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * Base class for geometry classes.
 * @author Brad Kimmel
 */
public abstract class AbstractGeometry implements Geometry {

	/**
	 * @author Brad Kimmel
	 *
	 */
	protected static class GeometryIntersection implements Intersection {

		/**
		 * @param geometry
		 * @param ray
		 * @param distance
		 * @param front
		 * @param surfaceId
		 * @param material
		 */
		private GeometryIntersection(AbstractGeometry geometry, Ray3 ray,
				double distance, boolean front, int surfaceId, Material material) {
			this.geometry = geometry;
			this.ray = ray;
			this.distance = distance;
			this.front = front;
			this.surfaceId = surfaceId;
			this.material = material;
		}

		public int surfaceId() {
			return this.surfaceId;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Intersection#distance()
		 */
		public double distance() {
			return this.distance;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Intersection#front()
		 */
		public boolean front() {
			return this.front;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Intersection#incident()
		 */
		public Vector3 incident() {
			return this.ray.direction();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#ambientMedium()
		 */
		public Medium ambientMedium() {
			return Medium.VACUUM;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#basis()
		 */
		public Basis3 basis() {
			if (this.basis == null) {
				this.setBasis(this.geometry.getBasis(this));
			}

			return this.basis;
		}

		public GeometryIntersection setBasis(Basis3 basis) {
			this.basis = basis;
			this.normal = basis.w();
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#location()
		 */
		public Point3 location() {
			if (this.location == null) {
				this.setLocation(this.ray.pointAt(this.distance));
			}

			return this.location;
		}

		public GeometryIntersection setLocation(Point3 location) {
			this.location = location;
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#material()
		 */
		public Material material() {
			return this.material;
		}

		public GeometryIntersection setMaterial(Material material) {
			this.material = material;
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#shadingBasis()
		 */
		public Basis3 shadingBasis() {
			return this.basis();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#shadingNormal()
		 */
		public Vector3 shadingNormal() {
			return this.normal();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#normal()
		 */
		public Vector3 normal() {
			if (this.normal == null) {
				this.setNormal(this.geometry.getNormal(this));
			}

			return this.normal;
		}

		public GeometryIntersection setNormal(Vector3 normal) {
			this.normal = normal;
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#tangent()
		 */
		public Vector3 tangent() {
			return this.basis().u();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#textureCoordinates()
		 */
		public Point2 textureCoordinates() {
			if (this.textureCoordinates == null) {
				this.setTextureCoordinates(this.geometry.getTextureCoordinates(this));
			}

			return this.textureCoordinates;
		}

		public GeometryIntersection setTextureCoordinates(Point2 textureCoordinates) {
			this.textureCoordinates = textureCoordinates;
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#closed()
		 */
		public boolean closed() {
			return this.geometry.isClosed();
		}

		private AbstractGeometry	geometry;
		private Ray3				ray;
		private double				distance;
		private boolean				front;
		private int					surfaceId;
		private Point3				location				= null;
		private Basis3				basis					= null;
		private Material			material				= null;
		private Vector3				normal					= null;
		private Point2				textureCoordinates		= null;

	}

	protected final GeometryIntersection newIntersection(Ray3 ray,
			double distance, boolean front, int surfaceId, Material material) {

		return new GeometryIntersection(this, ray, distance,
				front, surfaceId, material);

	}

	protected final GeometryIntersection newIntersection(Ray3 ray,
			double distance, boolean front, Material material) {

		return this.newIntersection(ray, distance, front, 0, material);

	}

	protected Basis3 getBasis(GeometryIntersection x) {
		throw new UnsupportedOperationException();
	}

	protected Vector3 getNormal(GeometryIntersection x) {
		throw new UnsupportedOperationException();
	}

	protected Point2 getTextureCoordinates(GeometryIntersection x) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.toolkit.Interval)
	 */
	public boolean visibility(Ray3 ray, Interval I) {

		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(I);

		this.intersect(ray, recorder);

		return recorder.isEmpty() || !I.contains(recorder.nearestIntersection().distance());

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.toolkit.Point3, ca.eandb.jmist.toolkit.Point3)
	 */
	public boolean visibility(Point3 p, Point3 q) {

		/*
		 * Determine the visibility in terms of the other overloaded
		 * method.
		 */
		Vector3		d		= p.vectorTo(q);
		Ray3		ray		= new Ray3(p, d.unit());
		Interval	I		= new Interval(0.0, d.length()).expand(-MathUtil.EPSILON);

		return this.visibility(ray, I);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.PartialBoundable3#surfaceMayIntersect(ca.eandb.jmist.toolkit.Box3)
	 */
	public boolean surfaceMayIntersect(Box3 box) {
		return this.boundingBox().intersects(box);
	}

}
