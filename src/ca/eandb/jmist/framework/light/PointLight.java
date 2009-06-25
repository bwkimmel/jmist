/**
 *
 */
package ca.eandb.jmist.framework.light;

import java.io.Serializable;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Light</code> that emits from a single point.
 * @author Brad Kimmel
 */
public final class PointLight implements Light, Serializable {

	/**
	 * Creates a new <code>PointLight</code>.
	 * @param location The <code>Point3</code> where the light is to emit from.
	 * @param emission The <code>Color</code> representing the emitted power of
	 * 		the light.
	 * @param shadows A value indicating whether the light should be affected
	 * 		by shadows.
	 */
	public PointLight(Point3 location, Color emittedPower, boolean shadows) {
		this.location = location;
		this.emittedPower = emittedPower;
		this.shadows = shadows;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, Illuminable target) {

		Vector3		lightIn			= x.getPosition().vectorTo(this.location);
		double		dSquared		= lightIn.squaredLength();

		lightIn = lightIn.divide(Math.sqrt(dSquared));

		double		ndotl			= x.getShadingNormal().dot(lightIn);
		double		attenuation		= ndotl / (4.0 * Math.PI * dSquared);

		target.addLightSample(new PointLightSample(x, location, emittedPower.times(attenuation), shadows));

	}

	/** The <code>Point3</code> where the light is to emit from. */
	private final Point3 location;

	/** The emission <code>Color</code> of the light. */
	private final Color emittedPower;

	/** A value indicating whether the light should be affected by shadows. */
	private final boolean shadows;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -5220350307274318220L;

}
