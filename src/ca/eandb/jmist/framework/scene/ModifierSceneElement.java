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

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.math.Ray3;

/**
 * @author brad
 *
 */
public class ModifierSceneElement extends SceneElementDecorator {

	private final Modifier modifier;

	/**
	 * @param inner
	 */
	public ModifierSceneElement(Modifier modifier, SceneElement inner) {
		super(inner);
		this.modifier = modifier;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		super.intersect(index, ray, new ModifierIntersectionRecorder(recorder));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		super.intersect(ray, new ModifierIntersectionRecorder(recorder));
	}

	private final class ModifierIntersectionRecorder extends
			IntersectionRecorderDecorator {
		private ModifierIntersectionRecorder(IntersectionRecorder inner) {
			super(inner);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#record(ca.eandb.jmist.framework.Intersection)
		 */
		@Override
		public void record(Intersection intersection) {
			inner.record(new IntersectionDecorator(intersection) {
				protected void transformShadingContext(
						ShadingContext context) {
					modifier.modify(context);
				}
			});
		}
	}

}
