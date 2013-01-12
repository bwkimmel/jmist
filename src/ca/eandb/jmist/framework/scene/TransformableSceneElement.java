/**
 * 
 */
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.AffineTransformable3;
import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.InvertibleAffineTransformation3;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnimplementedException;

/**
 * A decorator <code>SceneElement</code> that transforms the underlying
 * <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public final class TransformableSceneElement extends SceneElementDecorator
		implements AffineTransformable3 {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -2120007083803470213L;

	private final InvertibleAffineTransformation3 t = new InvertibleAffineTransformation3();
	
	private transient boolean ready = true;
	
	private transient boolean shapePreserving = true;
	
	private transient double scaleFactor = 1.0;
	
	private transient Box3 bound = null;
	
	/**
	 * @param inner
	 */
	public TransformableSceneElement(SceneElement inner) {
		super(inner);
	}
	
	private synchronized void computeBoundingBox() {
		if (bound != null) {
			return;
		}
		BoundingBoxBuilder3 bbox = new BoundingBoxBuilder3();
		for (int i = 0, n = super.getNumPrimitives(); i < n; i++) {
			Box3 b = super.getBoundingBox(i);
			for (int j = 0; j < 8; j++) {
				bbox.add(t.apply(b.corner(j)));
			}
		}
		bound = bbox.getBoundingBox();
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		if (bound == null) {
			computeBoundingBox();
		}

		return bound;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		if (bound == null) {
			computeBoundingBox();
		}
		return new Sphere(bound.center(), bound.diagonal() / 2.0);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#createLight()
	 */
	@Override
	public Light createLight() {
		throw new UnimplementedException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context, double ru, double rv,
			double rj) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context, double ru, double rv, double rj) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context,
			double ru, double rv, double rj) {
		super.generateRandomSurfacePoint(index, context, ru, rv, rj);
		transformShadingContext(context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context, double ru,
			double rv, double rj) {
		super.generateRandomSurfacePoint(context, ru, rv, rj);
		transformShadingContext(context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		BoundingBoxBuilder3 bbox = new BoundingBoxBuilder3();
		Box3 b = super.getBoundingBox(index);
		for (int j = 0; j < 8; j++) {
			bbox.add(t.apply(b.corner(j)));
		}
		return bbox.getBoundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		Box3 b = super.getBoundingBox(index);
		return new Sphere(b.center(), b.diagonal() / 2.0);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		if (!ready) {
			initialize();
		}
		if (!shapePreserving) {
			throw new UnsupportedOperationException();
		}
		return scaleFactor * scaleFactor * super.getSurfaceArea(); 
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#getSurfaceArea(int)
	 */
	@Override
	public double getSurfaceArea(int index) {
		if (!ready) {
			initialize();
		}
		if (!shapePreserving) {
			throw new UnsupportedOperationException();
		}
		return scaleFactor * scaleFactor * super.getSurfaceArea(); 
	}
	
	private void transformShadingContext(ShadingContext context) {

		Basis3 basis = context.getShadingBasis();
		Vector3 u = t.apply(basis.u());
		Vector3 v = t.apply(basis.v());
		
		context.setShadingBasis(Basis3.fromUV(u, v));
		
		basis = context.getBasis();
		u = t.apply(basis.u());
		v = t.apply(basis.v());
		
		context.setBasis(Basis3.fromUV(u, v));
		
		Point3 p = t.apply(context.getPosition());
		context.setPosition(p);
		
	}
	
	private class TransformingIntersectionDecorator extends IntersectionRecorderDecorator {

		protected TransformingIntersectionDecorator(IntersectionRecorder inner) {
			super(inner);
		}

		@Override
		public void record(Intersection intersection) {
			inner.record(new IntersectionDecorator(intersection) {
				protected void transformShadingContext(
						ShadingContext context) {
					TransformableSceneElement.this.transformShadingContext(context);
				}
			});
		}
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		ray = t.applyInverse(ray);
		super.intersect(index, ray, new TransformingIntersectionDecorator(recorder));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		ray = t.applyInverse(ray);
		super.intersect(ray, new TransformingIntersectionDecorator(recorder));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersects(int, ca.eandb.jmist.math.Box3)
	 */
	@Override
	public boolean intersects(int index, Box3 box) {
		BoundingBoxBuilder3 b = new BoundingBoxBuilder3();
		for (int i = 0; i < 8; i++) {
			b.add(t.applyInverse(box.corner(i)));
		}
		return super.intersects(index, b.getBoundingBox());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#visibility(int, ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(int index, Ray3 ray) {
		ray = t.applyInverse(ray);
		return super.visibility(index, ray);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#visibility(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(Ray3 ray) {
		ray = t.applyInverse(ray);
		return super.visibility(ray);
	}

	private void initialize() {
		if (!ready) {
			AffineMatrix3 matrix = t.apply(AffineMatrix3.IDENTITY);
			scaleFactor = Math.cbrt(matrix.determinant());
			
			Vector3 u = t.apply(Vector3.I).unit();
			Vector3 v = t.apply(Vector3.J).unit();
			Vector3 w = t.apply(Vector3.K).unit();
			
			shapePreserving = (u.dot(v) < MathUtil.EPSILON)
					&& (v.dot(w) < MathUtil.EPSILON)
					&& (w.dot(u) < MathUtil.EPSILON);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformable3#transform(ca.eandb.jmist.math.AffineMatrix3)
	 */
	@Override
	public void transform(AffineMatrix3 T) {
		t.transform(T);
		ready = false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.LinearTransformable3#transform(ca.eandb.jmist.math.LinearMatrix3)
	 */
	@Override
	public void transform(LinearMatrix3 T) {
		t.transform(T);
		ready = false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable3#rotate(ca.eandb.jmist.math.Vector3, double)
	 */
	@Override
	public void rotate(Vector3 axis, double angle) {
		t.rotate(axis, angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable3#rotateX(double)
	 */
	@Override
	public void rotateX(double angle) {
		t.rotateX(angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable3#rotateY(double)
	 */
	@Override
	public void rotateY(double angle) {
		t.rotateY(angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable3#rotateZ(double)
	 */
	@Override
	public void rotateZ(double angle) {
		t.rotateZ(angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Translatable3#translate(ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public void translate(Vector3 v) {
		t.translate(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scalable#scale(double)
	 */
	@Override
	public void scale(double c) {
		t.scale(c);
		if (ready) {
			scaleFactor *= c;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Stretchable3#stretch(ca.eandb.jmist.math.Vector3, double)
	 */
	@Override
	public void stretch(Vector3 axis, double c) {
		t.stretch(axis, c);
		ready = false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable3#stretch(double, double, double)
	 */
	@Override
	public void stretch(double cx, double cy, double cz) {
		t.stretch(cx, cy, cz);
		ready = false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable3#stretchX(double)
	 */
	@Override
	public void stretchX(double cx) {
		t.stretchX(cx);
		ready = false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable3#stretchY(double)
	 */
	@Override
	public void stretchY(double cy) {
		t.stretchY(cy);
		ready = false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable3#stretchZ(double)
	 */
	@Override
	public void stretchZ(double cz) {
		t.stretchZ(cz);
		ready = false;
	}

}
