/**
 *
 */
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.IntersectionGeometry;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.RandomScatterRecorder;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad
 *
 */
public final class PathShader implements RayShader {

	private final RayCaster caster;

	private final Light light;

	/**
	 * @param caster
	 * @param light
	 */
	public PathShader(RayCaster caster, Light light) {
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
			IntersectionGeometry x = caster.castRay(ray);
			if (x == null) {
				break;
			}

			Material mat = x.material();

			// Add emission for primary ray
			if (depth == 0 && mat.isEmissive()) {
				shade = shade.plus(mat.emission(x, ray.direction().opposite()));
			}

			// Compute direct illumination
			Color direct = light.illuminate(x, caster);
			shade = shade.plus(direct.times(importance));

			// Compute indirect illumination
			mat.scatter(x, recorder);
			ScatteredRay sr = recorder.getScatterResult();

			if (sr != null) {
				ray = sr.getRay();
				importance = importance.times(sr.getColor());
				depth++;
			} else {
				break;
			}
		}

		return shade;
	}

}
