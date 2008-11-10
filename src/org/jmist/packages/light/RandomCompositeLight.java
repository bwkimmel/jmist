/**
 *
 */
package org.jmist.packages.light;

import org.jmist.framework.Illuminable;
import org.jmist.framework.Random;
import org.jmist.framework.Spectrum;
import org.jmist.framework.SurfacePoint;
import org.jmist.framework.VisibilityFunction3;
import org.jmist.packages.random.SimpleRandom;
import org.jmist.packages.spectrum.ScaledSpectrum;
import org.jmist.toolkit.RandomUtil;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public final class RandomCompositeLight extends CompositeLight {

	public RandomCompositeLight(Random random) {
		this.random = random;
	}

	public RandomCompositeLight() {
		this(new SimpleRandom());
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Light#illuminate(org.jmist.framework.SurfacePoint, org.jmist.framework.VisibilityFunction3, org.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
			final Illuminable target) {

		this.children().get(this.select()).illuminate(x, vf, new Illuminable() {

			/* (non-Javadoc)
			 * @see org.jmist.framework.Illuminable#illuminate(org.jmist.toolkit.Vector3, org.jmist.framework.Spectrum)
			 */
			public void illuminate(Vector3 from, Spectrum radiance) {
				target.illuminate(from, new ScaledSpectrum(children().size(), radiance));
			}

		});

	}

	private int select() {
		return RandomUtil.discrete(0, this.children().size() - 1, random.next());
	}

	private final Random random;

}
