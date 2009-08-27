/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public interface LightSampler {

	LightNode sample(PathInfo pathInfo);

	LightNode sample(PathNode target);

	Color getTotalEmittedPower();

}
