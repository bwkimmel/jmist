/**
 *
 */
package ca.eandb.jmist.packages.measurement;

import ca.eandb.jmist.framework.measurement.AbstractCollectorSphere;
import ca.eandb.jmist.framework.measurement.CollectorSphere;
import ca.eandb.jmist.toolkit.SphericalCoordinates;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * A <code>CollectorSphere</code> that collects rays into two buckets, one for
 * the upper hemisphere and one for the lower hemisphere.
 * @author bkimmel
 */
public final class SpectrophotometerCollectorSphere extends
		AbstractCollectorSphere {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.AbstractCollectorSphere#getSensor(ca.eandb.jmist.toolkit.SphericalCoordinates)
	 */
	@Override
	protected int getSensor(SphericalCoordinates v) {
		return (v.polar() < (Math.PI / 2.0)) ? UPPER_HEMISPHERE : LOWER_HEMISPHERE;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.AbstractCollectorSphere#getSensor(ca.eandb.jmist.toolkit.Vector3)
	 */
	@Override
	protected int getSensor(Vector3 v) {
		return (v.z() > 0.0) ? UPPER_HEMISPHERE : LOWER_HEMISPHERE;
	}

	/**
	 * Creates a new <code>SpectrophotometerCollectorSphere</code>.
	 */
	public SpectrophotometerCollectorSphere() {
		super(2);
	}

	/**
	 * Creates a copy of an existing <code>SpectrophotometerCollectorSphere</code>.
	 * @param other The <code>SpectrophotometerCollectorSphere</code> to copy.
	 */
	public SpectrophotometerCollectorSphere(SpectrophotometerCollectorSphere other) {
		super(other);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.AbstractCollectorSphere#clone()
	 */
	@Override
	public CollectorSphere clone() {
		return new SpectrophotometerCollectorSphere(this);
	}

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

	/** The sensor ID for the upper hemisphere sensor. */
	private static final int UPPER_HEMISPHERE = 0;

	/** The sensor ID for the lower hemisphere sensor. */
	private static final int LOWER_HEMISPHERE = 1;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -6289494553073934175L;

}
