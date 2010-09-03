/**
 *
 */
package ca.eandb.jmist.framework.measurement;

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>CollectorSphere</code> that collects rays into two buckets, one for
 * the upper hemisphere and one for the lower hemisphere.
 * @author Brad Kimmel
 */
public final class SpectrophotometerCollectorSphere extends
		AbstractCollectorSphere {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#getSensorCenter(int)
	 */
	public SphericalCoordinates getSensorCenter(int sensor) {
		assert(0 <= sensor && sensor < 2);
		return sensor == UPPER_HEMISPHERE ? SphericalCoordinates.NORMAL : SphericalCoordinates.ANTINORMAL;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#getSensorProjectedSolidAngle(int)
	 */
	public double getSensorProjectedSolidAngle(int sensor) {
		return Math.PI;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#getSensorSolidAngle(int)
	 */
	public double getSensorSolidAngle(int sensor) {
		return 2.0 * Math.PI;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#record(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.measurement.CollectorSphere.Callback, java.lang.Object)
	 */
	public void record(Vector3 v, Callback f, Object obj) {
		f.record((v.z() > 0.0) ? UPPER_HEMISPHERE : LOWER_HEMISPHERE, obj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#record(ca.eandb.jmist.math.SphericalCoordinates, ca.eandb.jmist.framework.measurement.CollectorSphere.Callback, java.lang.Object)
	 */
	public void record(SphericalCoordinates v, Callback f, Object obj) {
		f.record((v.polar() < (Math.PI / 2.0)) ? UPPER_HEMISPHERE : LOWER_HEMISPHERE, obj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#sensors()
	 */
	public int sensors() {
		return 2;
	}

	/** The sensor ID for the upper hemisphere sensor. */
	private static final int UPPER_HEMISPHERE = 0;

	/** The sensor ID for the lower hemisphere sensor. */
	private static final int LOWER_HEMISPHERE = 1;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -6289494553073934175L;

}
