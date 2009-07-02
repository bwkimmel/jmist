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
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.light.PointLightSample;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

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
		if (!material.isEmissive()) {
			return null;
		}

		return new Light() {

			@Override
			public void illuminate(SurfacePoint x, final WavelengthPacket lambda, Illuminable target) {

				ShadingContext context = new ShadingContext() {

					private Modifier modifier = null;
					private Material material = null;
					private Shader shader = null;
					private Medium medium = null;
					private Point3 position = null;
					private Basis3 basis = null;
					private Basis3 shadingBasis = null;
					private int primitiveIndex = 0;
					private Point2 uv = null;

					@Override
					public Color castRay(ScatteredRay ray) {
						throw new UnsupportedOperationException();
					}

					@Override
					public Color getAmbientLight() {
						return null;
					}

					@Override
					public ColorModel getColorModel() {
						return ColorModel.getInstance();
					}

					@Override
					public double getDistance() {
						return 0;
					}

					@Override
					public Color getImportance() {
						return getColorModel().getWhite(lambda);
					}

					@Override
					public WavelengthPacket getWavelengthPacket() {
						return lambda;
					}

					@Override
					public Vector3 getIncident() {
						return null;
					}

					@Override
					public Iterable<LightSample> getLightSamples() {
						return null;
					}

					@Override
					public Modifier getModifier() {
						return modifier;
					}

					@Override
					public int getPathDepth() {
						return 0;
					}

					@Override
					public int getPathDepthByType(Type type) {
						return 0;
					}

					@Override
					public Ray3 getRay() {
						return null;
					}

					@Override
					public ScatteredRays getScatteredRays() {
						return null;
					}

					@Override
					public Shader getShader() {
						return shader;
					}

					@Override
					public boolean isEyePath() {
						return false;
					}

					@Override
					public boolean isFront() {
						return true;
					}

					@Override
					public boolean isLightPath() {
						return true;
					}

					@Override
					public void setAmbientMedium(Medium medium) {
						this.medium = medium;
					}

					@Override
					public void setBasis(Basis3 basis) {
						this.basis = basis;
					}

					@Override
					public void setMaterial(Material material) {
						this.material = material;
					}

					@Override
					public void setModifier(Modifier modifier) {
						this.modifier = modifier;
					}

					@Override
					public void setNormal(Vector3 normal) {
						this.basis = Basis3.fromW(normal);
					}

					@Override
					public void setPosition(Point3 position) {
						this.position = position;
					}

					@Override
					public void setPrimitiveIndex(int index) {
						this.primitiveIndex = index;
					}

					@Override
					public void setShader(Shader shader) {
						this.shader = shader;
					}

					@Override
					public void setShadingBasis(Basis3 basis) {
						this.shadingBasis = basis;
					}

					@Override
					public void setShadingNormal(Vector3 normal) {
						this.shadingBasis = Basis3.fromW(normal);
					}

					@Override
					public void setUV(Point2 uv) {
						this.uv = uv;
					}

					@Override
					public Color shade() {
						throw new UnsupportedOperationException();
					}

					@Override
					public Medium getAmbientMedium() {
						return medium;
					}

					@Override
					public Basis3 getBasis() {
						return basis;
					}

					@Override
					public Material getMaterial() {
						return material;
					}

					@Override
					public Vector3 getNormal() {
						return basis.w();
					}

					@Override
					public Point3 getPosition() {
						return position;
					}

					@Override
					public int getPrimitiveIndex() {
						return primitiveIndex;
					}

					@Override
					public Basis3 getShadingBasis() {
						return shadingBasis;
					}

					@Override
					public Vector3 getShadingNormal() {
						return shadingBasis.w();
					}

					@Override
					public Vector3 getTangent() {
						return basis.u();
					}

					@Override
					public Point2 getUV() {
						return uv;
					}

					@Override
					public boolean visibility(Ray3 ray, double maximumDistance) {
						throw new UnsupportedOperationException();
					}

					@Override
					public boolean visibility(Ray3 ray) {
						throw new UnsupportedOperationException();
					}

					@Override
					public boolean visibility(Point3 p, Point3 q) {
						throw new UnsupportedOperationException();
					}

				};

				generateImportanceSampledSurfacePoint(x, context);
				context.getModifier().modify(context);

				Point3 p = context.getPosition();
				Material mat = context.getMaterial();
				Vector3 v = x.getPosition().unitVectorFrom(p);
				double d2 = x.getPosition().squaredDistanceTo(p);
				double atten = 1.0 / (4.0 * Math.PI * d2);
				Color ri = mat.emission(context, v, lambda).times(atten);

				LightSample sample = new PointLightSample(x, p, ri);

				target.addLightSample(sample);

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
