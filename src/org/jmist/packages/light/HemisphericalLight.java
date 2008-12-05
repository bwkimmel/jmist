/**
 *
 */
package org.jmist.packages.light;

import org.jmist.framework.DirectionalTexture3;
import org.jmist.framework.Illuminable;
import org.jmist.framework.Light;
import org.jmist.framework.SurfacePoint;
import org.jmist.framework.VisibilityFunction3;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.RandomUtil;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;

/**
 * A <code>Light</code> that illuminates from all directions in the specified
 * hemisphere.
 * @author brad
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
	 * @see org.jmist.framework.Light#illuminate(org.jmist.framework.SurfacePoint, org.jmist.framework.VisibilityFunction3, org.jmist.framework.Illuminable)
	 */
	@Override
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
			Illuminable target) {

		Vector3	source = RandomUtil.uniformOnUpperHemisphere().toCartesian(Basis3.fromW(zenith));
		Ray3	ray = new Ray3(x.location(), source);

		if (source.dot(x.normal()) > 0.0 && (!shadows || vf.visibility(ray, Interval.POSITIVE))) {
			target.illuminate(source, environment.evaluate(source));
		}

	}

}
