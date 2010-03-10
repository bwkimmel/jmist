/**
 * 
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class RenderUtil {
	
	/**
	 * This constructor is private because this class cannot be instantiated.
	 */
	private RenderUtil() {}
	
	private static final class SimpleSurfacePointGeometry implements SurfacePointGeometry {
		
		private final Point3 position;
		private final Basis3 basis;
		private final Basis3 shadingBasis;
		private final Point2 textureCoordinates;
		
		/**
		 * @param position
		 * @param basis
		 * @param shadingBasis
		 * @param textureCoordinates
		 */
		public SimpleSurfacePointGeometry(Point3 position, Basis3 basis,
				Basis3 shadingBasis, Point2 textureCoordinates) {
			this.position = position;
			this.basis = basis;
			this.shadingBasis = shadingBasis;
			this.textureCoordinates = textureCoordinates;
		}

		public Basis3 getBasis() {
			return basis;
		}

		public Vector3 getNormal() {
			return basis.w();
		}

		public Point3 getPosition() {
			return position;
		}

		public int getPrimitiveIndex() {
			return 0;
		}

		public Basis3 getShadingBasis() {
			return null;
		}

		public Vector3 getShadingNormal() {
			return shadingBasis.w();
		}

		public Vector3 getTangent() {
			return basis.u();
		}

		public Point2 getUV() {
			return textureCoordinates;
		}
		
	}
	
	
	public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Vector3 N) {
		Basis3 basis = Basis3.fromW(N);
		return new SimpleSurfacePointGeometry(p, basis, basis, Point2.ORIGIN);
	}
	
	public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Vector3 N, Vector3 shadingNormal) {
		Basis3 basis = Basis3.fromW(N);
		Basis3 shadingBasis = Basis3.fromW(shadingNormal);
		return new SimpleSurfacePointGeometry(p, basis, shadingBasis, Point2.ORIGIN);
	}
	
	public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Basis3 basis) {
		return new SimpleSurfacePointGeometry(p, basis, basis, Point2.ORIGIN);
	}
	
	public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Basis3 basis, Basis3 shadingBasis) {
		return new SimpleSurfacePointGeometry(p, basis, shadingBasis, Point2.ORIGIN);
	}
	
	public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Vector3 N, Point2 uv) {
		Basis3 basis = Basis3.fromW(N);
		return new SimpleSurfacePointGeometry(p, basis, basis, uv);		
	}
	
	public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Vector3 N, Vector3 shadingNormal, Point2 uv) {
		Basis3 basis = Basis3.fromW(N);
		Basis3 shadingBasis = Basis3.fromW(shadingNormal);
		return new SimpleSurfacePointGeometry(p, basis, shadingBasis, uv);
	}
	
	public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Basis3 basis, Point2 uv) {
		return new SimpleSurfacePointGeometry(p, basis, basis, uv);
	}
	
	public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Basis3 basis, Basis3 shadingBasis, Point2 uv) {
		return new SimpleSurfacePointGeometry(p, basis, shadingBasis, uv);
	}

}
