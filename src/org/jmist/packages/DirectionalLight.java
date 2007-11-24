/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Illuminable;
import org.jmist.framework.Light;
import org.jmist.framework.Spectrum;
import org.jmist.framework.SurfacePoint;
import org.jmist.framework.VisibilityFunction3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;

/**
 * A <code>Light</code> that illuminates from a specified direction.
 * Equivalent to a point light at an infinite distance.
 * @author bkimmel
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
	 * @see org.jmist.framework.Light#illuminate(org.jmist.framework.SurfacePoint, org.jmist.framework.VisibilityFunction3, org.jmist.framework.Illuminable)
	 */
	@Override
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
