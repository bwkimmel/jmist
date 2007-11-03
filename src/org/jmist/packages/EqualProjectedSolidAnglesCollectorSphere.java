/**
 *
 */
package org.jmist.packages;

import java.io.Serializable;

import org.jmist.framework.measurement.AbstractCollectorSphere;
import org.jmist.framework.measurement.CollectorSphere;
import org.jmist.toolkit.SphericalCoordinates;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public final class EqualProjectedSolidAnglesCollectorSphere extends
		AbstractCollectorSphere implements Serializable {

	public EqualProjectedSolidAnglesCollectorSphere(int sensors) {
		super(sensors);
		// TODO Auto-generated constructor stub
	}

	public EqualProjectedSolidAnglesCollectorSphere(EqualProjectedSolidAnglesCollectorSphere other) {
		super(other);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.AbstractCollectorSphere#clone()
	 */
	@Override
	public CollectorSphere clone() {
		return new EqualProjectedSolidAnglesCollectorSphere(this);
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
	private static final long serialVersionUID = -6563158829889305695L;

}
