/**
 *
 */
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Ray3;

/**
 * A ray shader that shades a ray according to the distance to the
 * nearest intersection along the ray.
 * @author Brad Kimmel
 */
public final class DistanceRayShader implements RayShader {

	/**
	 * Creates a <code>DistanceRayShader</code>.
	 * @param caster The <code>RayCaster</code> to use.
	 */
	public DistanceRayShader(RayCaster caster) {
		this(caster, ColorModel.getInstance().getBlack());
	}

	/**
	 * Creates a <code>DistanceRayShader</code>.
	 * @param caster The <code>RayCaster</code> to use.
	 * @param missValue The <code>Spectrum</code> to assign to rays that do not
	 * 		intersect with any object.
	 */
	public DistanceRayShader(RayCaster caster, Spectrum missValue) {
		this.caster = caster;
		this.missValue = missValue;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
		Intersection x = this.caster.castRay(ray);

		return (x != null) ? lambda.getColorModel().getGray(x.getDistance(), lambda) : missValue.sample(lambda);
	}

	/** The ray caster to use. */
	private final RayCaster caster;

	private final Spectrum missValue;

}
