/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.SurfacePoint;
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
		 * @param tag
		 */
		private GeometryIntersection(AbstractGeometry geometry, Ray3 ray,
				double distance, boolean front, int surfaceId) {
			this.geometry = geometry;
			this.ray = ray;
			this.distance = distance;
			this.front = front;
			this.tag = surfaceId;
		}

		public int getTag() {
			return this.tag;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Intersection#distance()
		 */
		public double getDistance() {
			return this.distance;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Intersection#front()
		 */
		public boolean isFront() {
			return this.front;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Intersection#incident()
		 */
		public Vector3 getIncident() {
			return this.ray.direction();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#basis()
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
		 * @see ca.eandb.jmist.framework.SurfacePoint#location()
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
		 * @see ca.eandb.jmist.framework.SurfacePoint#shadingBasis()
		 */
		public Basis3 getShadingBasis() {
			return this.getBasis();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#shadingNormal()
		 */
		public Vector3 getShadingNormal() {
			return this.getNormal();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#normal()
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
		 * @see ca.eandb.jmist.framework.SurfacePoint#tangent()
		 */
		public Vector3 getTangent() {
			return this.getBasis().u();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#textureCoordinates()
		 */
		public Point2 getUV() {
			if (this.uv == null) {
				this.setUV(this.geometry.getTextureCoordinates(this));
			}

			return this.uv;
		}

		public GeometryIntersection setUV(Point2 uv) {
			this.uv = uv;
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#getPrimitiveIndex()
		 */
		public int getPrimitiveIndex() {
			return primitiveIndex;
		}

		public GeometryIntersection setPrimitiveIndex(int primitiveIndex) {
			this.primitiveIndex = primitiveIndex;
			return this;
		}

		private AbstractGeometry	geometry;
		private Ray3				ray;
		private double				distance;
		private boolean				front;
		private int					tag;
		private int					primitiveIndex	= 0;
		private Point3				location		= null;
		private Basis3				basis			= null;
		private Vector3				normal			= null;
		private Point2				uv				= null;

	}

	protected final GeometryIntersection newIntersection(Ray3 ray,
			double distance, boolean front, int surfaceId) {

		return new GeometryIntersection(this, ray, distance,
				front, surfaceId);

	}

	protected final GeometryIntersection newSurfacePoint(Point3 p, boolean front, int surfaceId) {

		return new GeometryIntersection(this, new Ray3(p, Vector3.ZERO), 0.0,
				front, surfaceId);

	}

	protected final GeometryIntersection newSurfacePoint(Point3 p, int surfaceId) {

		return this.newSurfacePoint(p, true, surfaceId);

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
	 * @see ca.eandb.jmist.framework.Geometry#generateRandomSurfacePoint(int)
	 */
	@Override
	public SurfacePoint generateRandomSurfacePoint(int index) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#generateRandomSurfacePoint()
	 */
	@Override
	public SurfacePoint generateRandomSurfacePoint() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#getSurfaceArea(int)
	 */
	@Override
	public double getSurfaceArea(int index) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersects(int, ca.eandb.jmist.math.Box3)
	 */
	@Override
	public boolean intersects(int index, Box3 box) {
		return box.intersects(getBoundingBox(index));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersects(int, ca.eandb.jmist.math.Ray3, double)
	 */
	@Override
	public boolean visibility(int index, Ray3 ray, double maximumDistance) {
		Interval I = new Interval(MathUtil.EPSILON, maximumDistance);
		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(I);
		intersect(index, ray, recorder);
		return !recorder.isEmpty();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersects(int, ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(int index, Ray3 ray) {
		return visibility(index, ray, Double.POSITIVE_INFINITY);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		int n = getNumPrimitives();
		double area = 0.0;
		for (int i = 0; i < n; i++) {
			area += getSurfaceArea(i);
		}
		return area;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		int n = getNumPrimitives();
		for (int i = 0; i < n; i++) {
			intersect(i, ray, recorder);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Point3, ca.eandb.jmist.math.Point3)
	 */
	@Override
	public boolean visibility(Point3 p, Point3 q) {
		double d = p.distanceTo(q);
		Ray3 ray = new Ray3(p, p.vectorTo(q).divide(d));
		return visibility(ray, d);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#visibility(ca.eandb.jmist.math.Ray3, double)
	 */
	@Override
	public boolean visibility(Ray3 ray, double maximumDistance) {
		Intersection x = NearestIntersectionRecorder.computeNearestIntersection(ray, this);
		return (x == null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#visibility(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(Ray3 ray) {
		return visibility(ray, Double.POSITIVE_INFINITY);
	}

}
