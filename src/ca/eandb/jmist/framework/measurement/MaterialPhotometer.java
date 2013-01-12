/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.measurement;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.measurement.CollectorSphere.Callback;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.progress.DummyProgressMonitor;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * @author Brad Kimmel
 *
 */
public final class MaterialPhotometer {

	private class PhotometerSurfacePoint implements SurfacePoint {
		
		public Basis3 getBasis() {
			return SurfacePointGeometry.STANDARD.getBasis();
		}

		public Vector3 getNormal() {
			return SurfacePointGeometry.STANDARD.getNormal();
		}

		public Point3 getPosition() {
			return SurfacePointGeometry.STANDARD.getPosition();
		}

		public int getPrimitiveIndex() {
			return SurfacePointGeometry.STANDARD.getPrimitiveIndex();
		}

		public Basis3 getShadingBasis() {
			return SurfacePointGeometry.STANDARD.getShadingBasis();
		}

		public Vector3 getShadingNormal() {
			return SurfacePointGeometry.STANDARD.getShadingNormal();
		}

		public Vector3 getTangent() {
			return SurfacePointGeometry.STANDARD.getTangent();
		}

		public Point2 getUV() {
			return SurfacePointGeometry.STANDARD.getUV();
		}

		public Medium getAmbientMedium() {
			return ambientMedium;
		}

		public Material getMaterial() {
			return specimen;
		}
		
	};
	
	public MaterialPhotometer(CollectorSphere collectorSphere, ColorModel colorModel) {
		this.collectorSphere = collectorSphere;
		this.sensorArray = new ColorSensorArray(collectorSphere.sensors(), colorModel);
	}

	public void reset() {
		this.sensorArray.reset();
	}
	
	public void setAmbientMedium(Medium medium) {
		this.ambientMedium = medium;
	}

	public void setSpecimen(Material specimen) {
		this.specimen = specimen;
	}

	public void setIncidentAngle(SphericalCoordinates incident) {
		this.incident = incident;
		this.in = incident.unit().opposite().toCartesian();
	}

	public void setWavelengthPacket(WavelengthPacket lambda) {
		this.lambda = lambda;
	}
	
	public Medium getAmbientMedium() {
		return this.ambientMedium;
	}

	public Material getSpecimen() {
		return this.specimen;
	}

	public SphericalCoordinates getIncidentAngle() {
		return this.incident;
	}

	public WavelengthPacket getWavelengthPacket() {
		return this.lambda;
	}

	public CollectorSphere getCollectorSphere() {
		return this.collectorSphere;
	}
	
	public ColorSensorArray getSensorArray() {
		return this.sensorArray;
	}

	public void castPhoton() {
		this.castPhotons(1);
	}

	public void castPhotons(long n) {
		this.castPhotons(n, DummyProgressMonitor.getInstance());
	}

	public void castPhotons(long n, ProgressMonitor monitor) {
		this.castPhotons(n, monitor, DEFAULT_PROGRESS_INTERVAL);
	}

	public void castPhotons(long n, ProgressMonitor monitor, long progressInterval) {

		long untilCallback = 0;
		Random rng = new SimpleRandom();
		int sqrt = (int) Math.floor(Math.sqrt(n));
		int nbox = sqrt * sqrt;

		for (int i = 0; i < n; i++) {

			if (--untilCallback <= 0) {

				double progress = (double) i / (double) n;

				if (!monitor.notifyProgress(progress)) {
					monitor.notifyCancelled();
					return;
				}

				untilCallback = progressInterval;

			}
			
			double ru = rng.next();
			double rv = rng.next();
			double rj = rng.next();
			
			if (i < nbox) {
				ru = (((double) (i % sqrt)) + ru) / (double) sqrt;
				rv = (((double) (i / sqrt)) + rv) / (double) sqrt;
			}
			
			ScatteredRay sr = specimen.scatter(surfacePoint, in, false, lambda, ru, rv, rj);

			if (sr != null) {
				Callback f = sensorArray.createCallback(sr.getColor());
				collectorSphere.record(sr.getRay().direction(), f);
			}

		}

		monitor.notifyProgress(1.0);
		monitor.notifyComplete();

	}

	private final SurfacePoint surfacePoint = new PhotometerSurfacePoint();
	private final ColorSensorArray sensorArray;
	private final CollectorSphere collectorSphere;
	private Medium ambientMedium = Medium.VACUUM;
	private Material specimen;
	private SphericalCoordinates incident;
	private Vector3 in;
	private WavelengthPacket lambda;

	private static final long DEFAULT_PROGRESS_INTERVAL = 1000;

}
