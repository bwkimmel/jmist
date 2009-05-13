/**
 *
 */
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Spectrum;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Ray3;
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
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.VisibilityFunction3, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf, Illuminable target) {

		if (this.shadows) {

			Ray3 shadowRay = new Ray3(x.location(), this.from);

			if (!vf.visibility(shadowRay, Interval.POSITIVE)) {
				return;
			}

		}

		target.illuminate(this.from, this.irradiance);

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
