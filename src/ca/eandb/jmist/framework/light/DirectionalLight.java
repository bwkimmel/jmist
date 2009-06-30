/**
 *
 */
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Light</code> that illuminates from a specified direction.
 * Equivalent to a point light at an infinite distance.
 * @author Brad Kimmel
 */
public final class DirectionalLight implements Light {

	/**
	 * Creates a new <code>DirectionalLight</code>.
	 * @param from The <code>Vector3</code> indicating the direction from which
	 * 		the light originates.
	 * @param irradiance The irradiance <code>Spectrum</code>.
	 * @param shadows A value indicating whether shadows should be applied.
	 */
	public DirectionalLight(Vector3 from, Spectrum irradiance, boolean shadows) {
		this.from = from.unit();
		this.irradiance = irradiance;
		this.shadows = shadows;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, WavelengthPacket lambda, Illuminable target) {
		double dot = x.getShadingNormal().dot(from);
		target.addLightSample(new DirectionalLightSample(x, from, irradiance.sample(lambda).times(dot), shadows));
	}

	/**
	 * The <code>Vector3</code> indicating the direction from which the light
	 * originates.
	 */
	private final Vector3 from;

	/** The irradiance <code>Spectrum</code>. */
	private final Spectrum irradiance;

	/** A value indicating whether shadows should be applied. */
	private final boolean shadows;

}
