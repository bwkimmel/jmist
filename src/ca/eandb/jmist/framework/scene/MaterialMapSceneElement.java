/**
 *
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
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.ByteArray;

/**
 * @author Brad
 *
 */
public final class MaterialMapSceneElement extends SceneElementDecorator {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 3360256839104414544L;

	private ByteArray map = new ByteArray();

	private List<Material> materials = new ArrayList<Material>();

	private HashMap<String, Integer> nameLookup = null;

	/**
	 * @param modifier
	 * @param inner
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

	public int addMaterial(Material material) {
		if (materials.size() >= 256) {
			throw new IllegalStateException("Material map is full");
		}
		int key = materials.size();
		materials.add(material);
		return key;
	}

	public MaterialMapSceneElement addMaterial(String key, Material material) {
		if (nameLookup == null) {
			nameLookup = new HashMap<String, Integer>();
		}
		nameLookup.put(key, addMaterial(material));
		return this;
	}

	public void setMaterial(int primitive, int materialKey) {
		setMaterialRange(primitive, 1, materialKey);
	}

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

	public MaterialMapSceneElement setMaterialRange(int start, int length,
			String name) {
		if (nameLookup == null || !nameLookup.containsKey(name)) {
			throw new IllegalArgumentException();
		}
		int materialKey = nameLookup.get(name);
		setMaterialRange(start, length, materialKey);
		return this;
	}

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
			SurfacePoint x, ShadingContext context) {
		double weight = super.generateImportanceSampledSurfacePoint(index, x,
				context);
		applyMaterial(context);
		return weight;
	}

	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context) {
		double weight = super.generateImportanceSampledSurfacePoint(x, context);
		applyMaterial(context);
		return weight;
	}

	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context) {
		super.generateRandomSurfacePoint(index, context);
		applyMaterial(context);
	}

	@Override
	public void generateRandomSurfacePoint(ShadingContext context) {
		super.generateRandomSurfacePoint(context);
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

				generateImportanceSampledSurfacePoint(primitive, x, context);
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

			public LightNode sample(PathInfo pathInfo, Random rng) {
				ShadingContext context = new MinimalShadingContext(rng);

				int index = rnd.next(rng);
				int primitive = primIndex[index];

				generateRandomSurfacePoint(primitive, context);
				context.getModifier().modify(context);

				return ScaledLightNode.create(1.0 / totalWeight,
						new SurfaceLightNode(pathInfo, context));
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
