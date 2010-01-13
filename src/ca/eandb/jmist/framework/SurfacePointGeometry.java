package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

public interface SurfacePointGeometry {

	Point3 getPosition();

	Vector3 getNormal();

	Basis3 getBasis();

	Vector3 getShadingNormal();

	Basis3 getShadingBasis();

	Vector3 getTangent();

	Point2 getUV();

	int getPrimitiveIndex();
	
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