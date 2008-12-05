/**
 *
 */
package ca.eandb.jmist.framework.measurement;

import ca.eandb.jmist.toolkit.SphericalCoordinates;
import ca.eandb.jmist.toolkit.Vector3;

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
