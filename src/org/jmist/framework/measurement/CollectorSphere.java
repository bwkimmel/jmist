/**
 *
 */
package org.jmist.framework.measurement;

import org.jmist.toolkit.SphericalCoordinates;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public interface CollectorSphere {

	void reset();
	void record(Vector3 v);
	void record(SphericalCoordinates v);

	int hits(int sensor);
	int[] hits();

	int sensors();

	double getSensorSolidAngle(int sensor);
	double getSensorProjectedSolidAngle(int sensor);

	SphericalCoordinates getSensorCenter(int sensor);

}
