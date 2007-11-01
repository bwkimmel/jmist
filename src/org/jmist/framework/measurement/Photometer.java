/**
 *
 */
package org.jmist.framework.measurement;

import org.jmist.framework.Material;
import org.jmist.framework.reporting.ProgressMonitor;
import org.jmist.toolkit.SphericalCoordinates;

/**
 * @author bkimmel
 *
 */
public final class Photometer {

	public Photometer(CollectorSphere collectorSphere) {
		this.collectorSphere = collectorSphere;
	}

	public void reset() {
		this.collectorSphere.reset();
	}

	public void setSpecimen(Material specimen) {
		// TODO implement this method.

	}

	public void setIncidentAngle(SphericalCoordinates incident) {
		// TODO implement this method.

	}

	public void setWavelength(double lambda) {
		// TODO implement this method.

	}

	public Material getSpecimen() {
		// TODO implement this method.
		return null;
	}

	public SphericalCoordinates getIncidentAngle() {
		// TODO implement this method.
		return null;
	}

	public double getWavelength() {
		// TODO implement this method.
		return 0.0;
	}

	public CollectorSphere getCollectorSphere() {
		return this.collectorSphere;
	}

	public void castPhoton() {
		this.castPhotons(1);
	}

	public void castPhotons(long n) {
		// TODO implement this method.

	}

	public void castPhotons(long n, ProgressMonitor monitor) {
		this.castPhotons(n, monitor, DEFAULT_PROGRESS_INTERVAL);
	}

	public void castPhotons(long n, ProgressMonitor monitor, long progressInterval) {
		// TODO implement this method.

	}

	private final CollectorSphere collectorSphere;

	private static final long DEFAULT_PROGRESS_INTERVAL = 1000;

}
