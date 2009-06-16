/**
 *
 */
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.math.RandomUtil;

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
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(Intersection x, final Illuminable target) {
		children().get(this.select()).illuminate(x, new Illuminable() {
			public void addLightSample(LightSample sample) {
				target.addLightSample(ScaledLightSample.create(children().size(), sample));
			}
		});
	}

	private int select() {
		return RandomUtil.discrete(0, this.children().size() - 1, random.next());
	}

	private final Random random;

}
