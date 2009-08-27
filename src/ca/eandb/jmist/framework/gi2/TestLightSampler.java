/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public final class TestLightSampler implements LightSampler {

	private final Light light;

	public TestLightSampler(Light light) {
		this.light = light;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.LightSampler#getTotalEmittedPower()
	 */
	public Color getTotalEmittedPower() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.LightSampler#sample(ca.eandb.jmist.framework.gi2.PathInfo)
	 */
	public LightNode sample(PathInfo pathInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.LightSampler#sample(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public LightNode sample(PathNode target) {
		// TODO Auto-generated method stub
		return null;
	}

}
