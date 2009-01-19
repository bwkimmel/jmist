/**
 *
 */
package ca.eandb.jmist.packages.light;

import ca.eandb.jmist.framework.DirectionalTexture3;
import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.toolkit.Basis3;
import ca.eandb.jmist.toolkit.Interval;
import ca.eandb.jmist.toolkit.RandomUtil;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * A <code>Light</code> that illuminates from all directions in the specified
 * hemisphere.
 * @author Brad Kimmel
 */
public final class HemisphericalLight implements Light {

	/**
	 * A <code>DirectionalTexture3</code> representing the incident radiance.
	 */
	private final DirectionalTexture3 environment;

	/**
	 * The <code>Vector3</code> representing the direction at the center of the
	 * illuminating hemisphere.
	 */
	private final Vector3 zenith;

	/** A value indicating whether shadows should be computed. */
	private final boolean shadows;

	/**
	 * Creates a new <code>HemisphericalLight</code>.
	 * @param environment A <code>DirectionalTexture3</code> representing the
	 * 		distribution of incoming radiance.
	 * @param zenith A <code>Vector3</code> indicating the direction toward the
	 * 		center of the illuminating hemisphere.
	 */
	public HemisphericalLight(DirectionalTexture3 environment, Vector3 zenith) {
		this(environment, zenith, true);
	}

	/**
	 * Creates a new <code>HemisphericalLight</code>.
	 * @param environment A <code>DirectionalTexture3</code> representing the
	 * 		distribution of incoming radiance.
	 * @param zenith A <code>Vector3</code> indicating the direction toward the
	 * 		center of the illuminating hemisphere.
	 * @param shadows A value indicating whether shadows should be computed.
	 */
	public HemisphericalLight(DirectionalTexture3 environment, Vector3 zenith, boolean shadows) {
		this.environment = environment;
		this.zenith = zenith;
		this.shadows = shadows;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.VisibilityFunction3, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
			Illuminable target) {

		Vector3	source = RandomUtil.uniformOnUpperHemisphere().toCartesian(Basis3.fromW(zenith));
		Ray3	ray = new Ray3(x.location(), source);

		if (source.dot(x.normal()) > 0.0 && (!shadows || vf.visibility(ray, Interval.POSITIVE))) {
			target.illuminate(source, environment.evaluate(source));
		}

	}

}
