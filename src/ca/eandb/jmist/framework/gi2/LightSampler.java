/**
 *
 */
package ca.eandb.jmist.framework.gi2;

/**
 * @author Brad
 *
 */
public interface LightSampler {

	LightNode sample();

	LightNode sample(PathNode target);

}
