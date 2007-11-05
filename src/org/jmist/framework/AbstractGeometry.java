/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;
import org.jmist.packages.NearestIntersectionRecorder;

/**
 * Base class for geometry classes.
 * @author bkimmel
 */
public abstract class AbstractGeometry implements Geometry {

	/**
	 * @author bkimmel
	 *
	 */
	protected static class GeometryIntersection implements Intersection {

		/**
		 * @param geometry
		 * @param ray
		 * @param distance
		 * @param front
		 * @param surfaceId
		 */
		private GeometryIntersection(AbstractGeometry geometry, Ray3 ray,
				double distance, boolean front, int surfaceId) {
			this.geometry = geometry;
			this.ray = ray;
			this.distance = distance;
			this.front = front;
			this.surfaceId = surfaceId;
		}

		public int surfaceId() {
			return this.surfaceId;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Intersection#distance()
		 */
		@Override
		public double distance() {
			return this.distance;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Intersection#front()
		 */
		@Override
		public boolean front() {
			return this.front;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Intersection#incident()
		 */
		@Override
		public Vector3 incident() {
			return this.ray.direction();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#ambientMedium()
		 */
		@Override
		public Medium ambientMedium() {
			return Medium.VACUUM;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#basis()
		 */
		@Override
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
		 * @see org.jmist.framework.SurfacePoint#illuminate(org.jmist.toolkit.Vector3, org.jmist.framework.Spectrum)
		 */
		@Override
		public void illuminate(Vector3 from, Spectrum irradiance) {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#location()
		 */
		@Override
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
		 * @see org.jmist.framework.SurfacePoint#material()
		 */
		@Override
		public Material material() {
			return this.material;
		}

		public GeometryIntersection setMaterial(Material material) {
			this.material = material;
			return this;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#microfacetBasis()
		 */
		@Override
		public Basis3 microfacetBasis() {
			return this.basis();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#microfacetNormal()
		 */
		@Override
		public Vector3 microfacetNormal() {
			return this.normal();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#normal()
		 */
		@Override
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
		 * @see org.jmist.framework.SurfacePoint#tangent()
		 */
		@Override
		public Vector3 tangent() {
			return this.basis().u();
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SurfacePoint#textureCoordinates()
		 */
		@Override
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
		 * @see org.jmist.framework.SurfacePoint#closed()
		 */
		@Override
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
			double distance, boolean front, int surfaceId) {

		return new GeometryIntersection(this, ray, distance,
				front, surfaceId);

	}

	protected final GeometryIntersection newIntersection(Ray3 ray,
			double distance, boolean front) {

		return this.newIntersection(ray, distance, front, 0);

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
	 * @see org.jmist.framework.VisibilityFunction3#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	public boolean visibility(Ray3 ray, Interval I) {

		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder();

		this.intersect(ray, I, recorder);

		return recorder.isEmpty() || !I.contains(recorder.nearestIntersection().distance());

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.VisibilityFunction3#visibility(org.jmist.toolkit.Point3, org.jmist.toolkit.Point3)
	 */
	public boolean visibility(Point3 p, Point3 q) {

		/*
		 * Determine the visibility in terms of the other overloaded
		 * method.
		 */
		Vector3		d		= p.vectorTo(q);
		Ray3		ray		= new Ray3(p, d.unit());
		Interval	I		= new Interval(0.0, d.length());

		return this.visibility(ray, I);

	}

}
