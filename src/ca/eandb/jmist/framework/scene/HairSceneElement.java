/**
 * 
 */
package ca.eandb.jmist.framework.scene;

import java.util.Random;

import ca.eandb.jmist.framework.Bounded3;
import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.random.RandomAdapter;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.GeometryUtil;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnimplementedException;

/**
 * @author Brad
 *
 */
public final class HairSceneElement implements SceneElement {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 7426131707749501794L;

	private final SceneElement emitter;
	
	private final int base;

	private final int amount = 100000;
	
	private final int segments = 4;

	private final Vector3 meanInitialVelocity = new Vector3(0.0, 0.0, 0.2);
	
	private final double randomInitialVelocity = 0.07;
	
	private final double roughness = 0.01;
	
	private final boolean renderEmitter = false;
	
	private final double baseWidth = 0.002;
	
	private final double tipWidth = 0.001;
	
	private class Strand implements Bounded3 {
		
		private Point3[] vertices;
		
		private ShadingContext emitterContext;
		
		private int strandIndex;

		public Box3 boundingBox() {
			BoundingBoxBuilder3 bound = new BoundingBoxBuilder3();
			for (Point3 vertex : vertices) {
				bound.add(vertex);
			}
			return bound.getBoundingBox();
		}

		public Sphere boundingSphere() {
			Box3 box = boundingBox();
			return new Sphere(box.center(), box.diagonal() / 2.0);
		}
		
		public void intersect(final Ray3 ray, IntersectionRecorder recorder) {
			for (int i = 0; i < vertices.length - 3; i++) {
				final double t = GeometryUtil.rayIntersectTriangle(ray, vertices[i], vertices[i+1], vertices[i+2]);
				final int vertexIndex = i;
				if (!Double.isNaN(t)) {
					recorder.record(new Intersection() {
						public double getDistance() {
							return t;
						}
						public double getTolerance() {
							return MathUtil.SMALL_EPSILON;
						}
						public boolean isFront() {
							return true;
						}
						public void prepareShadingContext(ShadingContext context) {
							Plane3 plane = Plane3.throughPoints(vertices[vertexIndex], vertices[vertexIndex+1], vertices[vertexIndex+2]);
							Vector3 n = plane.normal();
							Vector3 v = ray.direction();
							context.setPosition(ray.pointAt(t));
							context.setNormal(v.dot(n) > 0.0 ? n.opposite() : n);
							context.setMaterial(emitterContext.getMaterial());
							context.setModifier(emitterContext.getModifier());
							context.setPrimitiveIndex(base + strandIndex);
							context.setShader(emitterContext.getShader());
							context.setUV(emitterContext.getUV());
							context.setAmbientMedium(emitterContext.getAmbientMedium());
						}
						
					});
				}
			}
		}
		
		public boolean visibility(Ray3 ray) {
			for (int i = 0; i < vertices.length - 3; i++) {
				final double t = GeometryUtil.rayIntersectTriangle(ray, vertices[i], vertices[i+1], vertices[i+1]);
				if (!Double.isNaN(t) && t > MathUtil.SMALL_EPSILON) {
					return false;
				}
			}
			return true;
		}
		
		public boolean intersects(Box3 box) {
			throw new UnimplementedException();
		}
		
	};
	
	public HairSceneElement(SceneElement emitter) {
		this.emitter = emitter;
		this.base = renderEmitter ? emitter.getNumPrimitives() : 0;
	}
	
	private Strand createStrand(int index) {
		Random tempRnd = new Random(index);
		Random rnd = new Random(tempRnd.nextLong());
		RandomAdapter adapter = new RandomAdapter(rnd);
		MinimalShadingContext context = new MinimalShadingContext(adapter);
		emitter.generateRandomSurfacePoint(context, rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble());
		Strand strand = new Strand();
		strand.vertices = new Point3[2 * (segments + 1)];
		strand.emitterContext = context;
		strand.strandIndex = index;
		
		Point3 pos = context.getPosition();
		Vector3 vel = context.getBasis().toStandard(meanInitialVelocity).plus(
				RandomUtil.uniformInsideSphere(randomInitialVelocity, adapter)
						.toCartesian());
		double dt = 1.0 / (double) segments;
		double orientation = 2.0 * Math.PI * rnd.nextDouble();
		double co = Math.cos(orientation);
		double so = Math.sin(orientation);
		Basis3 basis = Basis3.fromWU(vel, context.getTangent());
		
		int segment = 0;
		int i = 0;
		while (true) {
			double t = (double) segment / (double) segments;
			double width = MathUtil.interpolate(baseWidth, tipWidth, t);
			
			strand.vertices[i++] = pos.plus(basis.toStandard(-0.5 * width * co, -0.5 * width * so, 0.0));
			strand.vertices[i++] = pos.plus(basis.toStandard(0.5 * width * co, 0.5 * width * so, 0.0));
			
			if (++segment > segments) {
				break;
			}
			
			pos = pos.plus(vel.times(dt));
			pos = pos.plus(RandomUtil.uniformInsideSphere(roughness, adapter).toCartesian());
		}
		
		return strand;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#createLight()
	 */
	public Light createLight() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context, double ru, double rv,
			double rj) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context, double ru, double rv, double rj) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context,
			double ru, double rv, double rj) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context, double ru,
			double rv, double rj) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		return index < base ? emitter.getBoundingBox(index) : createStrand(index - base).boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		return index < base ? emitter.getBoundingSphere(index) : createStrand(index - base).boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	@Override
	public int getNumPrimitives() {
		return base + amount;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea(int)
	 */
	@Override
	public double getSurfaceArea(int index) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		if (index < base) {
			emitter.intersect(index, ray, recorder);
		} else {
			createStrand(index - base).intersect(ray, recorder);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		// Warning: EXTREMELY SLOW - ALWAYS combine with an accelerator
		for (int i = 0; i < amount; i++) {
			intersect(base + i, ray, recorder);
		}
		if (renderEmitter) {
			emitter.intersect(ray, recorder);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Box3)
	 */
	@Override
	public boolean intersects(int index, Box3 box) {
		return index < base ? emitter.intersects(index, box) : createStrand(
				index - base).intersects(box);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(int index, Ray3 ray) {
		return index < base ? emitter.visibility(index, ray) : createStrand(
				index - base).visibility(ray);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		return emitter.boundingBox().expand(this.meanInitialVelocity.length() + randomInitialVelocity + tipWidth / 2.0);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return emitter.boundingSphere().expand(this.meanInitialVelocity.length() + randomInitialVelocity + tipWidth / 2.0);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(Ray3 ray) {
		// Warning: EXTREMELY SLOW - ALWAYS combine with an accelerator
		for (int i = 0, n = amount; i < n; i++) {
			if (!visibility(base + i, ray)) {
				return false;
			}
		}
		return renderEmitter ? emitter.visibility(ray) : true;
	}
	
	
	
}
