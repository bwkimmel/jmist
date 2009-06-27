/**
 *
 */
package ca.eandb.jmist.framework.scene;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
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
import ca.eandb.jmist.framework.light.PointLightSample;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.CategoricalRandom;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class MaterialMapSceneElement extends SceneElementDecorator {

	private ByteBuffer map = ByteBuffer.allocate(256);

	private List<Material> materials = new ArrayList<Material>();

	private HashMap<String, Integer> nameLookup = null;

	/**
	 * @param modifier
	 * @param inner
	 */
	public MaterialMapSceneElement(SceneElement inner) {
		super(inner);
	}

	private class MaterialIntersectionRecorder extends IntersectionRecorderDecorator {

		/**
		 * @param inner
		 */
		public MaterialIntersectionRecorder(IntersectionRecorder inner) {
			super(inner);
		}

		/* (non-Javadoc)
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
		if (start + length > map.capacity()) {
			int newSize = Math.min(2 * map.capacity(), start + length);
			ByteBuffer newMap = ByteBuffer.allocate(newSize);
			map.clear();
			newMap.put(map);
			map = newMap;
		}
		map.position(start);
		for (int i = 0; i < length; i++) {
			map.put((byte) materialKey);
		}
	}

	public MaterialMapSceneElement setMaterialRange(int start, int length, String name) {
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		super.intersect(index, ray, new MaterialIntersectionRecorder(recorder));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		super.intersect(ray, new MaterialIntersectionRecorder(recorder));
	}

	@Override
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context) {
		double weight = super.generateImportanceSampledSurfacePoint(index, x, context);
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

	/* (non-Javadoc)
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

		for (int i = 0; i < emissive.size(); i++) {
			primIndex[i] = emissive.get(i);
			weight[i] = super.getSurfaceArea(primIndex[i]); // TODO Factor in radiant exitance of material
		}

		final CategoricalRandom rnd = new CategoricalRandom(weight);

		return new Light() {

			@Override
			public void illuminate(SurfacePoint x, Illuminable target) {

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
					public ColorModel getColorModel() {
						return ColorModel.getInstance();
					}

					@Override
					public double getDistance() {
						return 0;
					}

					@Override
					public Color getImportance() {
						return getColorModel().getWhite();
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

				int index = rnd.next();
				int primitive = primIndex[index];
				double prob = rnd.getProbability(index);

				generateImportanceSampledSurfacePoint(primitive, x, context);
				if (context.getModifier() != null) context.getModifier().modify(context);

				Point3 p = context.getPosition();
				Material mat = context.getMaterial();
				Vector3 v = x.getPosition().unitVectorFrom(p);
				double d2 = x.getPosition().squaredDistanceTo(p);
				double atten = 1.0 / (4.0 * Math.PI * d2 * prob);
				Color ri = mat.emission(context, v).times(atten);

				LightSample sample = new PointLightSample(x, p, ri);

				target.addLightSample(sample);

			}

		};
	}

	private Material lookup(int primIndex) {
		if (primIndex >= 0 && primIndex < map.capacity()) {
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
