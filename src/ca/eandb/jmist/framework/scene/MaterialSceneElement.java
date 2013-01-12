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

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.light.AbstractLight;
import ca.eandb.jmist.framework.light.PointLightSample;
import ca.eandb.jmist.framework.modifier.CompositeModifier;
import ca.eandb.jmist.framework.modifier.ShaderModifier;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.ScaledLightNode;
import ca.eandb.jmist.framework.path.SurfaceLightNode;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * A decorator <code>SceneElement</code> that applies a <code>Material</code>
 * to the underlying <code>SceneElement</code>.
 * @see {@link Material}
 * @author Brad Kimmel
 */
public final class MaterialSceneElement extends ModifierSceneElement {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -3086927820777987668L;

	private final Material material;

	public MaterialSceneElement(Material material, SceneElement inner) {
		super(new MaterialModifier(material), inner);
		this.material = material;
	}

	public MaterialSceneElement(Material material, Shader shader, SceneElement inner) {
		super(new CompositeModifier().addModifier(new MaterialModifier(material)).addModifier(new ShaderModifier(shader)), inner);
		this.material = material;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#createLight()
	 */
	@Override
	public Light createLight() {
		if (!material.isEmissive()) {
			return null;
		}

		final double surfaceArea = getSurfaceArea();

		return new AbstractLight() {

			private static final long serialVersionUID = -2578460152471816304L;

			public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, Illuminable target) {

				ShadingContext context = new MinimalShadingContext(rng);
				generateImportanceSampledSurfacePoint(x, context, rng.next(), rng.next(), rng.next());
				context.getModifier().modify(context);

				Point3 p = context.getPosition();
				Material mat = material;//context.getMaterial();
				Vector3 v = x.getPosition().unitVectorFrom(p);
				Vector3 n = context.getShadingNormal();
				double d2 = x.getPosition().squaredDistanceTo(p);
				double atten = Math.max(n.dot(v), 0.0) * surfaceArea / (4.0 * Math.PI * d2);
				Color ri = mat.emission(context, v, lambda).times(atten);

				LightSample sample = new PointLightSample(x, p, ri);

				target.addLightSample(sample);

			}

			public LightNode sample(PathInfo pathInfo, double ru, double rv, double rj) {
				ShadingContext context = new MinimalShadingContext(null);
				generateRandomSurfacePoint(context, ru, rv, rj);
				context.getModifier().modify(context);

				return ScaledLightNode.create(1.0 / surfaceArea,
						new SurfaceLightNode(pathInfo, context, ru, rv, rj), rj);
			}

			public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
				return 1.0 / surfaceArea;
			}

		};
	}

//	/* (non-Javadoc)
//	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#isEmissive()
//	 */
//	@Override
//	public boolean isEmissive() {
//		return material.isEmissive();
//	}

	private static final class MaterialModifier implements Modifier {

		/**
		 * Serialization version ID.
		 */
		private static final long serialVersionUID = -2275096890951731906L;

		private final Material material;

		/**
		 * @param material
		 */
		public MaterialModifier(Material material) {
			this.material = material;
		}

		public void modify(ShadingContext context) {
			context.setMaterial(material);
		}

	}

}
