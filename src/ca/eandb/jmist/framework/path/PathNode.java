/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * A node in a <code>Path</code> used for path-integral based rendering
 * algorithms.
 * @author Brad Kimmel
 */
public interface PathNode {

	Color getCumulativeWeight();

	double getGeometricFactor();

	double getPDF();
	
	double getReversePDF();

	boolean isSpecular();

	PathNode getParent();

	HPoint3 getPosition();

	boolean isAtInfinity();

	boolean isOnEyePath();

	boolean isOnLightPath();

	int getDepth();

	ScatteredRay sample(double ru, double rv, double rj);

	Color scatter(Vector3 v);

	double getCosine(Vector3 v);

	double getPDF(Vector3 v);

	double getReversePDF(Vector3 v);

	ScatteringNode expand(double ru, double rv, double rj);
	
	double getRU();
	
	double getRV();
	
	double getRJ();
	
	PathNode reverse(PathNode newParent, PathNode grandChild);

	PathInfo getPathInfo();
	
}
