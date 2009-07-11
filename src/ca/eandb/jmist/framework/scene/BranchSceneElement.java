/*
 * Copyright (c) 2008 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
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
import ca.eandb.jmist.framework.light.RandomCompositeLight;
import ca.eandb.jmist.framework.random.CategoricalRandom;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author brad
 *
 */
public final class BranchSceneElement implements SceneElement {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -8500645819577622768L;

	private final List<SceneElement> children = new ArrayList<SceneElement>();

	private CategoricalRandom rnd = null;

	public BranchSceneElement addChild(SceneElement child) {
		children.add(child);
		return this;
	}

	private synchronized void buildChildSelector() {
		if (rnd != null) {
			return;
		}

		double[] weight = new double[children.size()];
		for (int i = 0; i < weight.length; i++) {
			weight[i] = children.get(i).getSurfaceArea();
		}
		rnd = new CategoricalRandom(weight);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#createLight()
	 */
	@Override
	public Light createLight() {
		List<Light> lights = new ArrayList<Light>();
		for (SceneElement child : children) {
			Light light = child.createLight();
			if (light != null) {
				lights.add(light);
			}
		}
		switch (lights.size()) {
		case 0:
			return null;
		case 1:
			return lights.get(0);
		default:
			return new RandomCompositeLight(lights);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context) {
		double weight = children.get(index).generateImportanceSampledSurfacePoint(x, context);
		context.setPrimitiveIndex(index);
		return weight;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context) {
		if (rnd == null) {
			buildChildSelector();
		}
		return generateImportanceSampledSurfacePoint(rnd.next(context.getRandom()), x, context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context) {
		children.get(index).generateRandomSurfacePoint(context);
		context.setPrimitiveIndex(index);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context) {
		if (rnd == null) {
			buildChildSelector();
		}
		generateRandomSurfacePoint(rnd.next(context.getRandom()), context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		return children.get(index).boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		return children.get(index).boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	@Override
	public int getNumPrimitives() {
		return children.size();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea(int)
	 */
	@Override
	public double getSurfaceArea(int index) {
		return children.get(index).getSurfaceArea();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea()
	 */
	@Override
	public double getSurfaceArea() {
		double area = 0.0;
		for (SceneElement child : children) {
			area += child.getSurfaceArea();
		}
		return area;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(final int index, Ray3 ray, IntersectionRecorder recorder) {
		children.get(index).intersect(ray, new IntersectionRecorderDecorator(recorder) {
			public void record(Intersection intersection) {
				inner.record(new IntersectionDecorator(intersection) {
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
		for (int i = 0; i < children.size(); i++) {
			intersect(i, ray, recorder);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Box3)
	 */
	@Override
	public boolean intersects(int index, Box3 box) {
		SceneElement child = children.get(index);
		int n = child.getNumPrimitives();
		for (int i = 0; i < n; i++) {
			if (child.intersects(i, box)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3, double)
	 */
	@Override
	public boolean visibility(int index, Ray3 ray, double maximumDistance) {
		return children.get(index).visibility(ray, maximumDistance);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(ca.eandb.jmist.math.Ray3, double)
	 */
	@Override
	public boolean visibility(Ray3 ray, double maximumDistance) {
		for (int i = 0; i < children.size(); i++) {
			if (!visibility(i, ray, maximumDistance)) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(int index, Ray3 ray) {
		return children.get(index).visibility(ray);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(Ray3 ray) {
		for (int i = 0; i < children.size(); i++) {
			if (!visibility(i, ray)) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
		for (SceneElement child : children) {
			builder.add(child.boundingBox());
		}
		return builder.getBoundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		Box3 boundingBox = boundingBox();
		return new Sphere(boundingBox.center(), boundingBox.diagonal() / 2.0);
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

}
