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
	 * @see ca.eandb.jmist.framework.measurement.AbstractCollectorSphere#clone()
	 */
	@Override
	public CollectorSphere clone() {
		return new EqualProjectedSolidAnglesCollectorSphere(this);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#getSensorCenter(int)
	 */
	public SphericalCoordinates getSensorCenter(int sensor) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#getSensorProjectedSolidAngle(int)
	 */
	public double getSensorProjectedSolidAngle(int sensor) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#getSensorSolidAngle(int)
	 */
	public double getSensorSolidAngle(int sensor) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.AbstractCollectorSphere#getSensor(ca.eandb.jmist.toolkit.SphericalCoordinates)
	 */
	@Override
	protected int getSensor(SphericalCoordinates v) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.AbstractCollectorSphere#getSensor(ca.eandb.jmist.toolkit.Vector3)
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
