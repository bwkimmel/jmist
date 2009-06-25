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

import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
public final class MaterialSceneElement extends ModifierSceneElement {

	private final Material material;

	public MaterialSceneElement(Material material, SceneElement inner) {
		super(new MaterialModifier(material), inner);
		this.material = material;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#createLight()
	 */
	@Override
	public Light createLight() {
		throw new UnimplementedException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#isEmissive()
	 */
	@Override
	public boolean isEmissive() {
		return material.isEmissive();
	}

	private static final class MaterialModifier implements Modifier {

		private final Material material;

		/**
		 * @param material
		 */
		public MaterialModifier(Material material) {
			this.material = material;
		}

		@Override
		public void modify(ShadingContext context) {
			context.setMaterial(material);
		}

	}

}
