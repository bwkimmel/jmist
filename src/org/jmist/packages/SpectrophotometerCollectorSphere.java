/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.measurement.AbstractCollectorSphere;
import org.jmist.framework.measurement.CollectorSphere;
import org.jmist.toolkit.SphericalCoordinates;

/**
 * @author bkimmel
 *
 */
public final class SpectrophotometerCollectorSphere extends
		AbstractCollectorSphere {

	public SpectrophotometerCollectorSphere() {
		super(2);
	}

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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#getSensorProjectedSolidAngle(int)
	 */
	@Override
	public double getSensorProjectedSolidAngle(int sensor) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#getSensorSolidAngle(int)
	 */
	@Override
	public double getSensorSolidAngle(int sensor) {
		// TODO Auto-generated method stub
		return 0;
	}

}
