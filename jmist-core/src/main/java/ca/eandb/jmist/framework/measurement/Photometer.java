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

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.scatter.SurfaceScatterer;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.progress.DummyProgressMonitor;
import ca.eandb.util.progress.ProgressMonitor;

/**
 * @author Brad Kimmel
 *
 */
public final class Photometer {

  public Photometer(CollectorSphere collectorSphere) {
    this.collectorSphere = collectorSphere;
    this.sensorArray = new IntegerSensorArray(collectorSphere);
  }

  public void reset() {
    this.sensorArray.reset();
  }

  public void setSpecimen(SurfaceScatterer specimen) {
    this.specimen = specimen;
  }

  public void setIncidentAngle(SphericalCoordinates incident) {
    this.incident = incident;
    this.in = incident.unit().opposite().toCartesian();
  }

  public void setWavelength(double wavelength) {
    this.wavelength = wavelength;
  }

  public SurfaceScatterer getSpecimen() {
    return this.specimen;
  }

  public SphericalCoordinates getIncidentAngle() {
    return this.incident;
  }

  public double getWavelength() {
    return this.wavelength;
  }

  public CollectorSphere getCollectorSphere() {
    return this.collectorSphere;
  }
  
  public IntegerSensorArray getSensorArray() {
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

    for (int i = 0; i < n; i++) {

      if (--untilCallback <= 0) {

        double progress = (double) i / (double) n;

        if (!monitor.notifyProgress(progress)) {
          monitor.notifyCancelled();
          return;
        }

        untilCallback = progressInterval;

      }
      
      Vector3 v = specimen.scatter(SurfacePointGeometry.STANDARD, in, false, wavelength, rng);

      if (v != null) {
        collectorSphere.record(v, sensorArray);
      }

    }

    monitor.notifyProgress(1.0);
    monitor.notifyComplete();

  }

  private final IntegerSensorArray sensorArray;
  private final CollectorSphere collectorSphere;
  private SurfaceScatterer specimen;
  private SphericalCoordinates incident;
  private Vector3 in;
  private double wavelength;

  private static final long DEFAULT_PROGRESS_INTERVAL = 1000;

}
