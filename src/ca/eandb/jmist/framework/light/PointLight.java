/**
 *
 */
package ca.eandb.jmist.framework.light;

import java.io.Serializable;

import ca.eandb.jmist.framework.Emitter;
import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Light</code> that emits from a single point.
 * @author Brad Kimmel
 */
public final class PointLight extends AbstractLight implements Serializable {

	/**
	 * Creates a new <code>PointLight</code>.
	 * @param location The <code>Point3</code> where the light is to emit from.
	 * @param emission The <code>Spectrum</code> representing the emitted power
	 * 		of the light.
	 * @param shadows A value indicating whether the light should be affected
	 * 		by shadows.
	 */
	public PointLight(Point3 location, Spectrum emittedPower, boolean shadows) {
		this.location = location;
		this.emittedPower = emittedPower;
		this.shadows = shadows;
		this.emitter = new PointEmitter(location, emittedPower);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, Illuminable target) {

		Vector3		lightIn			= x.getPosition().vectorTo(this.location);
		double		dSquared		= lightIn.squaredLength();

		lightIn = lightIn.divide(Math.sqrt(dSquared));

		double		ndotl			= x.getShadingNormal().dot(lightIn);
		double		attenuation		= ndotl / (4.0 * Math.PI * dSquared);

		target.addLightSample(new PointLightSample(x, location, emittedPower.sample(lambda).times(attenuation), shadows));

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#sample(ca.eandb.jmist.framework.Random)
	 */
	@Override
	public Emitter sample(Random rng) {
		return emitter;
	}

	/** The <code>Point3</code> where the light is to emit from. */
	private final Point3 location;

	/** The emission <code>Spectrum</code> of the light. */
	private final Spectrum emittedPower;

	/** The <code>Emitter</code> for this light source. */
	private final Emitter emitter;

	/** A value indicating whether the light should be affected by shadows. */
	private final boolean shadows;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -5220350307274318220L;

}
