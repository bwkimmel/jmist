/**
 *
 */
package ca.eandb.jmist.framework.measurement;

import java.io.Serializable;

import ca.eandb.jmist.toolkit.SphericalCoordinates;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * A default implementation of <code>CollectorSphere</code>.
 * @author Brad Kimmel
 */
public abstract class AbstractCollectorSphere implements CollectorSphere,
		Serializable {

	/**
	 * Creates a new <code>AbstractCollectorSphere</code>.
	 * {@link #initialize(int)} must be called before the new
	 * <code>CollectorSphere</code> may be used.
	 */
	protected AbstractCollectorSphere() {
		this.hits = null;
	}

	/**
	 * Initializes an <code>AbstractCollectorSphere</code>.  This constructor
	 * implicitly calls {@link #initialize(int)}.
	 * @param sensors The number of sensors in this
	 * 		<code>CollectorSphere</code>.
	 * @see #initialize(int)
	 */
	protected AbstractCollectorSphere(int sensors) {
		this.initialize(sensors);
	}

	/**
	 * Creates a copy of another <code>AbstractCollectorSphere</code>.
	 * @param other The <code>AbstractCollectorSphere</code> to copy.
	 */
	protected AbstractCollectorSphere(AbstractCollectorSphere other) {
		this.hits = other.hits.clone();
	}

	/**
	 * Initializes this <code>AbstractCollectorSphere</code>.  This method must
	 * be called before this <code>CollectorSphere</code> may be used.  This
	 * method may only be called once per instance.
	 * @param sensors The number of sensors in this
	 * 		<code>CollectorSphere</code>.
	 * @throws IllegalStateException if this method has already been called on
	 * 		this instance.
	 */
	protected void initialize(int sensors) throws IllegalStateException {

		if (this.hits != null) {
			throw new IllegalStateException("AbstractCollectorSphere already initialized.");
		}

		this.hits = new long[sensors];

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract CollectorSphere clone();

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#hits(int)
	 */
	public long hits(int sensor) {
		return this.hits[sensor];
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#record(ca.eandb.jmist.toolkit.Vector3)
	 */
	public void record(Vector3 v) {
		this.record(this.getSensor(v));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#record(ca.eandb.jmist.toolkit.SphericalCoordinates)
	 */
	public final void record(SphericalCoordinates v) {
		this.record(this.getSensor(v));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#reset()
	 */
	public void reset() {
		for (int i = 0; i < this.hits.length; i++) {
			this.hits[i] = 0;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#sensors()
	 */
	public int sensors() {
		return this.hits.length;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.measurement.CollectorSphere#merge(ca.eandb.jmist.framework.measurement.CollectorSphere)
	 */
	public void merge(CollectorSphere other) throws IllegalArgumentException {

		if (this.getClass().equals(other.getClass())) {

			AbstractCollectorSphere toMerge = (AbstractCollectorSphere) other;

			if (this.hits.length == toMerge.hits.length) {

				for (int i = 0; i < this.hits.length; i++) {
					this.hits[i] += toMerge.hits[i];
				}

				return;

			}

		}

		throw new IllegalArgumentException("Incompatible CollectorSpheres");

	}

	/**
	 * Records a hit to the specified sensor.
	 * @param sensor The index of a sensor to record a hit to.
	 */
	private final void record(int sensor) {
		if (sensor != MISS) {
			this.hits[sensor]++;
		}
	}

	/**
	 * Gets the sensor that the specified vector (expressed in
	 * <code>SphericalCoordinates</code>) strikes.
	 * @param v The vector (expressed in <code>SphericalCoordinates</code>).
	 * @return The sensor that the specified vector strikes, or
	 * 		<code>AbstractCollectorSphere.MISS</code> if no sensor is struck.
	 */
	protected abstract int getSensor(SphericalCoordinates v);

	/**
	 * Gets the sensor that the specified <code>Vector3</code> strikes.
	 * @param v The <code>Vector3</code>.
	 * @return The sensor that the specified vector strikes, or
	 * 		<code>AbstractCollectorSphere.MISS</code> if no sensor is struck.
	 */
	protected abstract int getSensor(Vector3 v);

	/** A value indicating that the vector does not hit any sensor. */
	protected static final int MISS = -1;

	/** An array containing the number of hits to each sensor. */
	private long[] hits;

}
