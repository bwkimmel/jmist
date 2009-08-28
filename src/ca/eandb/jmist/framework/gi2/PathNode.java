/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;

/**
 * @author Brad
 *
 */
public interface PathNode {

	Color getCumulativeWeight();

	Color getWeight();

	double getGeometricFactor();

	double getForwardPDF();

	double getBackwardPDF();

	boolean isSpecular();

	PathNode getChild();

	PathNode getParent();

	HPoint3 getPosition();

	boolean isAtInfinity();

	boolean isOnEyePath();

	boolean isOnLightPath();

	int getDepth();

	Color scatterTo(PathNode node);

	double getCosine(PathNode node);

	PathNode expand();

	PathInfo getPathInfo();

}
