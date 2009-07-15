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

package ca.eandb.jmist.framework.geometry;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author brad
 *
 */
public final class PrimitiveListGeometry extends AbstractGeometry {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -7468724116325274242L;

	private final List<PrimitiveGeometry> primitives = new ArrayList<PrimitiveGeometry>();

	public PrimitiveListGeometry addPrimitive(PrimitiveGeometry primitive) {
		primitives.add(primitive);
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	public Box3 getBoundingBox(int index) {
		return primitives.get(index).boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	public Sphere getBoundingSphere(int index) {
		return primitives.get(index).boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	public int getNumPrimitives() {
		return primitives.size();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(final int index, Ray3 ray, IntersectionRecorder recorder) {
		primitives.get(index).intersect(ray, new IntersectionRecorderDecorator(recorder) {
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
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
		for (PrimitiveGeometry primitive : primitives) {
			builder.add(primitive.boundingBox());
		}
		return builder.getBoundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		Box3 box = boundingBox();
		return new Sphere(box.center(), 0.5 * box.diagonal());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#getSurfaceArea(int)
	 */
	@Override
	public double getSurfaceArea(int index) {
		return primitives.get(index).getSurfaceArea();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.AbstractGeometry#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context) {
		primitives.get(index).generateRandomSurfacePoint(context);
		context.setPrimitiveIndex(index);
	}

}
