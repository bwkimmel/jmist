/**
 *
 */
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.ListScatterRecorder;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Ray3;

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
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
		return shadeRay(ray, 0, lambda.getColorModel().getWhite(lambda));
	}

	private Color shadeRay(Ray3 ray, int depth, Color importance) {

		ColorModel cm = importance.getColorModel();
		WavelengthPacket lambda = importance.getWavelengthPacket();
		Color shade = cm.getBlack(lambda);
		ListScatterRecorder recorder = new ListScatterRecorder();

		Intersection x = caster.castRay(ray);
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
		mat.scatter(x, lambda, recorder);
		boolean specularOnly = true;
		for (ScatteredRay sr : recorder.getScatterResults()) {

			// TODO: Modify ScatteredRayRecorder so that the Material knows that
			// certain types of ScatterResults are unwanted.  This way the
			// material wont waste time generating those scattering events.
			if (sr.getType() == ScatteredRay.Type.SPECULAR) {
				Ray3 reflRay = sr.getRay();
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
			Color direct = light.illuminate(x, lambda, caster);
			shade = shade.plus(direct);

		}

		return shade;
	}

}
