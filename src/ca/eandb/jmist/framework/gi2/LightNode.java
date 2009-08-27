/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public interface LightNode extends PathNode {

	Color getRadiantExitance();

	Color getRadiance(PathNode to);

	double getSourcePDF();

	boolean isSourceSingular();

	LightSampler getLightSampler();

}
