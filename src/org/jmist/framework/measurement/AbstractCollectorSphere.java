/**
 *
 */
package org.jmist.framework.measurement;

import org.jmist.toolkit.SphericalCoordinates;
import org.jmist.toolkit.Vector3;

/**
 * A default implementation of <code>CollectorSphere</code>.
 * @author bkimmel
 */
public abstract class AbstractCollectorSphere implements CollectorSphere {

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract CollectorSphere clone();

	/**
	 * Initializes an <code>AbstractCollectorSphere</code>.
	 * @param sensors The number of sensors in this
	 * 		<code>CollectorSphere</code>.
	 */
	protected AbstractCollectorSphere(int sensors) {
		this.hits = new long[sensors];
	}

	/**
	 * Creates a copy of another <code>AbstractCollectorSphere</code>.
	 * @param other The <code>AbstractCollectorSphere</code> to copy.
	 */
	protected AbstractCollectorSphere(AbstractCollectorSphere other) {
		this.hits = other.hits.clone();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#hits(int)
	 */
	@Override
	public long hits(int sensor) {
		return this.hits[sensor];
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#record(org.jmist.toolkit.Vector3)
	 */
	@Override
	public void record(Vector3 v) {

		assert(!this.recording);

		this.recording = true;
		this.record(SphericalCoordinates.fromCartesian(v));
		this.recording = false;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#record(org.jmist.toolkit.SphericalCoordinates)
	 */
	@Override
	public void record(SphericalCoordinates v) {

		assert(!this.recording);

		this.recording = true;
		this.record(v.toCartesian());
		this.recording = false;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#reset()
	 */
	@Override
	public void reset() {
		for (int i = 0; i < this.hits.length; i++) {
			this.hits[i] = 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#sensors()
	 */
	@Override
	public int sensors() {
		return this.hits.length;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.measurement.CollectorSphere#merge(org.jmist.framework.measurement.CollectorSphere)
	 */
	@Override
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
	protected final void record(int sensor) {
		this.hits[sensor]++;
	}

	/** An array containing the number of hits to each sensor. */
	private final long[] hits;

	/**
	 * A value indicating whether one of the overloads of
	 * <code>AbstractCollectorSphere.record</code> is currently on the call
	 * stack.  This exists to prevent an infinite loop that would occur if
	 * neither overload is overridden in a derived class, as each method
	 * provides default behavior by calling the other.
	 */
	private transient boolean recording = false;

}
