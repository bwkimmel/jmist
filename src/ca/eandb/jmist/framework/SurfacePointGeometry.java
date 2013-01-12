package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents the geometric properties of a point on the surface of some
 * element in a scene. 
 * @author Brad Kimmel
 */
public interface SurfacePointGeometry {

	/** The position of the surface point. */
	Point3 getPosition();

	/** The direction perpendicular to the surface. */
	Vector3 getNormal();

	/** The orientation of the surface. */
	Basis3 getBasis();

	/** The micro-surface normal. */
	Vector3 getShadingNormal();

	/** The micro-surface orientation. */
	Basis3 getShadingBasis();

	/** The first tangent vector. */
	Vector3 getTangent();

	/** The texture coordinates. */
	Point2 getUV();

	/** The ID of the primitive from the <code>SceneElement</code>. */
	int getPrimitiveIndex();
	
	/**
	 * A surface point at the origin oriented according to the standard basis
	 * (the normal in the positive z-direction).
	 */
	public static final SurfacePointGeometry STANDARD = new SurfacePointGeometry() {
		public Basis3 getBasis() {
			return Basis3.STANDARD;
		}
		public Vector3 getNormal() {
			return Vector3.K;
		}
		public Point3 getPosition() {
			return Point3.ORIGIN;
		}
		public int getPrimitiveIndex() {
			return 0;
		}
		public Basis3 getShadingBasis() {
			return Basis3.STANDARD;
		}
		public Vector3 getShadingNormal() {
			return Vector3.K;
		}
		public Vector3 getTangent() {
			return Vector3.I;
		}
		public Point2 getUV() {
			return Point2.ORIGIN;
		}
	};

}