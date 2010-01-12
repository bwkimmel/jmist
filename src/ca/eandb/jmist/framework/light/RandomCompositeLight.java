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
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.ScaledLightNode;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;

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
	 * @see ca.eandb.jmist.framework.Light#sample(ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public LightNode sample(PathInfo pathInfo, double ru, double rv, double rj) {
		SeedReference ref = new SeedReference(rj);
		int index = RandomUtil.discrete(0, children().size() - 1, ref);
		return ScaledLightNode.create(1.0 / children().size(),
				children().get(index).sample(pathInfo, ru, rv, ref.seed), rj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#getSamplePDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.path.PathInfo)
	 */
	public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
		double pdf = 0.0;
		for (Light light : children()) {
			pdf += light.getSamplePDF(x, pathInfo);
		}
		return pdf / (double) children().size();
	}

}
