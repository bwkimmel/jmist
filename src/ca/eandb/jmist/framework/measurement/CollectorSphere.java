/**
 *
 */
package ca.eandb.jmist.framework.measurement;

import java.io.Serializable;

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public interface CollectorSphere extends Serializable {

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
