/**
 *
 */
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.ListScatterRecorder;
import ca.eandb.jmist.framework.Material;
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
public final class WhittedRayShader implements RayShader {

	private final RayCaster caster;

	private final Light light;

	/**
	 * @param caster
	 * @param light
	 */
	public WhittedRayShader(RayCaster caster, Light light) {
		this.caster = caster;
		this.light = light;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public Color shadeRay(Ray3 ray) {
		return shadeRay(ray, 0, ColorModel.getInstance().getWhite());
	}

	private Color shadeRay(Ray3 ray, int depth, Color importance) {

		Color shade = ColorModel.getInstance().getBlack();
		ListScatterRecorder recorder = new ListScatterRecorder();

		Intersection x = caster.castRay(ray, Interval.POSITIVE);
		if (x == null) {
			return shade;
		}

		Material mat = x.material();

		// Add emission for primary ray
		if (depth == 0 && mat.isEmissive()) {
			shade = shade.plus(mat.emission(x, ray.direction().opposite()));
		}

		// Compute indirect illumination
		recorder.clear();
		mat.scatter(x, recorder);
		boolean specularOnly = true;
		for (ScatterResult sr : recorder.getScatterResults()) {

			// TODO: Modify ScatterRecorder so that the Material knows that
			// certain types of ScatterResults are unwanted.  This way the
			// material wont waste time generating those scattering events.
			if (sr.getType() == ScatterResult.Type.SPECULAR) {
				Ray3 reflRay = sr.getScatteredRay();
				Color reflImp = importance.times(sr.getColor());
				Color reflShade = shadeRay(reflRay, depth + 1, reflImp);
				shade = shade.plus(reflShade.times(sr.getColor()));
			} else {
				specularOnly = false;
			}
		}

		// FIXME: should query material about whether it has a BRDF, rather
		// than basing this decision on whether a non-specular ray was generated
		// by scatter.
		if (!specularOnly) {

			// Compute direct illumination
			IlluminationTarget target = new IlluminationTarget(x);
			light.illuminate(x, caster, target);
			shade = shade.plus(target.getResult());

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
