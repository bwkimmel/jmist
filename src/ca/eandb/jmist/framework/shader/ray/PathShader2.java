/**
 *
 */
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.RandomScatterRecorder;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.ScatterResult;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class PathShader2 implements RayShader {

	private final RayCaster caster;

	private final Light light;

	/**
	 * @param caster
	 * @param light
	 */
	public PathShader2(RayCaster caster, Light light) {
		this.caster = caster;
		this.light = light;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public Color shadeRay(Ray3 ray) {

		Color shade = ColorModel.getInstance().getBlack();
		RandomScatterRecorder recorder = new RandomScatterRecorder();
		Color importance = ColorModel.getInstance().getWhite();

		int depth = 0;
		while (true) {
			Intersection x = caster.castRay(ray, Interval.POSITIVE);
			if (x == null) {
				break;
			}

			Material mat = x.material();

			// Add emission for primary ray
			if (depth == 0 && mat.isEmissive()) {
				shade = shade.plus(mat.emission(x, ray.direction().opposite()));
			}

			// Compute direct illumination
			IlluminationTarget target = new IlluminationTarget(x);
			light.illuminate(x, caster, target);
			shade = shade.plus(target.getResult().times(importance));

			// Compute indirect illumination
			mat.scatter(x, recorder);
			ScatterResult sr = recorder.getScatterResult();

			if (sr != null) {
				ray = sr.getScatteredRay();
				importance = importance.times(sr.getColor());
				depth++;
			} else {
				break;
			}
		}

		return shade;
	}

	private static final class IlluminationTarget implements Illuminable {

		private final Intersection x;

		private Color result = ColorModel.getInstance().getBlack();

		/**
		 * @param x
		 */
		public IlluminationTarget(Intersection x) {
			this.x = x;
		}

		/**
		 * @return the result
		 */
		public final Color getResult() {
			return result;
		}

		@Override
		public void illuminate(Vector3 from, Color radiance) {
			Material material = x.material();
			Vector3 normal = x.microfacetNormal();
			double ndotv = from.dot(normal);
			if (ndotv > 0.0) {
				Color brdf = material.scattering(x, from);
				result = result.plus(radiance.times(ndotv).times(brdf));
			}
		}

	}

}
