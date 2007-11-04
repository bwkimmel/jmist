/**
 *
 */
package org.jmist.packages;

import java.io.Serializable;

import org.jmist.framework.Light;
import org.jmist.framework.Spectrum;
import org.jmist.framework.SurfacePoint;
import org.jmist.framework.VisibilityFunction3;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Vector3;

/**
 * A <code>Light</code> that emits from a single point.
 * @author bkimmel
 */
public final class PointLight implements Light, Serializable {

	/**
	 * Creates a new <code>PointLight</code>.
	 * @param location The <code>Point3</code> where the light is to emit from.
	 * @param emission The emission <code>Spectrum</code> of the light.
	 * @param shadows A value indicating whether the light should be affected
	 * 		by shadows.
	 */
	public PointLight(Point3 location, Spectrum emission, boolean shadows) {
		this.location = location;
		this.emission = emission;
		this.shadows = shadows;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Light#illuminate(org.jmist.framework.SurfacePoint, org.jmist.framework.VisibilityFunction3)
	 */
	@Override
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf) {

		if (!this.shadows || vf.visibility(x.location(), this.location)) {

			Vector3		from			= x.location().vectorTo(this.location);
			double		dSquared		= from.squaredLength();
			double		attenuation		= 1.0 / (4.0 * Math.PI * dSquared);

			x.illuminate(from.unit(), new ScaledSpectrum(attenuation, emission));

		}

	}

	/** The <code>Point3</code> where the light is to emit from. */
	private final Point3 location;

	/** The emission <code>Spectrum</code> of the light. */
	private final Spectrum emission;

	/** A value indicating whether the light should be affected by shadows. */
	private final boolean shadows;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -5220350307274318220L;

}
