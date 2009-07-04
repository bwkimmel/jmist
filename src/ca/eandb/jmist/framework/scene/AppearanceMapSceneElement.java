/**
 *
 */
package ca.eandb.jmist.framework.scene;

import java.nio.ByteBuffer;
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
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.Random;
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
import ca.eandb.jmist.math.CategoricalRandom;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class AppearanceMapSceneElement extends SceneElementDecorator {

	private ByteBuffer map = ByteBuffer.allocate(256);

	private List<Appearance> app = new ArrayList<Appearance>();

	private HashMap<String, Integer> nameLookup = null;

	/**
	 * @param modifier
	 * @param inner
	 */
	public AppearanceMapSceneElement(SceneElement inner) {
		super(inner);
	}

	private static final class Appearance {
		public final Material material;
		public final Shader shader;

		public Appearance(Material material, Shader shader) {
			this.material = material;
			this.shader = shader;
		}
	}

	private class AppearanceIntersectionRecorder extends IntersectionRecorderDecorator {

		/**
		 * @param inner
		 */
		public AppearanceIntersectionRecorder(IntersectionRecorder inner) {
			super(inner);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#record(ca.eandb.jmist.framework.Intersection)
		 */
		@Override
		public void record(Intersection intersection) {
			inner.record(new IntersectionDecorator(intersection) {
				protected void transformShadingContext(ShadingContext context) {
					applyAppearance(context);
				}
			});
		}

	}

	public int addAppearance(Material material, Shader shader) {
		if (app.size() >= 256) {
			throw new IllegalStateException("Material/Shader map is full");
		}
		int key = app.size();
		app.add(new Appearance(material, shader));
		return key;
	}

	public AppearanceMapSceneElement addAppearance(String key, Material material, Shader shader) {
		if (nameLookup == null) {
			nameLookup = new HashMap<String, Integer>();
		}
		nameLookup.put(key, addAppearance(material, shader));
		return this;
	}

	public void setAppearance(int primitive, int key) {
		setAppearanceRange(primitive, 1, key);
	}

	public void setAppearanceRange(int start, int length, int key) {
		if (key < 0 || key >= app.size()) {
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
			map.put((byte) key);
		}
	}

	public AppearanceMapSceneElement setAppearanceRange(int start, int length, String name) {
		if (nameLookup == null || !nameLookup.containsKey(name)) {
			throw new IllegalArgumentException();
		}
		int key = nameLookup.get(name);
		setAppearanceRange(start, length, key);
		return this;
	}

	public AppearanceMapSceneElement setAppearance(int primitive, String name) {
		return setAppearanceRange(primitive, 1, name);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		super.intersect(index, ray, new AppearanceIntersectionRecorder(recorder));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		super.intersect(ray, new AppearanceIntersectionRecorder(recorder));
	}

	@Override
	public double generateImportanceSampledSurfacePoint(int index,
			SurfacePoint x, ShadingContext context) {
		double weight = super.generateImportanceSampledSurfacePoint(index, x, context);
		applyAppearance(context);
		return weight;
	}

	@Override
	public double generateImportanceSampledSurfacePoint(SurfacePoint x,
			ShadingContext context) {
		double weight = super.generateImportanceSampledSurfacePoint(x, context);
		applyAppearance(context);
		return weight;
	}

	@Override
	public void generateRandomSurfacePoint(int index, ShadingContext context) {
		super.generateRandomSurfacePoint(index, context);
		applyAppearance(context);
	}

	@Override
	public void generateRandomSurfacePoint(ShadingContext context) {
		super.generateRandomSurfacePoint(context);
		applyAppearance(context);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#createLight()
	 */
	@Override
	public Light createLight() {

		int numPrim = super.getNumPrimitives();
		ArrayList<Integer> emissive = new ArrayList<Integer>();
		for (int i = 0; i < numPrim; i++) {
			if (lookup(i).material.isEmissive()) {
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
			weight[i] = super.getSurfaceArea(primIndex[i]); // TODO Factor in radiant exitance of material
			totalSurfaceArea += weight[i];
		}

		final double scale = totalSurfaceArea / emissive.size();

		final CategoricalRandom rnd = new CategoricalRandom(weight);

		return new Light() {

			@Override
			public void illuminate(SurfacePoint x, final WavelengthPacket lambda, final Random rng, Illuminable target) {

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
						return lambda.getColorModel();
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
					public Random getRandom() {
						return rng;
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

				int index = rnd.next(rng.next());
				int primitive = primIndex[index];

				generateImportanceSampledSurfacePoint(primitive, x, context);
				if (context.getModifier() != null) context.getModifier().modify(context);

				Point3 p = context.getPosition();
				Material mat = context.getMaterial();
				Vector3 v = x.getPosition().unitVectorFrom(p);
				double d2 = x.getPosition().squaredDistanceTo(p);
				double atten = scale / (4.0 * Math.PI * d2);
				Color ri = mat.emission(context, v, lambda).times(atten);

				LightSample sample = new PointLightSample(x, p, ri);

				target.addLightSample(sample);

			}

		};
	}

	private Appearance lookup(int primIndex) {
		if (primIndex >= 0 && primIndex < map.capacity()) {
			int matIndex = (int) map.get(primIndex);
			if (0 <= matIndex && matIndex < app.size()) {
				return app.get(matIndex);
			}
		}
		return null;
	}

	/**
	 * @param context
	 */
	private void applyAppearance(ShadingContext context) {
		int primIndex = context.getPrimitiveIndex();
		Appearance a = lookup(primIndex);
		if (a != null) {
			context.setMaterial(a.material);
			context.setShader(a.shader);
		}
	}


}
