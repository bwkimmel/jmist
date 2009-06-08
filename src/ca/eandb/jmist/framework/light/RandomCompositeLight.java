/**
 *
 */
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.math.RandomUtil;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
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
			 * @see ca.eandb.jmist.framework.Illuminable#illuminate(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.Color)
			 */
			public void illuminate(Vector3 from, Color radiance) {
				target.illuminate(from, radiance.times(children().size()));
			}

		});

	}

	private int select() {
		return RandomUtil.discrete(0, this.children().size() - 1, random.next());
	}

	private final Random random;

}
