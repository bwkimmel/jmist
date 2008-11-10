/**
 *
 */
package org.jmist.packages.measurement;

import java.io.Serializable;

import org.jmist.framework.measurement.AbstractCollectorSphere;
import org.jmist.framework.measurement.CollectorSphere;
import org.jmist.toolkit.SphericalCoordinates;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public final class EqualPolarAnglesCollectorSphere extends
		AbstractCollectorSphere implements Serializable {

	protected EqualPolarAnglesCollectorSphere(int sensors) {
		super(sensors);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.AbstractCollectorSphere#clone()
	 */
	@Override
	public CollectorSphere clone() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#getSensorCenter(int)
	 */
	public SphericalCoordinates getSensorCenter(int sensor) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#getSensorProjectedSolidAngle(int)
	 */
	public double getSensorProjectedSolidAngle(int sensor) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#getSensorSolidAngle(int)
	 */
	public double getSensorSolidAngle(int sensor) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.AbstractCollectorSphere#getSensor(org.jmist.toolkit.SphericalCoordinates)
	 */
	@Override
	protected int getSensor(SphericalCoordinates v) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.AbstractCollectorSphere#getSensor(org.jmist.toolkit.Vector3)
	 */
	@Override
	protected int getSensor(Vector3 v) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 7417152456495286098L;

}
