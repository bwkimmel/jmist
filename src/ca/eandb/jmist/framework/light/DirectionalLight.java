/**
 *
 */
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.color.Color;
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
	 * @param irradiance The irradiance <code>Color</code>.
	 * @param shadows A value indicating whether shadows should be applied.
	 */
	public DirectionalLight(Vector3 from, Color irradiance, boolean shadows) {
		this.from = from.unit();
		this.irradiance = irradiance;
		this.shadows = shadows;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(Intersection x, Illuminable target) {
		double dot = x.getShadingNormal().dot(from);
		target.addLightSample(new DirectionalLightSample(x, from, irradiance.times(dot), shadows));
	}

	/**
	 * The <code>Vector3</code> indicating the direction from which the light
	 * originates.
	 */
	private final Vector3 from;

	/** The irradiance <code>Color</code>. */
	private final Color irradiance;

	/** A value indicating whether shadows should be applied. */
	private final boolean shadows;

}
