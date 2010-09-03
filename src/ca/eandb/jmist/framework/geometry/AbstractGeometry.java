/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
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
public abstract class AbstractGeometry implements SceneElement {

	/** Serialization version ID. */
	private static final long serialVersionUID = -2662537214883018706L;

	/**
	 * @author Brad Kimmel
	 *
	 */
	protected static class GeometryIntersection implements Intersection, SurfacePoint {

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
			if (this.shadingBasis == null) {
				Basis3 basis = this.geometry.getShadingBasis(this);
				if (basis == null) {
					basis = getBasis();
				}
				this.setShadingBasis(basis);				
			}
			return shadingBasis;
		}
		
		public GeometryIntersection setShadingBasis(Basis3 shadingBasis) {
			this.shadingBasis = shadingBasis;
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.SurfacePoint#shadingNormal()
		 */
		public Vector3 getShadingNormal() {
			if (this.shadingNormal == null) {
				Vector3 normal = shadingBasis != null ? shadingBasis.w() : this.geometry.getShadingNormal(this);
				if (normal == null) {
					normal = getNormal();
				}
				this.setShadingNormal(normal);				
			}
			return shadingNormal;
		}
		
		public GeometryIntersection setShadingNormal(Vector3 shadingNormal) {
			this.shadingNormal = shadingNormal;
			return this;
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

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.Intersection#getTolerance()
		 */
		public double getTolerance() {
			return tolerance;
		}

		public GeometryIntersection setTolerance(double tolerance) {
			this.tolerance = tolerance;
			return this;
		}

		public void prepareShadingContext(ShadingContext context) {
			context.setPosition(getPosition());
			context.setPrimitiveIndex(getPrimitiveIndex());
			context.setBasis(getBasis());
			context.setShadingBasis(getShadingBasis());
			context.setUV(getUV());
		}

		public Medium getAmbientMedium() {
			return null;
		}

		public Material getMaterial() {
			return null;
		}

		private static final double DEFAULT_TOLERANCE = MathUtil.TINY_EPSILON;

		private AbstractGeometry	geometry;
		private Ray3				ray;
		private double				distance;
		private boolean				front;
		private int					tag;
		private int					primitiveIndex	= 0;
		private Point3				location		= null;
		private Basis3				basis			= null;
		private Vector3				normal			= null;
		private Basis3				shadingBasis	= null;
		private Vector3				shadingNormal	= null;
		private Point2				uv				= null;
		private double				tolerance		= DEFAULT_TOLERANCE;

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
	
	protected Basis3 getShadingBasis(GeometryIntersection x) {
		return null;
	}
	
	protected Vector3 getShadingNormal(GeometryIntersection x) {
		return null;
	}

	protected Basis3 getBasis(GeometryIntersection x) {
		throw new UnsupportedOperationException();
	}

	protected Vector3 getNormal(GeometryIntersection x) {
		throw new UnsupportedOperationException();
	}

	protected Point2 getTextureCoordinates(GeometryIntersection x) {
		return Point2.ORIGIN;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea(int)
	 */
	public double getSurfaceArea(int index) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Box3)
	 */
	public boolean intersects(int index, Box3 box) {
		return box.intersects(getBoundingBox(index));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Ray3)
	 */
	public boolean visibility(int index, Ray3 ray) {
		Interval I = new Interval(0.0, ray.limit());
		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(I);
		intersect(index, ray, recorder);
		return recorder.isEmpty();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea()
	 */
	public double getSurfaceArea() {
		int n = getNumPrimitives();
		double area = 0.0;
		for (int i = 0; i < n; i++) {
			area += getSurfaceArea(i);
		}
		return area;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		int n = getNumPrimitives();
		for (int i = 0; i < n; i++) {
			intersect(i, ray, recorder);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(ca.eandb.jmist.math.Ray3)
	 */
	public boolean visibility(Ray3 ray) {
		Intersection x = NearestIntersectionRecorder.computeNearestIntersection(ray, this);
		return (x == null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#createLight()
	 */
	public Light createLight() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext)
	 */
	public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext)
	 */
	public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
	 */
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context, double ru, double rv, double rj) {
		generateRandomSurfacePoint(index, context, ru, rv, rj);
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
	 */
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context, double ru, double rv, double rj) {
		generateRandomSurfacePoint(context, ru, rv, rj);
		return 1.0;
	}

}
