/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.ScatteredRayRecorder;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;

/**
 * @author Brad
 *
 */
public interface PathNode {

	Color getCumulativeWeight();

	double getGeometricFactor();

	double getPDF();

	double getReversePDF();

	boolean isSpecular();

	PathNode getParent();

	boolean isAtInfinity();

	boolean isOnEyePath();

	boolean isOnLightPath();

	HPoint3 getPosition();

	Color getBSDF(PathNode to);

	double getPDF(PathNode to);

	int getDepth();

	void scatter(ScatteredRayRecorder sr);

}
