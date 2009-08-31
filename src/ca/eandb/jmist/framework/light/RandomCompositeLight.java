/**
 *
 */
package ca.eandb.jmist.framework.light;

import java.util.Collection;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.gi2.LightNode;
import ca.eandb.jmist.framework.gi2.PathInfo;
import ca.eandb.jmist.framework.gi2.ScaledLightNode;
import ca.eandb.jmist.framework.random.RandomUtil;

/**
 * @author Brad Kimmel
 *
 */
public final class RandomCompositeLight extends CompositeLight {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 6951460238617706023L;

	public RandomCompositeLight() {
		super();
	}

	public RandomCompositeLight(Collection<? extends Light> children) {
		super(children);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, final Illuminable target) {
		int index = RandomUtil.discrete(0, children().size() - 1, rng);
		children().get(index).illuminate(x, lambda, rng, new Illuminable() {
			public void addLightSample(LightSample sample) {
				target.addLightSample(ScaledLightSample.create(children().size(), sample));
			}
		});
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#sample(ca.eandb.jmist.framework.gi2.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public LightNode sample(PathInfo pathInfo, Random rnd) {
		int index = RandomUtil.discrete(0, children().size() - 1, rnd);
		return ScaledLightNode.create(1.0 / children().size(),
				children().get(index).sample(pathInfo, rnd));
	}

}
