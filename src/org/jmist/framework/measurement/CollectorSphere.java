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

	CollectorSphere clone();

	void reset();
	void record(Vector3 v);
	void record(SphericalCoordinates v);

	long hits(int sensor);

	void merge(CollectorSphere other) throws IllegalArgumentException;

	int sensors();

	double getSensorSolidAngle(int sensor);
	double getSensorProjectedSolidAngle(int sensor);

	SphericalCoordinates getSensorCenter(int sensor);

}
