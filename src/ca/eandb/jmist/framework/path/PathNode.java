/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public interface PathNode {

	Color getCumulativeWeight();

	double getGeometricFactor();

	double getPDF();

	boolean isSpecular();

	PathNode getParent();

	HPoint3 getPosition();

	boolean isAtInfinity();

	boolean isOnEyePath();

	boolean isOnLightPath();

	int getDepth();

	ScatteredRay sample(Random rnd);

	Color scatter(Vector3 v);

	double getCosine(Vector3 v);

	double getPDF(Vector3 v);

	double getReversePDF(Vector3 v);

	ScatteringNode expand(Random rnd);

	PathInfo getPathInfo();

}
