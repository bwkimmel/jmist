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
package ca.eandb.jmist.framework.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.light.AbstractLight;
import ca.eandb.jmist.framework.light.PointLightSample;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.ScaledLightNode;
import ca.eandb.jmist.framework.path.SurfaceLightNode;
import ca.eandb.jmist.framework.random.CategoricalRandom;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.ByteArray;

/**
 * A decorator <code>SceneElement</code> that applies <code>Material</code>s to
 * the primitives of the underlying <code>SceneElement</code> according to a
 * mapping from primitive index to <code>Material</code>.
 * @see {@link Material}
 * @author Brad Kimmel
 */
public final class MaterialMapSceneElement extends SceneElementDecorator {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 3360256839104414544L;

	/**
	 * Indicates what material to use for each primitive.  The index into this
	 * array represents the primitive index.  The value represents an index
	 * into {@link #materials}.
	 */
	private ByteArray map = new ByteArray();

	/** The <code>List</code> of <code>Material</code>s to apply. */
	private List<Material> materials = new ArrayList<Material>();

	/** Lookup for materials by name. */
	private HashMap<String, Integer> nameLookup = null;

	/**
	 * Creates a <code>MaterialMapSceneElement</code>.
	 * @param inner The underlying <code>SceneElement</code>.
	 */
	public MaterialMapSceneElement(SceneElement inner) {
		super(inner);
	}

	private class MaterialIntersectionRecorder extends
			IntersectionRecorderDecorator {

		/**
		 * @param inner
		 */
		public MaterialIntersectionRecorder(IntersectionRecorder inner) {
			super(inner);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#record(ca.eandb.jmist.framework.Intersection)
		 */
		@Override
		public void record(Intersection intersection) {
			inner.record(new IntersectionDecorator(intersection) {
				protected void transformShadingContext(ShadingContext context) {
					applyMaterial(context);
				}
			});
		}

	}

	/**
	 * Adds a <code>Material</code>.
	 * @param material The <code>Material</code> to add.
	 * @return The ID to refer to this material by.
	 */
	public int addMaterial(Material material) {
		if (materials.size() >= 256) {
			throw new IllegalStateException("Material map is full");
		}
		int key = materials.size();
		materials.add(material);
		return key;
	}

	/**
	 * Adds a named <code>Material</code>.
	 * @param key The name to assign to the material.
	 * @param material The <code>Material</code> to add.
	 * @return A reference to <code>this</code> for method chaining.
	 */
	public MaterialMapSceneElement addMaterial(String key, Material material) {
		if (nameLookup == null) {
			nameLookup = new HashMap<String, Integer>();
		}
		nameLookup.put(key, addMaterial(material));
		return this;
	}

	/**
	 * Sets the material to apply to a given primitive.
	 * @param primitive The ID of the primitive.
	 * @param materialKey The ID of the material (as returned by
	 * 		{@link #addMaterial(Material)}).
	 */
	public void setMaterial(int primitive, int materialKey) {
		setMaterialRange(primitive, 1, materialKey);
	}

	/**
	 * Sets the material to apply to a range of primitives.
	 * @param start The ID of the first primitive.
	 * @param length The number of primitives to apply the material to.
	 * @param materialKey The ID of the material (as returned by
	 * 		{@link #addMaterial(Material)}).
	 */
	public void setMaterialRange(int start, int length, int materialKey) {
		if (materialKey < 0 || materialKey >= materials.size()) {
			throw new IllegalArgumentException();
		}
		if (map.size() < start + length) {
			map.resize(start + length);
		}
		for (int i = 0; i < length; i++) {
			map.set(start++, (byte) materialKey);
		}
	}

	/**
	 * Sets the material to apply to a range of primitives.
	 * @param start The ID of the first primitive.
	 * @param length The number of primitives to apply the material to.
	 * @param name The name of the material (as provided to
	 * 		{@link #addMaterial(String, Material)}).
	 */
	public MaterialMapSceneElement setMaterialRange(int start, int length,
			String name) {
		if (nameLookup == null || !nameLookup.containsKey(name)) {
			throw new IllegalArgumentException();
		}
		int materialKey = nameLookup.get(name);
		setMaterialRange(start, length, materialKey);
		return this;
	}

	/**
	 * Sets the material to apply to a given primitive.
	 * @param primitive The ID of the primitive.
	 * @param name The name of the material (as provided to
	 * 		{@link #addMaterial(String, Material)}).
	 */
	public MaterialMapSceneElement setMaterial(int primitive, String name) {
		return setMaterialRange(primitive, 1, name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(int,
	 *      ca.eandb.jmist.math.Ray3,
	 *      ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		super.intersect(index, ray, new MaterialIntersectionRecorder(recorder));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3,
	 *      ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		super.intersect(ray, new MaterialIntersectionRecorder(recorder));
	}

	@Override
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context, double ru, double rv, double rj) {
		double weight = super.generateImportanceSampledSurfacePoint(index, x,
				context, ru, rv, rj);
		applyMaterial(context);
		return weight;
	}

	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context, double ru, double rv, double rj) {
		double weight = super.generateImportanceSampledSurfacePoint(x, context, ru, rv, rj);
		applyMaterial(context);
		return weight;
	}

	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
		super.generateRandomSurfacePoint(index, context, ru, rv, rj);
		applyMaterial(context);
	}

