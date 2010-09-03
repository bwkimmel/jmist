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

	public static interface Callback {
		void record(int sensor, Object obj);
	};
	
	void record(Vector3 v, Callback f, Object obj);
	void record(SphericalCoordinates v, Callback f, Object obj);
	
	void record(Vector3 v, Callback f);
	void record(SphericalCoordinates v, Callback f);
	
	int sensors();

	double getSensorSolidAngle(int sensor);
	double getSensorProjectedSolidAngle(int sensor);

	SphericalCoordinates getSensorCenter(int sensor);

}
