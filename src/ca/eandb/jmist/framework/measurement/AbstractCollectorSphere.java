/**
 *
 */
package ca.eandb.jmist.framework.measurement;

import java.io.Serializable;

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A default implementation of <code>CollectorSphere</code>.
 * @author Brad Kimmel
 */
public abstract class AbstractCollectorSphere implements CollectorSphere,
		Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = -4858176134247615409L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#record(ca.eandb.jmist.math.SphericalCoordinates, ca.eandb.jmist.framework.measurement.CollectorSphere.Callback)
	 */
	public final void record(SphericalCoordinates v, Callback f) {
		record(v, f, null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#record(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.measurement.CollectorSphere.Callback)
	 */
	public final void record(Vector3 v, Callback f) {
		record(v, f, null);
	}

}
