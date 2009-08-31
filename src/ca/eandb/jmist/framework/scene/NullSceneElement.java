/**
 *
 */
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author Brad
 *
 */
public final class NullSceneElement implements SceneElement {

	/** Serialization version ID. */
	private static final long serialVersionUID = 102708499493815006L;

	public static final SceneElement INSTANCE = new NullSceneElement();

	private NullSceneElement() {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#createLight()
	 */
	public Light createLight() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
	 */
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context) {
		throw new IndexOutOfBoundsException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
	 */
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext)
	 */
	public void generateRandomSurfacePoint(int index, ShadingContext context) {
		throw new IndexOutOfBoundsException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext)
	 */
	public void generateRandomSurfacePoint(ShadingContext context) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	public Box3 getBoundingBox(int index) {
		return Box3.EMPTY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	public Sphere getBoundingSphere(int index) {
		return Sphere.EMPTY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	public int getNumPrimitives() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea(int)
	 */
	public double getSurfaceArea(int index) {
		throw new IndexOutOfBoundsException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea()
	 */
	public double getSurfaceArea() {
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		throw new IndexOutOfBoundsException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Box3)
	 */
	public boolean intersects(int index, Box3 box) {
		throw new IndexOutOfBoundsException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3)
	 */
	public boolean visibility(int index, Ray3 ray) {
		throw new IndexOutOfBoundsException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		return Box3.EMPTY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return Sphere.EMPTY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
	 */
	public boolean visibility(Ray3 ray) {
		return true;
	}

}
