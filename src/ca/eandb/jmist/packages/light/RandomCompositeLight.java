/**
 *
 */
package ca.eandb.jmist.packages.light;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Spectrum;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.packages.random.SimpleRandom;
import ca.eandb.jmist.packages.spectrum.ScaledSpectrum;
import ca.eandb.jmist.toolkit.RandomUtil;
import ca.eandb.jmist.toolkit.Vector3;

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
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.VisibilityFunction3, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
			final Illuminable target) {

		this.children().get(this.select()).illuminate(x, vf, new Illuminable() {

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.Illuminable#illuminate(ca.eandb.jmist.toolkit.Vector3, ca.eandb.jmist.framework.Spectrum)
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
