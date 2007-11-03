/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.measurement.AbstractCollectorSphere;
import org.jmist.framework.measurement.CollectorSphere;
import org.jmist.toolkit.SphericalCoordinates;
import org.jmist.toolkit.Vector3;

/**
 * A <code>CollectorSphere</code> that collects rays into two buckets, one for
 * the upper hemisphere and one for the lower hemisphere.
 * @author bkimmel
 */
public final class SpectrophotometerCollectorSphere extends
		AbstractCollectorSphere {

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.AbstractCollectorSphere#record(org.jmist.toolkit.SphericalCoordinates)
	 */
	@Override
	public void record(SphericalCoordinates v) {
		if (v.polar() < (Math.PI / 2.0)) {
			this.record(UPPER_HEMISPHERE);
		} else {
			this.record(LOWER_HEMISPHERE);
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.AbstractCollectorSphere#record(org.jmist.toolkit.Vector3)
	 */
	@Override
	public void record(Vector3 v) {
		if (v.z() > 0.0) {
			this.record(UPPER_HEMISPHERE);
		} else {
			this.record(LOWER_HEMISPHERE);
		}
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
	 * @see org.jmist.framework.measurement.AbstractCollectorSphere#clone()
	 */
	@Override
	public CollectorSphere clone() {
		return new SpectrophotometerCollectorSphere(this);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#getSensorCenter(int)
	 */
	@Override
	public SphericalCoordinates getSensorCenter(int sensor) {
		assert(0 <= sensor && sensor < 2);
		return sensor == UPPER_HEMISPHERE ? SphericalCoordinates.NORMAL : SphericalCoordinates.ANTINORMAL;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#getSensorProjectedSolidAngle(int)
	 */
	@Override
	public double getSensorProjectedSolidAngle(int sensor) {
		return Math.PI;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#getSensorSolidAngle(int)
	 */
	@Override
	public double getSensorSolidAngle(int sensor) {
		return 2.0 * Math.PI;
	}

	/** The sensor ID for the upper hemisphere sensor. */
	private static final int UPPER_HEMISPHERE = 0;

	/** The sensor ID for the lower hemisphere sensor. */
	private static final int LOWER_HEMISPHERE = 1;

}
