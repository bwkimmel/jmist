/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
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

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.scene.SceneElementDecorator;
import ca.eandb.jmist.math.Ray3;

/**
 * A <code>SceneElement</code> decorator that flips another <code>SceneElement</code>
 * inside out.
 * @author Brad Kimmel
 */
public final class InsideOutGeometry extends SceneElementDecorator {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -3257320253470920058L;

	/**
	 * Creates a new <code>InsideOutGeometry</code>.
	 * @param inner The <code>SceneElement</code> to turn inside out.
	 */
	public InsideOutGeometry(SceneElement inner) {
		super(inner);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		recorder = new InsideOutIntersectionRecorder(recorder);
		super.intersect(index, ray, recorder);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		recorder = new InsideOutIntersectionRecorder(recorder);
		super.intersect(ray, recorder);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context, double ru, double rv, double rj) {
		double weight = super.generateImportanceSampledSurfacePoint(index, x, context, ru, rv, rj);
		flip(context);
		return weight;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context, double ru, double rv, double rj) {
		double weight = super.generateImportanceSampledSurfacePoint(x, context, ru, rv, rj);
		flip(context);
		return weight;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
		super.generateRandomSurfacePoint(index, context, ru, rv, rj);
		flip(context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
		super.generateRandomSurfacePoint(context, ru, rv, rj);
		flip(context);
	}

	private static void flip(ShadingContext context) {
		context.setBasis(context.getBasis().opposite());
		context.setShadingBasis(context.getShadingBasis().opposite());
	}

	/**
	 * An <code>IntersectionRecorder</code> decorator that flips the normals,
	 * bases, tangents and the {@link Intersection#isFront()} property of all
	 * <code>Intersection</code>s recorded to it.
	 * @author Brad Kimmel
	 */
	private static final class InsideOutIntersectionRecorder extends
			IntersectionRecorderDecorator {

		/**
		 * Creates a new <code>InsideOutIntersectionRecorder</code>.
		 * @param inner The <code>IntersectionRecorder</code> to decorate.
		 */
		public InsideOutIntersectionRecorder(IntersectionRecorder inner) {
			super(inner);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#record(ca.eandb.jmist.framework.Intersection)
		 */
		@Override
		public void record(Intersection intersection) {
			inner.record(new InsideOutIntersection(intersection));
		}

		/**
		 * An <code>Intersection</code> decorator that flips the normals,
		 * bases, tangents, and the {@link Intersection#isFront()} properties of
		 * the decorated <code>Intersection</code>.
		 * @author Brad Kimmel
		 */
		private static final class InsideOutIntersection extends IntersectionDecorator {

			/**
			 * Creates a new <code>InsideOutIntersection</code>.
			 * @param inner The decorated <code>Intersection</code>.
			 */
			public InsideOutIntersection(Intersection inner) {
				super(inner);
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionDecorator#front()
			 */
			@Override
			public boolean isFront() {
				return !inner.isFront();
			}

			@Override
			protected void transformShadingContext(ShadingContext context) {
				flip(context);
			}

		}

	}

}
