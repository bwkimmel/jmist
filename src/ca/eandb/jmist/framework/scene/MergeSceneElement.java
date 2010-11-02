/**
 * 
 */
package ca.eandb.jmist.framework.scene;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.util.IntegerArray;
import ca.eandb.util.UnimplementedException;

/**
 * A <code>SceneElement</code> that merges one or more child scene elements
 * into a single <code>SceneElement</code>.  The primitives of this
 * <code>SceneElement</code> consist of the primitives of each of the child
 * <code>SceneElement</code>s.
 * @author Brad Kimmel
 */
public final class MergeSceneElement implements SceneElement {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -5677918761822356900L;

	private final List<SceneElement> children = new ArrayList<SceneElement>();
	
	private final IntegerArray offsets = new IntegerArray();
	
	private double surfaceArea = Double.NaN;
	
	private Box3 bbox = null;
	
	public MergeSceneElement addChild(SceneElement child) {
		offsets.add(getNumPrimitives());
		children.add(child);
		surfaceArea = Double.NaN;
		bbox = null;
		return this;
	}
	
	private int getChildIndex(int primIndex) {
		int lo = 0;
		int hi = children.size() - 1;
		int m;
		do {
			m = lo + (hi - lo + 1) / 2;
			if (primIndex >= offsets.get(m)) {
				lo = m;
			} else {
				hi = m - 1;
			}
		} while (lo < hi);
		return lo;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#createLight()
	 */
	@Override
	public Light createLight() {
		throw new UnimplementedException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context, double ru, double rv,
			double rj) {
		int childIndex = getChildIndex(index);
		int childPrimIndex = index - offsets.get(childIndex);
		double weight = children.get(childIndex).generateImportanceSampledSurfacePoint(childPrimIndex, x, context, ru, rv, rj);
		context.setPrimitiveIndex(index);
		return weight;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context, double ru, double rv, double rj) {
		throw new UnimplementedException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context,
			double ru, double rv, double rj) {
		int childIndex = getChildIndex(index);
		int childPrimIndex = index - offsets.get(childIndex);
		children.get(childIndex).generateRandomSurfacePoint(childPrimIndex, context, ru, rv, rj);
		context.setPrimitiveIndex(index);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext, double, double, double)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context, double ru,
			double rv, double rj) {
		throw new UnimplementedException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		int childIndex = getChildIndex(index);
		int childPrimIndex = index - offsets.get(childIndex);
		return children.get(childIndex).getBoundingBox(childPrimIndex);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		int childIndex = getChildIndex(index);
		int childPrimIndex = index - offsets.get(childIndex);
		return children.get(childIndex).getBoundingSphere(childPrimIndex);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	@Override
	public int getNumPrimitives() {
		int numChildren = offsets.size();
		return numChildren > 0 ? offsets.get(numChildren - 1) + children.get(numChildren - 1).getNumPrimitives() : 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea(int)
	 */
	@Override
	public double getSurfaceArea(int index) {
		int childIndex = getChildIndex(index);
		int childPrimIndex = index - offsets.get(childIndex);
		return children.get(childIndex).getSurfaceArea(childPrimIndex);
	}
	
	private synchronized void computeSurfaceArea() {
		if (Double.isNaN(surfaceArea)) {
			double area = 0.0;
			for (SceneElement child : children) {
				area += child.getSurfaceArea();
			}
			surfaceArea = area;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		if (Double.isNaN(surfaceArea)) {
			computeSurfaceArea();
		}
		return surfaceArea;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(final int index, Ray3 ray, IntersectionRecorder recorder) {
		int childIndex = getChildIndex(index);
		int childPrimIndex = index - offsets.get(childIndex);
		children.get(childIndex).intersect(childPrimIndex, ray, new IntersectionRecorderDecorator(recorder) {
			@Override
			public void record(Intersection intersection) {
				inner.record(new IntersectionDecorator(intersection) {
					@Override
					protected void transformShadingContext(
							ShadingContext context) {
						context.setPrimitiveIndex(index);
					}
				});
			}
		});
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		for (int i = 0, n = children.size(); i < n; i++) {
			final int offset = offsets.get(i);
			children.get(i).intersect(ray, new IntersectionRecorderDecorator(recorder) {
				@Override
				public void record(Intersection intersection) {
					inner.record(new IntersectionDecorator(intersection) {
						@Override
						protected void transformShadingContext(
								ShadingContext context) {
							context.setPrimitiveIndex(offset + context.getPrimitiveIndex());
						}
					});
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Box3)
	 */
	@Override
	public boolean intersects(int index, Box3 box) {
		int childIndex = getChildIndex(index);
		int childPrimIndex = index - offsets.get(childIndex);
		return children.get(childIndex).intersects(childPrimIndex, box);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(int index, Ray3 ray) {
		int childIndex = getChildIndex(index);
		int childPrimIndex = index - offsets.get(childIndex);
		return children.get(childIndex).visibility(childPrimIndex, ray);
	}
	
	private synchronized void computeBoundingBox() {
		if (bbox == null) {
			BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
			for (SceneElement child : children) {
				builder.add(child.boundingBox());
			}
			bbox = builder.getBoundingBox();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		if (bbox == null) {
			computeBoundingBox();
		}
		return bbox;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		if (bbox == null) {
			computeBoundingBox();
		}
		return new Sphere(bbox.center(), 0.5 * bbox.diagonal());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(Ray3 ray) {
		for (SceneElement child : children) {
			if (!child.visibility(ray)) {
				return false;
			}
		}
		return true;
	}

}
