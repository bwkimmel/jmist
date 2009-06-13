/**
 *
 */
package ca.eandb.jmist.framework.light;

import java.io.Serializable;

import ca.eandb.jmist.framework.EmissionPoint;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.RandomUtil;
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
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.framework.VisibilityFunction3)
	 */
	public Color illuminate(Intersection x, VisibilityFunction3 vf) {

		if (!this.shadows || vf.visibility(x.location(), this.location)) {

			Vector3		lightIn			= x.location().vectorTo(this.location);
			double		dSquared		= lightIn.squaredLength();
			double		attenuation		= 1.0 / (4.0 * Math.PI * dSquared);

			// normalize light vector
			lightIn = lightIn.divide(Math.sqrt(dSquared));

			Material	m = x.material();
			Color		bsdf = m.scattering(x, lightIn);
			double		cost = lightIn.dot(x.shadingNormal());

			return bsdf.times(emittedPower).times(attenuation * cost);

		}

		return ColorModel.getInstance().getBlack();

	}

	public EmissionPoint emit() {
		Vector3 direction = RandomUtil.uniformOnSphere().toCartesian();
		return VolumeEmissionPoint.create(location, direction, emittedPower, emittedPower.divide(4.0 * Math.PI));
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
