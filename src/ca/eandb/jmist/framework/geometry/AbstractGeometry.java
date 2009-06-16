/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.IntersectionGeometry;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.SurfacePointGeometry;
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
	protected static class GeometryIntersection implements IntersectionGeometry {

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
		 * @see ca.eandb.jmist.framework.IntersectionGeometry#distance()
		 */
		public double getDistance() {
			return this.distance;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionGeometry#front()
		 */
		public boolean isFront() {
			return this.front;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionGeometry#incident()
		 */
		public Vector3 getIncident() {
			return this.ray.direction();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePointGeometry#basis()
		 */
		public Basis3 getBasis() {
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
		 * @see ca.eandb.jmist.framework.SurfacePointGeometry#location()
		 */
		public Point3 getPosition() {
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
		 * @see ca.eandb.jmist.framework.SurfacePointGeometry#shadingBasis()
		 */
		public Basis3 getShadingBasis() {
			return this.getBasis();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePointGeometry#shadingNormal()
		 */
		public Vector3 getShadingNormal() {
			return this.getNormal();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePointGeometry#normal()
		 */
		public Vector3 getNormal() {
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
		 * @see ca.eandb.jmist.framework.SurfacePointGeometry#tangent()
		 */
		public Vector3 getTangent() {
			return this.getBasis().u();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePointGeometry#textureCoordinates()
		 */
		public Point2 getUV() {
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
		 * @see ca.eandb.jmist.framework.SurfacePointGeometry#closed()
		 */
		public boolean isSurfaceClosed() {
			return this.geometry.isClosed();
		}

		private AbstractGeometry	geometry;
		private Ray3				ray;
		private double				distance;
		private boolean				front;
		private int					surfaceId;
		private Point3				location				= null;
		private Basis3				basis					= null;
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

	protected final GeometryIntersection newSurfacePoint(Point3 p, boolean front, int surfaceId) {

		return new GeometryIntersection(this, new Ray3(p, Vector3.ZERO), 0.0,
				front, surfaceId);

	}

	protected final GeometryIntersection newSurfacePoint(Point3 p, boolean front) {

		return this.newSurfacePoint(p, front, 0);

	}

	protected final GeometryIntersection newSurfacePoint(Point3 p, int surfaceId) {

		return this.newSurfacePoint(p, true, surfaceId);

	}

	protected final GeometryIntersection newSurfacePoint(Point3 p) {

		return this.newSurfacePoint(p, true, 0);

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

		return recorder.isEmpty() || !I.contains(recorder.nearestIntersection().getDistance());

	}

	public boolean visibility(Ray3 ray) {

		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder();

		this.intersect(ray, recorder);

		return recorder.isEmpty();

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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#generateRandomSurfacePoint()
	 */
	@Override
	public SurfacePointGeometry generateRandomSurfacePoint() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		throw new UnsupportedOperationException();
	}

}