	@Override
	public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
		super.generateRandomSurfacePoint(context, ru, rv, rj);
		applyMaterial(context);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#createLight()
	 */
	@Override
	public Light createLight() {

		int numPrim = super.getNumPrimitives();
		ArrayList<Integer> emissive = new ArrayList<Integer>();
		for (int i = 0; i < numPrim; i++) {
			if (lookup(i).isEmissive()) {
				emissive.add(i);
			}
		}

		if (emissive.isEmpty()) {
			return null;
		}

		final int[] primIndex = new int[emissive.size()];
		final double[] weight = new double[emissive.size()];
		double totalSurfaceArea = 0.0;

		for (int i = 0; i < emissive.size(); i++) {
			primIndex[i] = emissive.get(i);
			weight[i] = super.getSurfaceArea(primIndex[i]); // TODO Factor in
															// radiant exitance
															// of material
			totalSurfaceArea += weight[i];
		}

		final double scale = totalSurfaceArea / emissive.size();
		final double totalWeight = totalSurfaceArea;
		final CategoricalRandom rnd = new CategoricalRandom(weight);

		return new AbstractLight() {

			private static final long serialVersionUID = -4977755592893506132L;

			public void illuminate(SurfacePoint x, WavelengthPacket lambda,
					Random rng, Illuminable target) {
				ShadingContext context = new MinimalShadingContext(rng);

				int index = rnd.next(rng);
				int primitive = primIndex[index];

				generateImportanceSampledSurfacePoint(primitive, x, context, rng.next(), rng.next(), rng.next());
				context.getModifier().modify(context);

				Point3 p = context.getPosition();
				Material mat = context.getMaterial();
				Vector3 v = x.getPosition().unitVectorFrom(p);
				Vector3 n = context.getShadingNormal();
				double d2 = x.getPosition().squaredDistanceTo(p);
				double atten = Math.max(n.dot(v), 0.0) * totalWeight
						/ (4.0 * Math.PI * d2);
				Color ri = mat.emission(context, v, lambda).times(atten);
				LightSample sample = new PointLightSample(x, p, ri);

				target.addLightSample(sample);
			}

			public LightNode sample(PathInfo pathInfo, double ru, double rv, double rj) {
				ShadingContext context = new MinimalShadingContext(null);

				SeedReference ref = new SeedReference(rj);
				int index = rnd.next(ref);
				int primitive = primIndex[index];

				generateRandomSurfacePoint(primitive, context, ru, rv, ref.seed);
				context.getModifier().modify(context);

				return ScaledLightNode.create(1.0 / totalWeight,
						new SurfaceLightNode(pathInfo, context, ru, rv, ref.seed), rj);
			}

			public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
				return 1.0 / totalWeight;
			}

		};

	}

	private Material lookup(int primIndex) {
		if (primIndex >= 0 && primIndex < map.size()) {
			int matIndex = (int) map.get(primIndex);
			if (0 <= matIndex && matIndex < materials.size()) {
				return materials.get(matIndex);
			}
		}
		return null;
	}

	/**
	 * @param context
	 */
	private void applyMaterial(ShadingContext context) {
		int primIndex = context.getPrimitiveIndex();
		Material mat = lookup(primIndex);
		if (mat != null) {
			context.setMaterial(mat);
		}
	}

}
